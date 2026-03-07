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

## 🔐 Gestione Autenticazione e Autorizzazione nel Backend API

Il **backend-api** ha il compito principale di esporre gli endpoint dell'applicazione.  
Tuttavia, per garantire la sicurezza nell'accesso alle informazioni, è necessario verificare che le richieste ricevute dal frontend siano **autenticate** e che l'utente disponga delle **autorizzazioni necessarie** per accedere alle risorse richieste.

Per questo motivo il backend analizza le richieste HTTP ricevute e verifica la presenza di un **token JWT** all'interno dell'header della richiesta. Dal token vengono estratte le **claims**, che contengono le informazioni necessarie per identificare l'utente e i suoi permessi.

----

## 🔗 Analisi JwtAuthenticationFilter

L'attuale implementazione del backend utilizza un filtro chiamato `JwtAuthenticationFilter`, che intercetta tutte le richieste HTTP in ingresso.  
Questo filtro consente di determinare se la richiesta debba essere completamente validata oppure se sia già stata verificata da un componente a monte dell'architettura.

Il filtro permette quindi di distinguere due scenari principali.

---

### 1️⃣ Richiesta già validata da un gateway

In alcuni casi la richiesta può provenire da un **API Gateway** o da un servizio intermedio che ha già verificato la validità del token JWT.

In questo scenario:

- le informazioni contenute nelle **claims** del token vengono inserite direttamente negli **header della richiesta**
- il backend può leggere queste informazioni senza dover analizzare nuovamente il token
- si evita quindi il processo di validazione del JWT

Questo approccio consente di:

- ridurre il carico sul backend
- velocizzare il processo di autenticazione
- delegare parte della sicurezza a un componente centralizzato dell'architettura

---

### 2️⃣ Richiesta non ancora validata

In altri casi il token JWT potrebbe essere presente nell'header della richiesta ma **non essere stato ancora analizzato**.

In questo scenario il backend deve:

1. leggere il token JWT dall'header `Authorization`
2. verificarne la validità (firma e scadenza)
3. estrarre le **claims**
4. utilizzare tali informazioni per autenticare l'utente e determinarne i permessi

Se il token risulta **non valido o assente**, la richiesta viene bloccata e viene restituita una risposta **HTTP 401 Unauthorized**.

---

```dockerfile
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

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
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                if (SecurityContextHolder.getContext().getAuthentication() == null &&
                        jwtUtil.isTokenValid(token, username)) {
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
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Unauthorized: Invalid or missing JWT token");
        response.getWriter().flush();
    }
}
```
---

## 🎯 Obiettivo dell'architettura

L'obiettivo di questo approccio è:

- **facilitare il processo di autenticazione** quando la richiesta è già stata verificata
- **garantire comunque la sicurezza del sistema** quando il backend deve effettuare autonomamente la validazione del token

In questo modo il backend-api rimane flessibile e compatibile con architetture più complesse, mantenendo allo stesso tempo un livello di sicurezza nell'accesso alle risorse dell'applicazione.
