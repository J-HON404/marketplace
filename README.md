# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.

---

## 🚀 Panoramica del Progetto

L'obiettivo è fornire una piattaforma completa con gestione separata dei ruoli e una logica di business che permetta dal caricamento del prodotto , alla vendita fino alla conferma di ricezione dell’ordine.

* **Architettura:** REST a livelli (Controller, Service, Repository).
* **Frontend:** Single Page Application (SPA) .
* **Sicurezza:** Autenticazione  basata su **JWT** con Spring Security.

---

## ⚙️ Funzionalità Principali

### 🔐 Autenticazione e Sicurezza
* Registrazione e login con distinzione ruoli (`SELLER` / `CUSTOMER`).
* Generazione e validazione dei token **JWT**.
* Controllo degli accessi basato su ruoli.
* Protezione delle API tramite filtri Spring Security.

### 🏪 Area Venditore (SELLER)
* **Shop Management:** Gestione completa del profilo negozio.
* **Gestione Catalogo:** CRUD prodotti, gestione stock e date di disponibilità.
* **Logistica:** Monitoraggio ordini, inserimento **Tracking ID** e data di consegna stimata.
* **Alert:** Creazione di avvisi, promozioni ed annunci sui prodotti venduti.

### 🛍 Area Acquirente (CUSTOMER)
* **Shopping:** Navigazione tra negozi e prodotti disponibili.
* **Carrello:** Gestione dinamica delle quantità e processo di checkout.
* **Tracking:** Storico ordini e conferma di ricezione consegna.
* **Alert:** Ricezione di annunci e promozioni dedicate.

---

## 🧱 Architettura Tecnica

### **Backend**
* **Java 21** & **Spring Boot 3.5.8**
* **Persistenza:** Spring Data JPA con **MariaDB**.
* **Pattern:** DTO, Global Exception Handling, Server-side Validation.

---

## 🗄 Struttura del Database



| Entità | Descrizione |
| :--- | :--- |
| **Profile** | Anagrafica utenti, credenziali e ruoli. |
| **Shop** | Dettagli del negozio associato a un venditore. |
| **Product** | Catalogo articoli, prezzi e inventario. |
| **Cart** | Stato del carrello corrente dell'utente. |
| **Order** | Dettagli transazione, tracking e stato consegna. |
| **ProductNotice** | Avvisi e promozioni attive sui prodotti. |

---

## 📁 Organizzazione delle Cartelle

### **Backend** 
```text
└── marketplace
    ├── controller    # Endpoint REST API
    ├── dto           # Data Transfer Objects
    ├── exception     # Handler per errori e risposte personalizzate
    ├── model         # Entity JPA 
    ├── repository    # Interfacce Spring Data JPA
    ├── security      # Configurazione JWT, filtri e permessi
    └── service       # Logica di business applicativa
```


## 🔗 Analisi JwtAuthenticationFilter

Il backend-api dovrebbe limitarsi ad esporre gli enpoint pubblici dell'api, tuttavia per garantire la sicurezza nell'accesso alle informazioni dell'applicazione, è necessario verificare che le richieste ricevute dal frontend, siano autenticate , per poi andare a verificare le autorizzazioni e vietare l'accesso nel caso non si abbiano i permessi richiesti. 
Il backend-api dovrà quindi dove è necessario analizzare la richiesta ricevuta, prendendo dall'header il token jwt ed estrendo le claims, utili per autenticare l'utente e le autorizzazioni associate. La versione attuale del backend-api tramite il filtro  JwtAuthenticationFilter che gli permette di intercettare le richieste HTTP ricevute, gli permrette di capire quando è necessario analizzare in interezza la validità del token jwt e quando invece la richiesta è già stata validata e necessità di ulteriori verifiche. Questo è possibile manipolando l'header della richiesta ed inserendo direttamente i valori della claims del token, ch permettono al backend di non preoccuparsi quindi di estrarle dla token jwt, perchè è già stato fatto. Tuttavavia ci potrebbero essere situazioni in cui il token jwt nell'header della richiesta non è stato analizzato e di conseguenza dovrà essere analizzato dal backend-api, per poi nel caso fosse valido estrarre dal body le claims. L'obiettivo è quindi facilitare la fase di autenticazione nei confronti del backend-api, ma allo stesso tempo garantire la sicurezza nel caso non sia stato fatto un controllo preventivo sul token e quindi il backend debba in prima persona verificarlo, prima di decidere se elaborare la richiesta.

----

```dockerfile
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //controllo se il gateway ha passato gli header
        String gatewayProfileId = request.getHeader("X-Profile-Id");
        String gatewayRole = request.getHeader("X-Role");
        String gatewayShopId = request.getHeader("X-Shop-Id");

        if (gatewayProfileId != null && gatewayRole != null) {
            // Authentication leggero direttamente dalle info del gateway
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + gatewayRole);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            new JwtPrincipal(Long.parseLong(gatewayProfileId),
                                    gatewayRole,
                                    gatewayShopId != null ? Long.parseLong(gatewayShopId) : null),
                            null,
                            Collections.singletonList(authority)
                    );
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
            return;
        }
        // legge JWT dall'header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                if (SecurityContextHolder.getContext().getAuthentication() == null &&
                        jwtUtil.isTokenValid(token, username)) {
                    // Carica utente dal DB
                    CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                    return; // token valido → request continua
                }
            } catch (Exception e) {
                logger.warn("JWT validation failed: " + e.getMessage());
            }
        }
        // token assente o non valido → blocca con 401
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Unauthorized: Invalid or missing JWT token");
        response.getWriter().flush();
    }
    /*** Classe interna per rappresentare principal “leggero” basato sulle info del gateway.**/
    public static class JwtPrincipal {
        private final Long profileId;
        private final String role;
        private final Long shopId;
        public JwtPrincipal(Long profileId, String role, Long shopId) {
            this.profileId = profileId;
            this.role = role;
            this.shopId = shopId;
        }
        public Long getProfileId() { return profileId; }
        public String getRole() { return role; }
        public Long getShopId() { return shopId; }
    }
}
```
