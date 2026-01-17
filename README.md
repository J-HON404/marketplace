🛒 Marketplace Web Application

Progetto marketplace full-stack basato su Spring Boot e Angular.
L'applicazione gestisce un ecosistema multi-venditore in cui gli utenti possono registrarsi come SELLER o CUSTOMER per gestire shop, prodotti e ordini in tempo reale.

🚀 Panoramica del Progetto

L'obiettivo è fornire una piattaforma completa con gestione separata dei ruoli e una logica di business che copre l’intero ciclo di vendita, dal caricamento del prodotto fino alla conferma di ricezione dell’ordine.

Architettura: REST a livelli (Controller, Service, Repository)

Frontend: Single Page Application (SPA) moderna e reattiva

Sicurezza: Autenticazione stateless basata su JWT con Spring Security

⚙️ Funzionalità Principali
🔐 Autenticazione e Sicurezza

Il sistema gestisce la registrazione e il login distinguendo i ruoli SELLER e CUSTOMER.
La sicurezza è garantita tramite:

Generazione, validazione e gestione della sessione con JWT

Controllo degli accessi basato su ruoli

Protezione delle API tramite Spring Security

🏪 Area Venditore (SELLER)

Shop Management

Gestione completa del proprio negozio

Gestione Catalogo

Creazione e gestione dei prodotti

Gestione stock

Date di disponibilità

Logistica

Visualizzazione degli ordini ricevuti

Inserimento Tracking ID

Inserimento data di consegna stimata

Notifiche Prodotto

Creazione di avvisi, promozioni e notifiche sullo stato delle scorte

🛍 Area Acquirente (CUSTOMER)

Shopping

Navigazione tra negozi e prodotti disponibili

Carrello

Gestione dinamica delle quantità

Checkout dell’ordine

Tracking

Visualizzazione dello storico ordini

Conferma di ricezione (disponibile solo dopo la data stimata)

Alert

Visualizzazione avvisi, annunci e promozioni associati ai prodotti

🧱 Architettura Tecnica
Backend

Java 21

Spring Boot 3.5.8

Persistenza: Spring Data JPA con MySQL

Pattern architetturali:

Utilizzo di DTO per lo scambio dati

Gestione centralizzata delle eccezioni

Validazione lato server

Frontend

Angular 20.3

TypeScript

Standalone Components e Angular Routing

Networking: HttpInterceptor per l’iniezione automatica del token JWT

Reattività: Gestione asincrona dei flussi tramite RxJS (Observable)

🗄 Struttura del Database

Il database relazionale organizza i dati attorno alle seguenti entità principali:

Profile

Shop

Product

Cart

Order

ProductNotice

📁 Organizzazione del Progetto
Backend
src/main/java/com/unicam/cs/progettoweb/marketplace
├── controller      # Endpoint REST API
├── dto             # Oggetti di trasferimento dati
├── exception       # Handler per errori personalizzati
├── model           # Entity JPA e modelli database
├── repository      # Interfacce di accesso ai dati
├── security        # Configurazione JWT e filtri di sicurezza
└── service         # Logica di business e servizi applicativi

Frontend
src/app
├── common          # Componenti condivisi (Navbar, Footer, ecc.)
├── core            # Interceptor, Guard e servizi di sistema
├── models          # Interfacce e tipi TypeScript
├── services        # Servizi per le chiamate API di dominio
└── views           # Componenti pagina e logica di visualizzazione

🚀 Setup e Installazione
Requisiti

Java 21 & Gradle

Node.js ≥ 20

Angular CLI ≥ 20.3

MySQL
