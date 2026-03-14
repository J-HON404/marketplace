# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.


# 🚀 Obiettivi Fase 6: Ottimizzazione e Caching

L'obiettivo principale di questa fase è ottimizzare l'architettura Docker (FASE 5) descritta nel `docs/docker-deployment ` del branch `final/modular-marketplace` , con l'introduzione di **Redis** come database in-memory per gestire la cache dei token JWT. Questo permette di ridurre drasticamente il carico computazionale del Gateway e abilita la revoca istantanea delle sessioni utente.

## 📄 Modifica Docker Compose

PRIMA : 
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
      - ./02-data.sql:/docker-entrypoint-initdb.d/02-data.sql
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
DOPO:
```dockerfile
version: '3.8'

services:

  marketplace-db:
    image: mariadb:12
    container_name: marketplace-db
    restart: always
    env_file: .env
    ports:
      - "3307:3306"
    volumes:
      - db_data:/var/lib/mysql
      - ./docker-init:/docker-entrypoint-initdb.d
    networks:
      - marketplace-network

  marketplace-auth:
    build:
      context: .
      dockerfile: marketplace-auth-module/Dockerfile
    container_name: marketplace-auth
    restart: on-failure
    env_file: .env
    ports:
      - "${AUTH_PORT}:${AUTH_PORT}"
    depends_on:
      - marketplace-db
      - marketplace-redis
    networks:
      - marketplace-network

  marketplace-backend:
    build:
      context: .
      dockerfile: marketplace-backend-api/Dockerfile
    container_name: marketplace-backend
    restart: on-failure
    env_file: .env
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
    env_file: .env
    ports:
      - "${GATEWAY_PORT}:${GATEWAY_PORT}"
    depends_on:
      - marketplace-auth
      - marketplace-backend
      - marketplace-redis
    networks:
      - marketplace-network

  marketplace-frontend:
    build:
      context: .
      dockerfile: marketplace-frontend/Dockerfile
    container_name: marketplace-frontend
    env_file: .env
    restart: on-failure
    ports:
      - "4207:80"
    depends_on:
      - marketplace-gateway
    networks:
      - marketplace-network

  marketplace-redis:
    image: redis:alpine
    container_name: marketplace-redis
    restart: always
    ports:
      - "${REDIS_PORT}:${REDIS_PORT}"
    volumes:
      - redis_data:/data
    networks:
      - marketplace-network

networks:
  marketplace-network:
    driver: bridge

volumes:
  db_data:
    driver: local
  redis_data:
    driver: local

```
---

 **Dipendenze aggiornate (`depends_on`):**  
   - Il gateway adesso dipende anche dal container Redis.  
   - Il modulo auth adesso dipende anche dal container Redis.
     
**Aggiunto redis_data volume:**
  - Il container Redis Cache usufruisce del volume `redis_data` per il salvataggio dei token jwt,
    garantendo la persistenza dei dati e prevedendo la perdita di informazioni in caso di riavvio o aggiornamento del container.
--- 


