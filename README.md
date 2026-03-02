# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale.

---

## 🚀 Obiettivi Fase 3: Ottimizzare Containerizzazione

L'obiettivo è prendere l'applicazione presente nel branch 'stage/local-docker' , il cui obiettivo è trasformare l'applicazione in modo che sia portabile, isolata e facilmente scalabile  attraverso l'uso di **Docker** e **Docker Compose**. Il sistema è stato suddiviso in tre micro-servizi indipendenti:

1.  **Backend**: Spring Boot (Java 21)
2.  **Frontend**: Angular (Nginx come Web Server)
3.  **Database**: MariaDB

Rispetto alla versione dell'applicazione presente nel branch 'stage/local-docker', sono state apportate le segeunti modifiche

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
# Stage 2: Serve con Nginx
FROM nginx:alpine
COPY --from=build /app/dist/marketplace-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```
DOPO:
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
## Build & Run 
```dockerfile
docker build -t marketplace-frontend .
docker run -d \
  --name marketplace-frontend \
  -p 4200:80 \
  -e BACKEND_URL=https://mio-backend.azurewebsites.net \
  -e HOST_BACKEND=mio-backend.azurewebsites.net \
  marketplace-frontend
```

p 4200:80 → Mappa la porta 80 del container sulla porta 4200 dell’host.
Internamente, il container continua a usare la porta 80, ma Docker ridirige il traffico dalla 4200 dell’host.

## 🌐 Modifica Configurazione reverse proxy Nginx

Per permettere al frontend di comunicare con il backend senza incorrere in problemi di CORS, Nginx è configurato come Reverse Proxy:

PRIMA: 

```dockerfile
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
```

DOPO:

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

## 🌐 Introduzione Script Docker-entrypoint

```dockerfile
#!/bin/sh

envsubst '${BACKEND_URL} ${HOST_BACKEND}' \
    < /etc/nginx/conf.d/default.conf.template \
    > /etc/nginx/conf.d/default.conf
exec nginx -g 'daemon off;'
 ```
  ### 🛠️ Considerazioni

In precedenza, il Dockerfile Angular serviva l’app tramite Nginx usando un file di configurazione statico (nginx.conf). Questo significava che il collegamento al backend era hardcoded, e ogni volta che l’URL del backend cambiava, era necessario ricostruire l’immagine Docker. In questa versione è stato introdotto un template Nginx (nginx.conf.template),due nuove variabili di ambiente (BACKEND_URL e HOST_BACKEND) ed uno script di entrypoint (docker-entrypoint.sh), che sostituisce le variabili di ambiente nel template nginx al momento dell’avvio del container, generando il file di configurazione Nginx finale. 
Il file Nginx introdotto già nella precdente versione, serve a configurare il server web che distribuisce la tua applicazione Angular e gestisce il proxy verso il backend. Tutte le richieste che iniziano con /api/ vengono inviate al backend. Ora questo indirizzo backend è variabile e dinamico,sostituita all'avvio del container dallo script, così da non dover modificare o fare un nuovo deploy dell'immagine container.
Riassumendo i vantaggi ottenuti sono: 

Flessibilità: la stessa immagine Docker può puntare a backend diversi senza ricostruzione.

Portabilità: la configurazione è adattabile a più ambienti (staging, produzione, test).

Semplicità nel deploy: basta impostare le variabili di ambiente corrette su Azure,AWS o in qualsiasi altro ambiente.

Sicurezza e manutenzione: evita modifiche manuali al file statico e riduce errori di configurazione.

In questo modo, l’app Angular rimane indipendente dall’ambiente backend specifico e può essere distribuita più facilmente in ambienti diversi.  

