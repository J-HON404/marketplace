# 🛒 Marketplace Web Application

Un marketplace full-stack basato su **Spring Boot** e **Angular**, progettato per un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale.  

> ⚠️ Nota: Il sistema non è pensato per gestire carichi ad alta scala o scenari enterprise complessi, ma rappresenta una base sicura, modulare e facilmente estendibile per progetti di media dimensione.

---

## 🚀 Panoramica del Progetto

L'applicazione permette di gestire tutto il processo di vendita, dalla creazione del prodotto fino alla conferma della ricezione dell’ordine.

**Architettura principale:**
- REST a livelli (Controller, Service, Repository)
- Single Page Application (SPA) con Angular
- Sicurezza basata su **JWT** e Spring Security
- Containerizzazione tramite Docker e orchestrazione con Docker Compose

---

## ⚙️ Funzionalità Principali

### 🔐 Autenticazione e Sicurezza
- Registrazione e login con distinzione ruoli (`SELLER` / `CUSTOMER`)
- Generazione e validazione dei token **JWT**
- Controllo degli accessi basato su ruoli
- Protezione delle API tramite filtri Spring Security

### 🏪 Area Venditore (SELLER)
- **Gestione Shop:** Profilo negozio completo
- **Catalogo Prodotti:** CRUD prodotti, gestione stock e date di disponibilità
- **Logistica:** Monitoraggio ordini, inserimento Tracking ID e data consegna stimata
- **Alert:** Creazione di promozioni e avvisi sui prodotti

### 🛍 Area Acquirente (CUSTOMER)
- **Shopping:** Navigazione tra negozi e prodotti
- **Carrello:** Gestione dinamica delle quantità e checkout
- **Tracking:** Storico ordini e conferma ricezione consegna
- **Alert:** Ricezione di annunci e promozioni dedicate

---

## 🧱 Architettura Tecnica

### Backend
- **Linguaggio:** Java 21 & Spring Boot 3.5.8
- **Database:** MariaDB con Spring Data JPA
- **Pattern:** DTO, gestione globale eccezioni, validazione lato server

### Frontend
- **Framework:** Angular 20.3 & TypeScript
- **Core:** Standalone Components, Angular Routing
- **Networking:** HttpInterceptor per iniezione automatica JWT
- **Reattività:** Gestione asincrona tramite **RxJS** (Observable)

---

## 🗄 Struttura del Database

| Entità | Descrizione |
| ------ | ----------- |
| **Profile** | Anagrafica utenti, credenziali e ruoli |
| **Shop** | Dettagli del negozio associato al venditore |
| **Product** | Catalogo articoli, prezzi e inventario |
| **Cart** | Stato del carrello corrente dell'utente |
| **Order** | Dettagli transazione, tracking e stato consegna |
| **ProductNotice** | Avvisi e promozioni attive sui prodotti |

---

# 🏗️ Legenda dei Branch

Per comprendere a pieno lo sviluppo dell'applicazione nel tempo e l'evoluzione architetturale fino alla sua forma finale, è consigliato seguire i branch nell'ordine cronologico descritto di seguito.  

I branch documentano l'evoluzione dell'applicazione, dalla versione base fino alla versione finale, orientata a diventare un'architettura cloud-native e a microservizi.

---

  ## Versioni iniziali (REST API e contenirizzazione Docker)

- **stage/rest-version**  
  Prima versione dell'applicazione, nella sua forma base come REST API.

- **stage/local-docker**  
  Primo approccio di contenerizzazione dell'applicazione in locale con Docker.

- **stage/local-dockerV2**  
  Evoluzione del primo approccio di contenerizzazione locale con Docker, con l'introduzione di concetti e migliorie.

---

## Evoluzione verso architettura cloud

Successivamente, l'applicazione è stata evoluta con l'intento di renderla inizialmente Cloud Based e successivamente affinare l'architettura verso un modello orientato ai microservizi. Le versioni finali permettono una evoluzione agile dei moduli.  

- **stage/backend-api**  
  Versione 1.0 del backend, con analisi tecnica delle componenti e identificazione delle aree di sviluppo.

- **stage/marketplace-frontend**  
  Versione 1.0 del frontend, con analisi tecnica delle componenti e identificazione delle aree di sviluppo.

---

## Migliorie e deploy su Microsoft Azure

L'applicazione è stata successivamente costruita e distribuita sul cloud Microsoft Azure. I branch seguenti rappresentano le migliorie strutturali in questa fase:

- **stage/azure-app**  
  Primo approccio di deploy su Azure, con descrizione delle tecnologie utilizzate e delle configurazioni effettuate per la migrazione.

- **stage/api-gateway-backend**  
  Prima versione dell'API Gateway, utile a disaccoppiare la dipendenza tra container frontend e backend, favorendo un approccio distribuito e scalabile.

- **stage/api-gateway-backendV2**  
  Evoluzione dell'API Gateway, con miglioramenti nella politica di routing e nei controlli di sicurezza.

- **stage/auth-service-backend**  
  Modulo di autenticazione centralizzato, con logica di gestione dei token JWT dedicata al backend.

---

## Versioni finali (modularizzazione e scalabilità)

- **final/modular-marketplace**  
  Versione quasi completa dell'applicazione, pronta per essere eseguita sia in locale con Docker sia in ambiente cloud come Azure.

- **final/distribuited-session-caching**  
  Versione completa con introduzione di caching e aggiornamenti di sicurezza rispetto alla versione precedente, con l'obiettivo di rendere il sistema più sicuro e performante.

---

# 📌 Build from Scratch

È possibile andare a costruire l'applicazione in locale per le sue versioni finali **final/modular-marketplace** , **final/distribuited-session-caching** con Docker

### Prerequisiti
- **Docker** e **Docker Desktop**
- **Git**  

Verifica Docker:
```bash
docker --version
docker compose version
  ```

## Installazione

1. **Clonare il branch:**
   ```text
    # Clonare il branch 'final/modular-marketplace'
    git clone --branch final/modular-marketplace https://github.com/J-HON404/marketplace.git

    # Oppure il branch 'final/distribuited-session-caching'
    git clone --branch final/distribuited-session-caching https://github.com/J-HON404/marketplace.git
   ```
  
2. **Entrare nella directory del progetto:**
   ```bash
   cd marketplace
    ```

3. **Configurazione delle variabili d'ambiente:**
  Il progetto utilizza variabili d'ambiente definite in un file .env.
  Nel repository è presente un file di esempio .env.example.  
   Creare il file .env copiando il file di esempio:
  ```bash
     cp .env.example .env
  ```
  Aprire il file .env ed aggiungere i parametri mancanti

4. **Build dei container:**
   Costruzione dei container a partire dalle immagini
 ```bash
     docker compose build
  ```

5. **Avvio dell'applicazione:**
 Il progetto utilizza Docker Compose per avviare e orchestrare tutti i servizi.
 ```bash
     docker compose up -d

  ```
6.**Stato dei container:**
 ```bash
     docker compose ps 
  ```

7.**Arresto dell'applicazione:**
 ```bash
     docker compose down
  ```

8.**Verificare le immagini dell'applicazione:**
 ```bash
    docker compose images
  ```

9.**Visualizzare i log dell'applicazione:**
 ```bash
    docker compose logs -f
  ```

Per consentire un corretto funzionamento è consigliato ai fini dell'applicazione avere disponibili almeno 2 GB, considerando immagini, volumi e container

10.**Arrestare container:**

 ```bash
   docker compose stop
  ```

11.**Arrestare ed eliminare container:**

 ```bash
   docker compose down
  ```

12.**Arrestare, eliminare container e volumi:**

 ```bash
   docker compose down -v
  ```

---

## 🌐 Deploy su Microsoft Azure

L’applicazione è stata distribuita su **Azure Container Instances**. I container possono essere avviati dal portale di Azure.

**Politica pay-for-use:** per utilizzare l’applicazione su Azure, contattare l'autore(io).

Documentazione aggiuntiva su architettura e configurazioni è disponibile nei branch descritti.
