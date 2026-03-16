# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.

---

# 🚀 Obiettivi : API Gateway V2

L'obiettivo di questa fase è andare a migliorare l'api-gateway presente nel **branch stage/api-gateway-backend**. Quella versione di gateway usufruiva di un JwtAuthenticationFilter che andasse ad analizzare il token presente nell'header della richiesta e se non valido o mancante, andasse a scartare la richiesta inoltrando 401 Unauthorized, altrimenti la richiesta  con con all'interno dell'header il token originale verrebbe inoltrata ai servizi backend.
Adesso però con questa nuova versione, l'obiettivo è limitare le operazioni lato backend, permettendo a questi ultimi di effettuare controlli più specifici sulla richiesta, quindi lasciando al gateway il compito di scartare le richieste non valide perchè prive di autenticazione.


---

 **Manipolazione Authorization header:**                                                                                                                                                 
 La seguente versione permette al gateway una volta ottenuta la richiesta, di andare ad analizzare il token presente nell'header ed estrapolare le informazioni necessarie per               determinarne la sua validità e completezza. Il passaggio integrato in questa versione, riguarda una manipolazione controllata dell'header della richiesta, poicè una volta analizzate le claims del token, il gateway andrà ad aggiungere le informazioni all'interno dell'header della richiesta, andandolo quindi a modificare. In questo modo il backend non dovrà più riceve la richiesta con all'interno il token jwt originale da verificare, ma avrà pronti i campi presenti nelle claims direttamente nell'authorization header! Potrà quindi direttamente verificare le autorizzazioni sulla base delle informazioni ricevute

## ⚙️ Analisi Filtro API Gateway
Il nucleo della logica applicativa del gateway si basa sul seguente JwtAuthenticationFilter aggiornato

```dockerfile
/**
 * Filtro JWT API Gateway aggiornato.
 * - Blocca le richieste non valide
 * - Salta le richieste verso /api/auth
 * - Estrae claims principali se il token è valido
 * - Aggiunge header X-Profile-Id, X-Role, X-Shop-Id ai microservizi
 */
@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtGatewayFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().toString();

        // Salta la validazione JWT per il modulo di autenticazione
        if (path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtUtil.isTokenValid(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Token valido → estrai claims principali
            Long profileId = jwtUtil.extractProfileId(token);
            String role = jwtUtil.extractRole(token);
            Long shopId = jwtUtil.extractShopId(token);

            // Aggiungi header custom ai microservizi
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-Profile-Id", String.valueOf(profileId))
                    .header("X-Role", role)
                    .header("X-Shop-Id", shopId != null ? String.valueOf(shopId) : "")
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

            return chain.filter(mutatedExchange);

        } catch (Exception e) {
            // Qualsiasi eccezione → blocca la request
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1; // esegue prima degli altri filtri
    }
}

```

---
### Considerazioni
La versione precedente del gateway inoltrava unicamente il token JWT originale ai microservizi backend. Il backend doveva verificare la validità del token per ogni richiesta ricevuta. Se il token era assente o invalido → 401. Se il token era valido → veniva inoltrata la richiesta con il token originale nell’header Authorization. La nuova versione punta a ridurre l'autenticazione lato backend, delegando al gateway, il quale: verifica firma, scadenza e claims principali. Avviene un blocco immediato delle richieste non valide (401 Unauthorized). L'api gateway in caso di verifica andata a buon fine, aggiunge nell'header i seguenti campi:                                                                                                                                                                                  
 X-Profile-Id , X-Role , X-Shop-Id (se è un seller) .
In questo modo il backend non deve più decodificare il token JWT originale. Può basarsi direttamente sugli header per:
Determinare il profilo utente e applicare autorizzazioni con @PreAuthorize o controlli specifici

**Vantaggi principali ottenuti:**  

*Sicurezza centralizzata* : il gateway blocca subito le richieste non valide.

*Backend più leggero*: non deve decodificare token JWT per ogni richiesta.

*Flessibilità*: il backend può usare le informazioni del gateway per autorizzazioni rapide, oppure fare controlli aggiuntivi solo se necessario.

---

## LIbrerie Springboot utilizzate
Ogni richiesta del client viene rievuta da *Spring WebFlux*, una libreria in grado di gestire richieste Http in modo asincrono non bloccando i thread dell'applicazione e creando un oggetto ServerWebExchange che rappresenta la request e response. In supporto è presente *Spring Cloud Gateway* che permette la definizone delle rotte disponibili ed invoca i filtri definiti. In questo caso l'unico filtro in grado di elaborare le richieste è il JwtGatewayFilter.

---

# JWT Gateway Filter – Flusso e gestione dell’immutabilità

Questo filtro nel Gateway ha il compito di **controllare il token JWT delle richieste in ingresso** e, se il token è valido, **inoltrare alcune informazioni dell’utente ai microservizi tramite header HTTP**.

Quando una richiesta arriva al Gateway,la prima cosa che viene controllata è il **path della richiesta**. Se la richiesta è diretta verso `/api/auth`, il filtro **salta completamente la validazione del token** perché queste rotte servono per login o registrazione. In questo caso la richiesta viene semplicemente inoltrata al servizio di autenticazione.

Per tutte le altre richieste il filtro controlla la presenza dell’header `Authorization`. Se l’header non esiste oppure non inizia con `Bearer`, la richiesta viene immediatamente bloccata con **HTTP 401 Unauthorized**.

Se l’header è presente, il token viene estratto e validato tramite `JwtUtil`. Se il token non è valido o genera un’eccezione, la richiesta viene nuovamente bloccata con **401**. Se invece il token è valido, dal token vengono estratte alcune informazioni importanti (claims), come ad esempio:

- `profileId`
- `role`
- `shopId`

Queste informazioni servono ai microservizi per sapere **chi sta facendo la richiesta e con quali permessi**, senza dover decodificare il JWT ogni volta.

A questo punto il Gateway deve **aggiungere queste informazioni come header HTTP** alla richiesta prima di inoltrarla ai microservizi.

Qui entra in gioco un concetto importante di **Spring WebFlux**: le richieste HTTP sono **immutabili**.

Immutabile significa che **l’oggetto request non può essere modificato dopo essere stato creato**. Non è quindi possibile aggiungere header direttamente alla richiesta originale. Questo design è stato scelto per garantire sicurezza nei thread e per supportare il modello di programmazione **reattivo e non bloccante**.

Per gestire questa limitazione, Spring mette a disposizione il metodo `mutate()`. Questo metodo **non modifica la richiesta originale**, ma crea **una nuova copia della richiesta basata su quella originale**, permettendo di applicare modifiche come l’aggiunta di header.

Il filtro quindi crea una **nuova request mutata** con gli header `X-Profile-Id`, `X-Role` e `X-Shop-Id`.

Dato che anche l’oggetto `ServerWebExchange` è immutabile, viene creato anche un **nuovo exchange** che contiene la richiesta mutata.

Infine il filtro passa questo nuovo exchange alla catena dei filtri (`chain.filter(mutatedExchange)`), e il Gateway inoltra la richiesta ai microservizi.

Dal punto di vista del microservizio, la richiesta arriverà normalmente ma con alcuni header aggiuntivi contenenti le informazioni dell’utente estratte dal JWT.



