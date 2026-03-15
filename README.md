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

# 🏗️ Legenda dei branch
Per comprendere a pieno lo sviluppo dell'applicazione nel tempo e comprendere a pieno l'evoluzione archietturale della sua forma finale, è consigliato prendere visione dei vari branch nel seguente ordine cronologico descritto.
I branch difatti descrivono e motivano l'evoluzione archietturale dell'applicazione, dalla sua versione base fino alla versione attuale finale, comunque vicina ad un applicazione cloud native orienta ai microservizi. Le fasi dell'applicazione sono rappresentate in modo cronologico dai seguenti branch

- **stage/rest-version:** è la prima versione dell'applicazione, nella sua versione base rest-api
- **stage/local-docker:** rappresenta il primo approccio di contenerizzazione dell'applicazione in locale con Docker
- **stage/local-dockerV2:** rappresenta l'evoluzione del primo approccio di contenirizzazione locale Docker, con l'introduzione di concetti e migliorie
  Successivamente si è sentita la necessità di evolvere l'applicazione, con l'intento di trasformarla in un approccio inizialmente Cloud Based e con il tempo raffinarla per ottenere un archiettura più vicina possibile ad una orientata ai microservizi. Le versioni finali difatti hanno lo scopo di rendere possibile un evoluzione agile dei moduli in un architettura orientata ai microservizi. I seguenti branch rappresentano le versioni più recenti dell'applicazione , i quali esplicitano in modo incrementale l'evoluzione archiettturale di essa
  - **stage/backend-api:** si concentra sul backend nella sua versione 1.0, con un analisi tecnica delle sue componenti, con l'intento di capire le aree di sviluppo
  - **stage/marketplace-frontend**: si concentra sul frontend dell'applicazione nella sua versione 1.0, con un analisi tecnica delle sue componenti, con l'intento di capire le aree di sviluppo
    In seguito l'applicazione è stata costruita sul cloud Microsoft Azure. I branch descritti rappresentano le migliorie strutturali dell'applicazione in questa fase
    - **stage/azure-app:** rappresenta il primo approccio di deploy con azure, nel seguente branch sono descritte le tecnologie utilizzate e le configurazioni effettuate per la migrazione       dell'applicazione
    - **stage/api-gateway-backend:** definisce la prima versione di un api-gateway con il file di disaccoppiare la dipendenza tra container frontend e backend, favorendo così un approccio       maggiormente distribuito e scalabile.
    - **stage/api-gateway-backendV2:** rappresenta l'evoluzione dell' api-gateway sviluppato, con l'evoluzione della politica di routing e dei controlli di sicurezza
    - **stage/auth-service-backend:** definisce un modulo di autenticazione, con l'intento di centralizzare la logica di autenticazione del token jwt in un modulo backend dedicato
    - **final/modular-maketplace:** rappresenta la versione quasi completa dell'applicazione, pronta ad essere costruita in locale con l'ausilio di Docker e in un ambiente cloud come Azure
    - **final/distribuited-session-caching:** introduce concetti di caching ed aggiornamenti di sicurezza rispetto alla versione precedente. Con lo scopo di raffinare i processi interni dell'applicazione, rendendo il sistema più sicuro e performante

---

# 📌 Build from Scratch

### Prerequisiti
- **Docker** e **Docker Desktop**
- **Git**  

Verifica Docker:
```bash
docker --version
docker compose version
  ```

## Installazione

1. **Clonare il repository:**
   ```bash
   git clone [https://github.com/J-HON404/marketplace.git](https://github.com/J-HON404/marketplace.git)
  ```

2. **Entrare nella directory del progetto:**
   ```bash
   cd marketplace
  ```

3. **Configurazione delle variabili d'ambiente:**
  Il progetto utilizza variabili d'ambiente definite in un file .env.
  Nel repository è presente un file di esempio .env.example
   Creare il file .env copiando il file di esempio:
  ```bash
     cp .env.example .env
  ```
  Aprire il file .env ed aggiungere i parametri mancanti

4. **Build dei container:**
 Il progetto utilizza Docker Compose per costruire e orchestrare tutti i servizi.
 Per costruire le immagini Docker
 ```bash
     docker compose build
  ```

5. **Avvio dell'applicazione:**
 Il progetto utilizza Docker Compose per costruire e orchestrare tutti i servizi.
 Per costruire le immagini Docker
 ```bash
     docker compose up

  ```
6.**Stato dei container:**
 ```bash
     docker ps
  ```

7.**Arresto dell'applicazione:**
 ```bash
     docker compose down
  ```

8..**Verificare spazio utilizzato:**
 ```bash
     docker system df
  ```

Per consentire un corretto funzionamento è consigliato ai fini dell'applicazione avere disponibili almeno 2.6 GB, considerando immagini, volumi e container

Per rimuovere immagini,container e volumi

 ```bash
   docker compose down -v
  ```

---

## 🌐 Deploy su Microsoft Azure

L’applicazione è stata distribuita su **Azure Container Instances**. I container possono essere avviati dal portale di Azure.

**Politica pay-for-use:** per l’accesso, contattarmi.

Documentazione aggiuntiva su architettura e configurazioni disponibile nei branch del repository.
