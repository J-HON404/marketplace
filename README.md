# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale.

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
* **Notifiche:** Creazione di avvisi, promozioni e alert sulle scorte.

### 🛍 Area Acquirente (CUSTOMER)
* **Shopping:** Navigazione tra negozi e prodotti disponibili.
* **Carrello:** Gestione dinamica delle quantità e processo di checkout.
* **Tracking:** Storico ordini e conferma di ricezione (abilitata post-consegna).
* **Alert:** Ricezione di annunci e promozioni dedicate.

---

## 🧱 Architettura Tecnica

### **Backend**
* **Java 21** & **Spring Boot 3.5.8**
* **Persistenza:** Spring Data JPA con **MySQL**.
* **Pattern:** DTO, Global Exception Handling, Server-side Validation.

### **Frontend**
* **Angular 20.3** & **TypeScript**.
* **Core:** Standalone Components e Angular Routing.
* **Networking:** HttpInterceptor per l’iniezione automatica del token JWT.
* **Reattività:** Gestione asincrona tramite **RxJS** (Observable).

---

## 🗄 Struttura del Database



| Entità | Descrizione |
| :--- | :--- |
| **Profile** | Anagrafica utenti, credenziali e ruoli. |
| **Shop** | Dettagli del negozio associato a un venditore. |
| **Product** | Catalogo articoli, prezzi e inventario. |
| **Cart** | Stato del carrello corrente dell'utente. |
| **Order** | Dettagli transazione, tracking e stato consegna. |
| **ProductNotice** | Log delle notifiche e promozioni attive. |

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
### **Frontend**
```text
└── src/app
    ├── common        # Componenti condivisi
    ├── core          # Logica di sistema: Interceptor JWT e configurazioni
    ├── models        # Definizioni di interfacce
    ├── services      # Servizi per chiamate API (HttpClient) e gestione stato
    └── views         # Componenti di pagina (Home, Dashboard, Shop, Checkout)
```
