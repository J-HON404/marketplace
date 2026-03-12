# Architettura del Sistema

Questa architettura rappresenta l’evoluzione dei concetti e delle criticità introdotte nei branch precedenti.

In particolare deriva dalle architetture presenti in:

- `stage/azure-app`
- `stage/local-dockerV2`

dove sono stati introdotti i concetti di **containerizzazione dei moduli**, sia in locale tramite **Docker** sia nel cloud tramite **Azure**.

Rispetto alla versione `stage/rest-version`, questa evoluzione introduce concetti più avanzati legati al **cloud computing** e ad alcune logiche tipiche delle **architetture orientate ai microservizi**.

Non si tratta ancora di una vera architettura a microservizi, ma di una **soluzione ibrida** tra un applicazione cloud based e cloud native, poichè introduce alcuni principi dei microservizi mantenendo ancora alcune caratteristiche monolitiche. Rimangono quindi margini di miglioramento in termini di:

- scalabilità
- performance
- separazione dei domini
- gestione delle configurazioni
- comunicazione tra servizi

L’obiettivo è evolvere progressivamente verso una struttura più vicina a un’architettura **microservices-oriented**.

---

# Modifiche Introdotte

In questa versione sono stati migliorati i seguenti moduli:

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

## Frontend Module

Il Frontend Module è l’interfaccia utente dell’applicazione, sviluppata in **Angular** e servita tramite **Nginx** come web server.  

**Responsabilità principali:**
- Gestire l’interfaccia utente per `SELLER` e `CUSTOMER`.
- Comunicare con il sistema tramite l’**API Gateway**, evitando accessi diretti ai servizi backend.
- Gestire il **routing lato client** 
- Configurare il **reverse proxy Nginx** per:
  - instradare le richieste `/api/` verso l’API Gateway
  - gestire correttamente CORS e timeout
  - rendere la SPA indipendente dall’ambiente backend

In questo modo, il frontend rimane **portabile, scalabile e sicuro**, potendo puntare a diversi ambienti senza modificare l’applicazione.

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

---

# Considerazioni

L'applicazione al momento non permette **Alta disponbilità** e **Fault Tollerance***. Questa scelta è stata fatta per rispettare la natura dell'applicazione stessa, che evidenzia un architettura che nonostante è maggiormente distribuita rispetto alla sua prima versione **stage/rest-version**, è rimasta però sempre fedele alla filosofia iniziale, cioè un piccolo marktplace, non pensato per gestire alti carichi di richieste. Per questi motivi non è stato integrato un **Load Balancer** di supporto all' api-gateway, in grado di distribuire il carico verso i moduli backend, gestendo quindi **Auto-scaling** dei container.

---

# Database condiviso

Attualmente l'applicazione utilizza **un unico database condiviso** tra i moduli backend.  
Questa scelta non rappresenta l'approccio ideale nel caso di una futura migrazione verso un'**architettura a microservizi**, dove generalmente ogni servizio possiede il proprio database. Tuttavia, nel contesto attuale dell'applicazione, mantenere un database unico è risultato essere il compromesso più conveniente.

Il database contiene:

- gli **schemi logici relativi ai dati dell'API**
- una **tabella `profiles`** dedicata agli utenti dell'applicazione

Una possibile evoluzione dell'architettura potrebbe prevedere l'introduzione di un **database dedicato all'autenticazione (`DB_AUTH`)**, separando quindi:

- **dati di autenticazione e profilo utente**
- **dati di dominio dell'API**

Questa separazione potrebbe facilitare una futura **scalabilità indipendente** dei servizi. Tuttavia, nel contesto attuale dell'applicazione, tale scelta introdurrebbe alcune complessità aggiuntive.

#### 1. Maggiore complessità gestionale

Con due database distinti:

- `backend_api` dovrebbe accedere sia a:
  - `DB_AUTH` → per verificare identità e autorizzazioni dell'utente
  - `marketplace_db` → per gestire i dati dell'API
- di conseguenza sarebbe necessario gestire **più connection string** e una maggiore complessità nella configurazione del backend.

#### 2. Duplicazione o sincronizzazione dei dati utente

Se il database di autenticazione contenesse la tabella `profiles`, si presenterebbero due possibili scenari:

**Replica della tabella `profiles` nel database applicativo**

- sarebbe necessario replicare le informazioni utente anche nel `marketplace_db`
- questo richiederebbe meccanismi di **sincronizzazione tra database**, aumentando la complessità dell'infrastruttura.

**Tabella `profiles` presente solo in `DB_AUTH`**

- il `backend_api` dovrebbe interrogare il database di autenticazione ogni volta che necessita di informazioni utente
- questo introdurrebbe **dipendenze tra servizi** e maggiore latenza nelle operazioni.

#### 3. Overengineering rispetto ai requisiti attuali

Il database di autenticazione conterrebbe **un solo schema logico**, limitato alla gestione degli utenti.  
In questa fase del progetto, introdurre un database separato rappresenterebbe quindi una **complessità architetturale non necessaria** per le necessità dell'applicazione.

## Soluzione adottata

Il progetto utilizza **un unico database condiviso** con **separazione logica dei ruoli** tramite utenti MySQL con permessi diversi.

* Cartella `docker-init/` con tre script eseguiti in ordine:

1. **01-schema.sql** – crea schema e tabelle.  
2. **02-data.sql** – popola le tabelle con dati iniziali .  
3. **03-users.sql** – crea utenti MySQL e assegna privilegi specifici.

* Utenti MySQL e permessi

- **marketuser** – backend API principale  
  - Accesso completo a tutte le tabelle  
  - Gestione completa delle operazioni CRUD  

- **authuser** – servizio di autenticazione  
  - Accesso limitato a `profiles` e `shops`  
  - Operazioni: `SELECT`, `INSERT`, `UPDATE`  
  - Gestione credenziali e generazione token JWT

Il backend-api module si connetterà al db con utente marketuser, per recuperare i dati dell'api ed accedere quando necessario ai dati utenti.
Il backend-auth-module potrà accedere al db con utente authuser, per poter gestire le credenziali utenti e poter generare token jwt

## Vantaggi

- Maggiore **sicurezza**
- **Isolamento tra moduli**  
- **Gestione semplificata** con un unico database
  
---

# Evoluzione verso microservizi

Attualmente questa architettura rappresenta una **fase intermedia tra monolite e microservizi**.

Per diventare completamente orientata ai microservizi sarebbe necessario introdurre:

- **Service Discovery** per individuare dinamicamente i servizi
- **Alta disponbilità**
- **Fault tollerance**
- **Database separati per servizio**

---
