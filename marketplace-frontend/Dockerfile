# --- STAGE 1: Build ---
FROM node:20 AS build
WORKDIR /app
COPY marketplace-frontend/package*.json ./
RUN npm install --legacy-peer-deps
COPY marketplace-frontend/. .
RUN npx ng build --configuration production

# --- STAGE 2: Serve ---
FROM nginx:alpine

# 1. Copia i file compilati di Angular
COPY --from=build /app/dist/marketplace-frontend/browser /usr/share/nginx/html

# 2. Copia la config nginx
COPY marketplace-frontend/nginx.conf.template /etc/nginx/conf.d/default.conf.template

# 3. CANCELLA la config di default di Nginx per evitare conflitti
RUN rm /etc/nginx/conf.d/default.conf

# 4. Prepara lo script di entrypoint
COPY marketplace-frontend/docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
EXPOSE 80
