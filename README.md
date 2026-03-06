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

