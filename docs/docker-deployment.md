# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale.

---

## 🚀 Obiettivi Fase 5: Ottimizzazione ed Introduzione moduli nella Containerizzazione 

L'obiettivo è prendere l'applicazione presente nel **branch stage/local-dockerV2** e strutturare l'applicazione in modo che sia portabile, isolata e facilmente scalabile  attraverso l'uso di **Docker** e **Docker Compose**. Il sistema è stato suddiviso in tre micro-servizi indipendenti:

1.  **Backend**: Spring Boot (Java 21)
2.  **Frontend**: Angular (Nginx come Web Server)
3.  **Database**: MariaDB

Rispetto alla versione dell'applicazione presente nel branch 'stage/local-dockerV2', sono state apportate le seguenti modifiche

## 📄 Modifica Dockerfile Frontend

PRIMA : 
### FRONTEND (Angular)
```dockerfile
# Stage 1: Build Angular
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npx ng build --configuration production
# Serve con Nginx
FROM nginx:alpine
COPY --from=build /app/dist/marketplace-frontend/browser /usr/share/nginx/html
COPY nginx.conf.template /etc/nginx/conf.d/default.conf.template
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh
EXPOSE 80
CMD ["/docker-entrypoint.sh"]
```
DOPO:
### FRONTEND (Angular)
```dockerfile
# --- STAGE 1: Build ---
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npx ng build --configuration production
# --- STAGE 2: Serve ---
FROM nginx:alpine
COPY --from=build /app/dist/marketplace-frontend/browser /usr/share/nginx/html
COPY nginx.conf.template /etc/nginx/conf.d/default.conf.template
RUN rm /etc/nginx/conf.d/default.conf
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]
EXPOSE 80
```
Il container ora è più **controllato**:  
- La configurazione **default di Nginx viene rimossa**, evitando conflitti in fase di build.
- Lo script di avvio è definito con `ENTRYPOINT`, quindi viene **sempre eseguito all’avvio del container**.

## Build & Run 
```dockerfile
docker build -t marketplace-frontend .
docker run -d \
  --name marketplace-frontend \
  -p 4200:80 \
  -e GATEWAY_URL=https://mio-backend.azurewebsites.net \
  -e HOST_GATEWAY=mio-backend.azurewebsites.net \
  marketplace-frontend
```

## 📄 Modifica Dockerfile Backend

PRIMA : 
### BACKEND (Spring boot)
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
DOPO:
### BACKEND (Spring boot)
```dockerfile
# ---------- Stage 1: Build ----------
ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jdk AS build
WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
COPY settings.gradle .
COPY build.gradle .
COPY marketplace-backend-api ./marketplace-backend-api
RUN chmod +x gradlew
RUN sed -i 's/\r$//' gradlew
# Build del modulo
RUN ./gradlew :marketplace-backend-api:build -x test
# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:${JAVA_VERSION}-jre
WORKDIR /app
COPY --from=build /app/marketplace-backend-api/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["sh","-c","java -Dserver.port=${BACKEND_PORT:-8082} -jar app.jar"]
``` 
- **Build mirata:** ora viene compilato solo il modulo `marketplace-backend-api` invece di tutta l’app, rendendo la build più veloce e sicura.  
- **Pulizia dei file:** `gradlew` viene reso eseguibile e corretti eventuali problemi
- **Porta modificata:** il container ora espone `8082` invece di `8080`, e la variabile d’ambiente è `BACKEND_PORT`.  
- **COPY più specifico:** copiati solo i file e le cartelle necessarie per la build del modulo backend.

## Build & Run 
```dockerfile
docker build -t marketplace-backend .
docker run -d \
  --name marketplace-backend \
  -p 8082:8082 \
  -e BACKEND_PORT=8082 \
  marketplace-backend
```

## 📄 Introduzione Dockerfile Auth-Module

```dockerfile
# ---------- Stage 1: Build ----------
ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jdk AS build
WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
COPY settings.gradle .
COPY build.gradle .
COPY marketplace-auth-module ./marketplace-auth-module
RUN chmod +x gradlew
RUN sed -i 's/\r$//' gradlew
RUN ./gradlew :marketplace-auth-module:build -x test
# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:${JAVA_VERSION}-jre
WORKDIR /app
COPY --from=build /app/marketplace-auth-module/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8081} -jar app.jar"]
```

## Build & Run 
```dockerfile
docker build -t marketplace-auth-module .
docker run -d \
  --name marketplace-auth-module \
  -p 8081:8081 \
  -e PORT=8081 \
  marketplace-auth-module
```

## 📄 Introduzione Dockerfile Api-Gateway

```dockerfile
# ---------- Stage 1: Build ----------
ARG JAVA_VERSION=21
FROM eclipse-temurin:${JAVA_VERSION}-jdk AS build
WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
COPY settings.gradle .
COPY build.gradle .
COPY marketplace-gateway-api ./marketplace-gateway-api
RUN chmod +x gradlew
RUN sed -i 's/\r$//' gradlew
RUN ./gradlew :marketplace-gateway-api:build -x test
# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:${JAVA_VERSION}-jre
WORKDIR /app
COPY --from=build /app/marketplace-gateway-api/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT:-8080} -jar app.jar"]
```

## Build & Run 
```dockerfile
docker build -t marketplace-gateway-api .
docker run -d \
  --name marketplace-gateway-api \
  -p 8080:8080 \
  -e PORT=8080 \
  marketplace-gateway-api
```

## 📄 Modifica Docker Compose

PRIMA : 
### BACKEND (Spring boot)
```dockerfile
services:
  marketplace-db:
    image: mariadb:12
    container_name: marketplace-db
    env_file:
      - .env
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql
      - ./marketplace-schema.sql:/docker-entrypoint-initdb.d/marketplace-schema.sql
    networks:
      - marketplace-network
  marketplace-backend:
    build:
      context: ./marketplace-backend
      dockerfile: Dockerfile.backend
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
    build:
      context: ./marketplace-frontend
      dockerfile: Dockerfile.frontend
    container_name: marketplace-frontend
    env_file:
      - .env
    ports:
      - "4200:80"
    depends_on:
      - marketplace-backend
    networks:
      - marketplace-network
networks:
  marketplace-network:
    driver: bridge
volumes:
  db_data:
```
DOPO:
```dockerfile
version: '3.8'
services:
  marketplace-db:
    image: mariadb:12
    container_name: marketplace-db
    restart: always
    env_file: .env.example
    ports:
      - "3307:3306"
    volumes:
      - db_data:/var/lib/mysql
      - ./marketplace-schema.sql:/docker-entrypoint-initdb.d/marketplace-schema.sql
    networks:
      - marketplace-network
  marketplace-auth:
    build:
      context: .
      dockerfile: marketplace-auth-module/Dockerfile
    container_name: marketplace-auth
    restart: on-failure
    env_file: .env.example
    ports:
      - "${AUTH_PORT}:${AUTH_PORT}"
    depends_on:
      - marketplace-db
    networks:
      - marketplace-network
  marketplace-backend:
    build:
      context: .
      dockerfile: marketplace-backend-api/Dockerfile
    container_name: marketplace-backend-v2
    restart: on-failure
    env_file: .env.example
    ports:
      - "${BACKEND_PORT}:${BACKEND_PORT}"
    depends_on:
      - marketplace-db
    networks:
      - marketplace-network
  marketplace-gateway:
    build:
      context: .
      dockerfile: marketplace-gateway-api/Dockerfile
    container_name: marketplace-gateway
    restart: on-failure
    env_file: .env.example
    ports:
      - "${GATEWAY_PORT}:${GATEWAY_PORT}"
    depends_on:
      - marketplace-auth
      - marketplace-backend
    networks:
      - marketplace-network
  marketplace-frontend:
    build:
      context: ./marketplace-frontend
      dockerfile: Dockerfile
    container_name: marketplace-frontend-v2
    env_file: .env.example
    restart: on-failure
    ports:
      - "4207:80"
    depends_on:
      - marketplace-gateway
    networks:
      - marketplace-network
networks:
  marketplace-network:
    driver: bridge
volumes:
  db_data:
    driver: local
```

Oltre all'introduzione dei nuovi container e delle modifiche legate alle porte, sono stati aggiungi:

1. **Strategia di restart:**  
   - I container principali (`db`, `auth`, `backend`, `gateway`, `frontend`) ora hanno direttive `restart`:
     - `always` per il database  
     - `on-failure` per gli altri servizi  
   Questo rende i container più **resilienti**.

2. **Dipendenze aggiornate (`depends_on`):**  
   - Il gateway dipende sia da Auth che da Backend.  
   - Il frontend dipende dal Gateway invece che direttamente dal Backend.


## 🌐 Introduzione Rotte Api-Gateway

```dockerfile
# AUTH MODULE
spring.cloud.gateway.routes[0].id=auth-service
spring.cloud.gateway.routes[0].uri=${AUTH_SERVICE_URL}
spring.cloud.gateway.routes[0].predicates[0]=Path=/api/auth/**

# BACKEND API
spring.cloud.gateway.routes[1].id=api-service
spring.cloud.gateway.routes[1].uri=${API_SERVICE_URL}
spring.cloud.gateway.routes[1].predicates[0]=Path=/**
```
Le richieste destinate ad /api/auth/ecc.. vengono inoltrate all' Auth-module e non richiedono autenticazione, le altre rotte vengono gestite dal modulo backend-api e richiedono autenticazione

## 🌐 Modifica Configurazione reverse proxy Nginx

Per permettere al frontend di comunicare con il backend senza incorrere in problemi di CORS, Nginx è configurato come Reverse Proxy:

PRIMA: 

```dockerfile
server {
    listen 80;

    root /usr/share/nginx/html;
    index index.html;

    # Angular SPA fallback
    location / {
        try_files $uri /index.html;
    }

    # Proxy verso backend Azure
    location /api/ {
        # URL del backend (variabile d'ambiente)
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

DOPO:

```dockerfile
server {
    listen 80;

    root /usr/share/nginx/html;
    index index.html;

    # Angular SPA fallback: gestisce il routing lato client
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Proxy verso backend Gateway
    location /api/ {
      
        proxy_pass ${GATEWAY_URL};

        proxy_set_header Host              ${HOST_GATEWAY};
        proxy_set_header X-Real-IP         $remote_addr;
        proxy_set_header X-Forwarded-For   $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # Configurazioni specifiche per connessioni HTTPS
        proxy_ssl_server_name on;
        proxy_http_version    1.1;
        proxy_set_header      Connection "";

        # Timeout per evitare interruzioni su chiamate lunghe
        proxy_connect_timeout 90;
        proxy_send_timeout    90;
        proxy_read_timeout    90;
    }
}

 ```

- **Routing Angular funzionante:** se l’utente apre direttamente una pagina dell’app (es. `/profilo`), Nginx mostra comunque `index.html` così Angular può gestire la pagina correttamente, senza resituire 404.
- **Backend tramite Gateway:** le chiamate `/api/` passano ora attraverso un gateway (`GATEWAY_URL`) invece di collegarsi direttamente al     backend.  
- **Connessioni più stabili:** Nginx non mantiene connessioni aperte inutilmente verso il backend, evitando errori durante le chiamate API.  
- **Timeout configurati:** `connect`, `send` e `read` timeout aumentati a 90s per gestire richieste lunghe senza interruzioni.

## 🌐 Modifica Script Docker-entrypoint

PRIMA: 

```dockerfile
#!/bin/sh

envsubst '${BACKEND_URL} ${HOST_BACKEND}' \
    < /etc/nginx/conf.d/default.conf.template \
    > /etc/nginx/conf.d/default.conf
exec nginx -g 'daemon off;'
 ```

DOPO: 

```dockerfile
#!/bin/sh

envsubst '${GATEWAY_URL} ${HOST_GATEWAY}' \
    < /etc/nginx/conf.d/default.conf.template \
    > /etc/nginx/conf.d/default.conf
exec nginx -g 'daemon off;'
 ```
### 🛠️ Considerazioni

In precedenza, il Dockerfile Angular serviva l’app tramite Nginx usava riferimento diretto del modulo backend, adesso la richiesta viene inoltrata all Api-Gateway.  

Lo script sostituisce le variabili di ambiente nel template Nginx al momento dell’avvio del container, generando il **file di configurazione finale**.  

**Vantaggi principali ottenuti:**  

Flessibilità: la stessa immagine Docker può puntare a backend o gateway diversi senza ricostruzione.

Portabilità: la configurazione è adattabile a più ambienti (staging, produzione, test).

Semplicità nel deploy: basta impostare le variabili di ambiente corrette su Azure,AWS o in qualsiasi altro ambiente.

Sicurezza e manutenzione: evita modifiche manuali al file statico e riduce errori di configurazione.

In questo modo, l’app Angular rimane indipendente dall’ambiente backend specifico e può essere distribuita più facilmente in ambienti diversi.  


