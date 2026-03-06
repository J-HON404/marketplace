# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.

---

# 🚀 Obiettivi : Introduzione API Gateway 

L'obiettivo di questa fase è andare a migliorare l'api-gateway presente nel **branch stage/api-gateway-backend**. Quella versione di gateway usufruiva di un JwtAuthenticationFilter che andasse ad analizzare il token presente nell'header della richiesta e se non valido omancante andasse scartasse la richiesta inoltrando 401 Unauthorized, altrimenti la richiesta  con con all'interno dell'header il token originale verrebbe inoltrata ai servizi backend.
Adesso però con questa nuova versione, l'obiettivo è limitare le operazioni lato backend, permettendo a questi ultimi di effettuare controlli più specifici sulla richiesta, quindi lasciando al gateway il compito di scartare le richieste non valide perchè prive di autenticazione.

 **Manipolazione authorization header:**                                                                                                                                                      La seguente versione permette al gateway una volta ottenuta la richiesta, di andare ad analizzare il token presente nell'header ed estrapolare le informazioni necessarie per               determinarne la sua validità e completezza. Il passaggio integrato in questa versione, riguarda una manipolazione controllata dell'header della richiesta, poicè una volta analizzate le claims del token, il gateway andrà ad aggiungere le informazioni all'interno dell'header della richiesta, andandolo quindi a modificare. In questo modo il backend non dovrà più riceve ela richiesta con all'interno il token jwt originale da verificare, ma avrà pronti i campi presenti nelle claims direttamente nell'authorization header! Potrà quindi direttamente verificare le autorizzazioni sulla base delle informazioni ricevute

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
Il seguente filtro ha lo scopo di verificare l'autenticazione legata alle richieste HTTP che arrivano dall'esterno. L'autenticazione si basa su un token jwt, che viene analizzato per determinare se la richiesta può passare al backend ed i suoi servizi. Il seguente filtro richiede che sia presente questo token all'interno dell'header della richiesta. Non richiede un autenticazione nella richiesta solo nel caso la destinazione della richiesta fosse sugli enpoint backend legati all'accesso o registrazione nella piattaforma, perchè in quel caso l'utente non può essere ancora autenticato nel sistema. Nel caso invece mancasse autenticazione nella richiesta per le altre destinazioni, verrà restituito il codice *401 Unauthorized* e la richiesta verrà scartata, senza essere inoltrata ai servizi interni. Il gateway per verificare autenticità del token usufruisce del *jwt secret*, una chiave segreta per verificare autenticità del token e che non sia stato modficato o generato da una fonte non autorizzata.Se la verifica va a buon fine, il token viene considerato valido e il filtro estrae le claims, cioè le informazioni contenute all’interno del JWT. Le claims possono includere dati come l’identificativo dell’utente, il ruolo o la data di scadenza del token. Successivamente la richiesta viene inoltrata al backend. Concludendo Il suo compito è verificare che tutte le richieste destinate ai servizi interni backend contengano un token JWT valido. In questo modo si garantisce che solo gli utenti autenticati possano accedere alle risorse del sistema.


