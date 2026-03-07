# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.

---

## 🚀 Panoramica Frontend

L'obiettivo è fornire un interfaccia completa con gestione separata dei ruoli e che permetta dal caricamento del prodotto , alla vendita fino alla conferma di ricezione dell’ordine.

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

### **Frontend**
* **Angular 20.3** & **TypeScript**.
* **Core:** Standalone Components e Angular Routing.
* **Networking:** HttpInterceptor per l’iniezione automatica del token JWT.
* **Reattività:** Gestione asincrona tramite **RxJS** (Observable).

---

## 📁 Organizzazione delle Cartelle

### **Frontend**
```text
└── src/app
    ├── common        # Componenti condivisi
    ├── core          # Logica di sistema: Interceptor JWT e configurazioni
    ├── models        # Definizioni di interfacce
    ├── services      # Servizi per chiamate API (HttpClient) e gestione stato
    └── views         # Componenti di pagina (Home, Dashboard, Shop, Checkout)
```

## 📄 Analisi web server Nginx

Il frontend angular dell'applicazione viene servito da un server web Nginx che nell'attuale configurazione funge da reverse proxy verso il backend.
Nginx è fondamentale per gestire correttamente le richieste HTTP che arrivano dall'host e instradarle correttamente ai servizi dell'applicazione.
Con la sua configurazione reverse proxy Nginx inoltra tutte le richieste destinate alle API (`/api/`) verso il servizio backend, fungendo da
punto di ingresso unico per il sistema e separando la gestione del frontend da quella delle logiche applicative.

**Flusso delle richieste**

1. Il client accede all'applicazione tramite browser.
2. Nginx serve i file statici del frontend Angular.
3. Le richieste verso `/api/` vengono intercettate da Nginx.
4. Nginx inoltra tali richieste al backend tramite reverse proxy.
5. Il backend Spring Boot elabora la richiesta e restituisce la risposta.

---

```dockerfile
server {
    listen 80;

    root /usr/share/nginx/html;
    index index.html;

    # Angular SPA fallback
    location / {
        try_files $uri /index.html;
    }

    # Proxy verso backend 
    location /api/ {
        # URL del backend (https)
        proxy_pass ${BACKEND_URL};

        # Header utili per il backend
        proxy_set_header Host ${HOST_BACKEND};
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # HTTPS specific
        proxy_ssl_server_name on;
        proxy_http_version 1.1;
    }
}
```
### Considerazioni
Il codice mostra come le informazioni legate al backend(`BACKEND_URL` e `HOST_BACKEND`) vengano gestite tramitevariabili di ambiente.
Questo approccio evita di hardcodare gli indirizzi dei serviziall'interno della configurazione e consente di adattare facilmenteil comportamento dell'applicazione ai diversi ambienti di esecuzione (sviluppo, staging, produzione).
In questo modo è possibile modificare la destinazione delle richiesteAPI senza dover cambiare la configurazione del server o ricompilare l'applicazione, migliorando la flessibilità e la portabilità del sistema.

