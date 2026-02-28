# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale.

---

## 🚀 Obiettivi Fase 2: Containerizzazione

L'obiettivo è rendere l'applicazione portabile, isolata e facilmente scalabile attraverso l'uso di **Docker** e **Docker Compose**. Il sistema è stato suddiviso in tre micro-servizi indipendenti:

1.  **Backend**: Spring Boot (Java 21)
2.  **Frontend**: Angular (Nginx come Web Server)
3.  **Database**: MariaDB

### 🛠 Multi-Stage Build
Per il Backend e il Frontend è stata adottata la tecnica della **Multi-stage build**. Questo approccio separa l'ambiente di compilazione dall'ambiente di esecuzione:
* **Stage 1 (Build):** Scaricamento dipendenze e compilazione dei sorgenti.
* **Stage 2 (Runtime):** Creazione di un layer leggero contenente solo l'artefatto finale.
* **Vantaggi:** Immagini finali estremamente ridotte e deployment più rapido.

---

## 📄 Dockerfile dei Servizi

### Backend (Spring Boot)

```dockerfile
# Stage 1: Build
ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jdk AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build -x test
# Stage 2: Runtime
FROM eclipse-temurin:${JAVA_VERSION}-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
### Frontend (Angular)
```
### FRONTEND (Angular)
```dockerfile
# Stage 1: Build Angular
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npx ng build --configuration production
# Stage 2: Serve con Nginx
FROM nginx:alpine
COPY --from=build /app/dist/marketplace-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```
## 🌐 Configurazione reverse proxy Nginx

Per permettere al frontend di comunicare con il backend senza incorrere in problemi di CORS, Nginx è configurato come Reverse Proxy:

Nginx
server {
    listen 80;
    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri /index.html;
    }

    location /api/ {
        proxy_pass http://marketplace-backend:8080;
    }
}


## 🌐 Network backend e database

Inizialmente il backend comunicava con il contaiener db tramite l' Utilizzo di --link marketplace-db:marketplace-db. Questa soluzione si è rivelata limitante a causa della gestione statica degli IP e della mancanza di risoluzione DNS dinamica.

## ⚙️ Analisi delle Criticità e Passaggio a Docker Compose

Il collegamento iniziale tra backend e database tramite il flag `--link` si è rivelato limitante a causa della sua natura **statica**:

* **Dipendenza dall'IP:** Nel container Spring Boot vengono iniettati automaticamente i riferimenti e l'indirizzo IP del database. Tuttavia, poiché non esiste un servizio di risoluzione DNS dinamica tra i due, se il container MariaDB dovesse essere riavviato ottenendo un nuovo IP, il backend perderebbe la connessione, rendendo necessario un riavvio manuale del container backend.
* **Isolamento di Rete:** Ogni container opera in una propria rete interna isolata. Di conseguenza, i container backend e frontend non possono comunicare correttamente utilizzando esclusivamente il file Nginx come reverse proxy, poiché manca un'infrastruttura di rete condivisa.

### La Soluzione: Docker Compose
Per superare l'isolamento dei container e rendere la comunicazione possibile, la strategia corretta è la creazione di una **rete Docker condivisa**. L'utilizzo di un orchestratore come **Docker Compose** risolve queste problematiche permettendo di:

1.  **Esecuzione Simultanea:** Gestisce l'avvio coordinato di tutti i container creati nello stesso momento.
2.  **Risoluzione DNS Dinamica:** Abilita la comunicazione tra servizi tramite il loro nome (es. `marketplace-db`) anziché tramite indirizzi IP instabili.
3.  **Rete Comune:** Inserisce automaticamente tutti i servizi in un'unica rete bridge, eliminando la necessità di collegamenti statici e manuali.

## Docker Compose 

```dockerfile
networks:
  marketplace-network:
    driver: bridge

services:
  marketplace-db:
    image: mariadb:12
    container_name: marketplace-db
    env_file:
      - .env
    ports:
      - "3306:3306"
    networks:
      - marketplace-network
    volumes:
      - db_data:/var/lib/mysql
      - ./marketplace.sql:/docker-entrypoint-initdb.d/marketplace.sql

  marketplace-backend:
    build: ./marketplace-backend
    container_name: marketplace-backend
    env_file:
      - .env
    ports:
      - "${PORT}:${PORT}"
    depends_on:
      - marketplace-db
    networks:
      - marketplace-network

  marketplace-frontend:
    build: ./marketplace-frontend
    container_name: marketplace-frontend
    env_file:
      - .env
    depends_on:
      - marketplace-backend
    ports:
      - "4200:80"
    networks:
      - marketplace-network

volumes:
  db_data:

  ```

  ### 🛠️ Considerazioni

L'infrastruttura dell'applicazione è stata ottimizzata per garantire stabilità e comunicazione fluida tra i servizi:

* **Rete Bridge Personalizzata (`marketplace-network`):** Abilita la risoluzione DNS automatica tra i servizi. Questo permette ai container di comunicare utilizzando gli **hostname** (es. `marketplace-db`) anziché indirizzi IP statici, che risulterebbero instabili in caso di riavvio.

* **Coordinamento dell'Ordine di Avvio:** Grazie alla direttiva `depends_on`, viene garantita la corretta sequenza logica di accensione dei servizi: 
    **Database** ->  **Backend** ->  **Frontend**

* **Resilienza del Database (Spring Boot):** Dato che MariaDB richiede tempi di inizializzazione superiori rispetto al runtime Java, è stata configurata la proprietà:
    `spring.datasource.hikari.initialization-fail-timeout=-1`
    
    Questa impostazione permette al backend di rimanere in attesa della disponibilità effettiva del database, evitando crash improvvisi durante la fase di startup dell'intero ecosistema.

  Nella cartella docker sono presenti tutti i file di configurazione 


