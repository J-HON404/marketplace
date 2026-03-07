# --- BUILD STAGE ---
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npx ng build --configuration production

# --- SERVE STAGE ---
FROM nginx:alpine

# 1. Copia i file compilati (Verifica bene il percorso 'browser'!)
COPY --from=build /app/dist/marketplace-frontend/browser /usr/share/nginx/html

# 2. Copia i file di configurazione
COPY nginx.conf.template /etc/nginx/conf.d/default.conf.template
COPY docker-entrypoint.sh /docker-entrypoint.sh

RUN chmod +x /docker-entrypoint.sh

EXPOSE 80

# 4. Usa ENTRYPOINT per far girare lo script
ENTRYPOINT ["/bin/sh", "/docker-entrypoint.sh"]
