# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale.

> **Nota:** La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile. Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance. Rappresenta una base solida, sicura ed organizzata per progetti di media dimensione.

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
```
### Frontend (Angular)

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

## Considerazione Configurazioni

Il collegamento by link tra backend e databse si è rivelato limitante , poichè 
anche se due container riescono a comunicare perché vengono inseriti nella stessa rete bridge, il loro collegamento è però statico. In particolare nel container springboot viene iniettato automaticamente nelle variabili d'ambiente DB , i riferimenti a ques'utltimo e quindi anche il suo indirizzo ip , che il container spring boot non conosco direttamente, tuttavia se i riferimenti del db cambiassero, sarebbe necessario riavviare il container di springboot. Poiché non esiste un servizio di risoluzione DNS dinamica tra i due, se il container MariaDB dovesse essere riavviato e ottenere un nuovo IP, il backend perderebbe la connessione.
Inoltre i container backend e frontend non possono comunicare correttamete con il solo file Ngnix che funge da reverse proxy tra le parti.
Dal momento che ogni container è isolato ed ha una rete propria interna, per rendere la comunicazione psosibile con altri container è crare una rete docker condivisa e soprattuto sarebbe utile avere un orchestatore che permetta l'esecuzione di questi container creati simultaneamente, la soluzione a questi problemi si chiama 
Dokcer Compose

## Docker Compose 

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

 L'architettura si basa su una **Rete Bridge** personalizzata (`marketplace-network`) che abilita la risoluzione DNS automatica tra i servizi, permettendo ai container di comunicare tramite hostname anziché IP statici. L'**ordine di avvio** è coordinato tramite la direttiva `depends_on`, garantendo la sequenza logica Database → Backend → Frontend. 
Per gestire la **resilienza del Database**, dato che MariaDB richiede tempi di inizializzazione superiori rispetto al runtime Java, è stata configurata la proprietà `spring.datasource.hikari.initialization-fail-timeout=-1` in Spring Boot; questo permette al backend di restare in attesa della disponibilità del DB senza crashare durante lo startup. 
La **persistenza dei dati** è affidata a un volume Docker dedicato (`db_data`), che preserva lo stato del database anche in caso di rimozione dei container. L'**inizializzazione** dello schema avviene in modo trasparente tramite il montaggio del file `marketplace.sql` nella directory `/docker-entrypoint-initdb.d/`, eseguito esclusivamente alla prima creazione del volume. Infine, la **gestione delle variabili d'ambiente** è centralizzata nel file `.env` tramite la direttiva `env_file`, garantendo sicurezza, ordine e facilità di configurazione tra i diversi ambienti.
