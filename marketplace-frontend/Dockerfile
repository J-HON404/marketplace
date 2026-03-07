# --- STAGE 1: Build ---
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npx ng build --configuration production

# --- STAGE 2: Serve ---
FROM nginx:alpine

# 1. Copia i file compilati di Angular
COPY --from=build /app/dist/marketplace-frontend/browser /usr/share/nginx/html

COPY nginx.conf.template /etc/nginx/conf.d/default.conf.template

# 2. CANCELLA la config di default di Nginx per evitare conflitti
RUN rm /etc/nginx/conf.d/default.conf

# 3. Prepara lo script
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
EXPOSE 80
