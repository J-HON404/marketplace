# Architettura del Sistema

Questa architettura rappresenta l’evoluzione dei concetti introdotti nei branch precedenti.

In particolare deriva dalle architetture presenti in:

- `stage/azure-app`
- `stage/local-dockerV2`

dove sono stati introdotti i concetti di **containerizzazione dei moduli**, sia in locale tramite **Docker** sia nel cloud tramite **Azure**.

Rispetto alla versione `stage/rest-version`, questa evoluzione introduce concetti più avanzati legati al **cloud computing** e ad alcune logiche tipiche delle **architetture orientate ai microservizi**.

Non si tratta ancora di una vera architettura a microservizi, ma di una **soluzione ibrida** che introduce alcuni principi dei microservizi mantenendo ancora alcune caratteristiche monolitiche. Rimangono quindi margini di miglioramento in termini di:

- scalabilità
- performance
- separazione dei domini
- gestione delle configurazioni
- comunicazione tra servizi

L’obiettivo è evolvere progressivamente verso una struttura più vicina a un’architettura **microservices-oriented**.

---

# Moduli Introdotti

In questa versione sono stati integrati i seguenti moduli:

- **Auth Module**  
  introdotto nello stage `stage/auth-service-backend`

- **API Gateway**  
  proveniente da `stage/api-gateway-backendV2`, migliorato in termini di **sicurezza** e **performance**

- **Backend API Module**  
  derivato da `stage/backend-api`, adattato per integrarsi correttamente con il gateway e la gestione dell’autenticazione

- **Frontend Module**  
  proveniente da `stage/marketplace-frontend`

---

# Componenti principali

## Auth Module

L’**Auth Module** gestisce l’identità e l’autenticazione degli utenti.

Responsabilità principali:

- registrazione utenti
- login
- generazione dei token **JWT**

Quando un utente effettua il login, il servizio verifica le credenziali e genera un **JWT firmato**, che verrà poi utilizzato dal client per autenticare tutte le richieste successive.

---

## API Gateway

L’**API Gateway** rappresenta il **punto di ingresso unico del sistema**.

Gestisce la sicurezza e instrada le richieste verso i servizi interni, evitando che i moduli backend siano esposti direttamente.

Responsabilità principali:

- validazione dei **JWT**
- routing delle richieste verso il servizio corretto
- blocco delle richieste non autorizzate

Solo le richieste con **token valido** vengono inoltrate ai servizi backend.

---

## Backend API

Il **Backend API Module** contiene la **business logic dell’applicazione**.

Gestisce funzionalità come:

- gestione shop
- gestione prodotti
- gestione ordini
- controlli di autorizzazione

Espone quindi gli endpoint utilizzati dal frontend per interagire con il sistema.

---

# Gestione della sicurezza

Il sistema utilizza un approccio a **doppio livello di sicurezza**.

Il primo livello è il **Gateway**, che valida il token JWT prima di inoltrare le richieste ai servizi backend.

Il secondo livello è il **Backend API**, che mantiene comunque un controllo indipendente tramite il filtro `JwtAuthenticationFilter`. Questo consente al backend di verificare il token nel caso in cui una richiesta provi a **bypassare il gateway**.

Se il token è assente o non valido, la richiesta viene rifiutata con `401 Unauthorized`.

Questo approccio implementa il principio di **defense-in-depth**, in cui più livelli del sistema contribuiscono alla sicurezza.

---

# Ottimizzazione tramite Header Custom

Per migliorare le performance è stato introdotto un meccanismo di comunicazione tra **Gateway** e **Backend** tramite **header custom**.

Quando il Gateway valida il JWT, estrae alcune informazioni dal token e le inoltra al backend tramite header come:

- `X-Profile-Id`
- `X-Role`
- `X-Shop-Id`

In questo caso il backend non deve:

- decodificare il JWT
- interrogare il database

e può costruire direttamente un oggetto `Authentication` leggero per i controlli di autorizzazione.

Se invece gli header non sono presenti, il backend attiva un **fallback completo**:

1. legge il JWT dall'header `Authorization`
2. verifica firma e scadenza
3. carica l’utente dal database tramite `CustomUserDetailsService`
4. popola il `SecurityContext`

Questo garantisce sicurezza anche nel caso di richieste che non passano dal gateway.

---

# Vantaggi dell’architettura

**Sicurezza**

- il gateway filtra le richieste non autorizzate
- il backend mantiene una validazione indipendente del token
- i servizi interni non sono esposti direttamente

**Performance**

- il backend evita query al database per ogni richiesta
- le informazioni utente vengono passate dal gateway tramite header

**Scalabilità e Manutenibilità**

- separazione tra autenticazione, gateway e backend applicativo
- ogni modulo ha una responsabilità chiara (frontend, gateway, business logic)

---

# Evoluzione verso microservizi

Attualmente questa architettura rappresenta una **fase intermedia tra monolite e microservizi**.

Per diventare completamente orientata ai microservizi sarebbe necessario introdurre:

- **service Discovery** per individuare dinamicamente i servizi
- **config Server** per la gestione centralizzata delle configurazioni
- **messaggistica asincrona** tra servizi (es. RabbitMQ)
- **database separati per servizio**

Attualmente infatti il sistema utilizza **un unico database** che contiene sia i dati di autenticazione sia quelli applicativi. In un'architettura a microservizi sarebbe preferibile separare questi domini in database distinti.

---
