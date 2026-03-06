# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.

---

# 🚀 Obiettivi : Introduzione API Gateway 

L'obiettivo di questa fase è andare a migliorare l'api-gateway presente nel **branch stage/api-gateway-backend**. Quella versione di gateway usufruiva di un JwtAuthenticationFilter che andasse ad analizzare il token presente nell'header della richiesta e se non valido omancante andasse scartasse la richiesta inoltrando 401 Unauthorized, altrimenti la richiesta  con con all'interno dell'header il token originale verrebbe inoltrata ai servizi backend.
Adesso però con questa nuova versione, l'obiettivo è limitare le operazioni lato backend, permettendo a questi ultimi di effettuare controlli più specifici sulla richiesta, quindi lasciando al gateway il compito di scartare le richieste non valide perchè prive di autenticazione.

 **Manipolazione authorization header:**                                                                                                                                                                                                                                                                                                                                                  La seguente versione permette al gateway una volta ottenuta la richiesta, di andare ad analizzare il token presente nell'header ed estrapolare le informazioni necessarie per               determinarne la sua validità e completezza. Il passaggio integrato in questa versione, riguarda una manipolazione controllata dell'header della richiesta, poicè una volta analizzate le claims del token, il gateway andrà ad aggiungere le informazioni all'interno dell'header della richiesta, andandolo quindi a modificare. In questo modo il backend non dovrà più riceve ela richiesta con all'interno il token jwt originale da verificare, ma avrà pronti i campi presenti nelle claims direttamente nell'authorization header! Potrà quindi direttamente verificare le autorizzazioni sulla base delle informazioni ricevute

## ⚙️ Analisi Filtro API Gateway
Il nucleo della logica applicativa del gateway si basa sul seguente JwtAuthenticationFilter aggiornato

```dockerfile
/**
 * Filtro JWT  API Gateway.
 * - Blocca la request se JWT non valido
 * - Estrae claims principali se valido
 * - Invia header X-Profile-Id, X-Role, X-Shop-Id ai microservizi
 */
@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtGatewayFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Token assente → blocca con 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            // Se il token non è valido → blocca con 401
            if (!jwtUtil.isTokenValid(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Token valido → estrai claims
            Long profileId = jwtUtil.extractProfileId(token);
            String role = jwtUtil.extractRole(token);
            Long shopId = jwtUtil.extractShopId(token);

            // Aggiungi header ai microservizi
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

### Considerazioni
La versione precedente del gateway passava il token JWT originale ai microservizi backend. Il backend doveva verificare la validità del token da solo. Se il token era assente o invalido → 401. Se il token era valido → veniva inoltrata la richiesta con il token originale nell’header Authorization. La nuova versione punta a ridurre il carico lato backend, delegando al gateway: Validazione JWT: verifica firma, scadenza e claims principali. Blocco immediato delle richieste non valide (401 Unauthorized). L'api gateway aggiunge nell'header solo i campi necessarie:     X-Profile-Id , X-Role , X-Shop-Id, 
in questo modo il backend non deve più decodificare il token JWT originale. Può basarsi direttamente sugli header per:

1)Determinare il profilo utente
2)Applicare autorizzazioni con @PreAuthorize o controlli specifici

**Vantaggi principali ottenuti:**  

Sicurezza centralizzata : il gateway blocca subito le richieste non valide.

Backend più leggero: non deve decodificare token JWT per ogni richiesta.

Flessibilità: il backend può usare le informazioni del gateway per autorizzazioni rapide, oppure fare controlli aggiuntivi solo se necessario.



