# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.


# 🚀 Obiettivi Fase 7: Ottimizzazione e Caching

L'obiettivo principale di questa fase è ottimizzare l'architettura Azure (FASE 6) descritta nel `docs/azure-deployment ` del branch `final/modular-marketplace` , con l'introduzione di **Redis** come database in-memory per gestire la cache dei token JWT. Questo permette di ridurre drasticamente il carico computazionale del Gateway e abilita la revoca istantanea delle sessioni utente.

> *Nota:* I comandi inseriti sono solo a scopo descrittivo, non rappresentano tutte le configurazioni associate per ogni singolo componente.

## 🏗️ Architettura del Sistema

L'architettura si evolve da un modello semplice Frontend-Backend a una struttura modulare protetta e scalabile:

* **Frontend**: Esposto pubblicamente, funge da interfaccia utente e comunica esclusivamente con il Gateway.
* **API Gateway**: Esposto pubblicamente, gestisce il routing e la validazione rapida dei token interrogando Redis.
* **Auth Module**: Modulo interno, responsabile dell'autenticazione, della generazione dei JWT e della scrittura dei dati di sessione su Redis.
* **API Backend Module**: Modulo **interno**, espone gli endpoint per la gestione del marketplace (prodotti, ordini, shop).
* **Redis Cache**: Database interno che fornisce storage Key-Value ad alte prestazioni via protocollo TCP.

---

- **Azure Container Registry**
  
 Nel registro responsabile per le immagini docker inserite nell'ambiente Azure ed utilizzate dai container-app, è stata aggiunte la seguenti immagine:
  
Immagine container Redis-cache
```dockerfile
az acr login --name acresamecloud 
docker tag redis:alpine acresamecloud.azurecr.io/marketplace-redis:v1
docker push acresamecloud.azurecr.io/marketplace-redis:v1
```

- **Azure Key Vault**
  
  Sono stati aggiunti i seguenti secret per concedere l’accesso ai dati da parte dei container api-gateway-backend ed auth-module-backend

### 🔑 Configurazione Auth-module

Il container dell’**Auth Module** utilizza i secret per connettersi al container Redis e gestire l’autenticazione JWT:

```dockerfile
az keyvault secret set --vault-name kv-esame-marketplace --name REDIS_HOST --value "xxxxx"
az keyvault secret set --vault-name kv-esame-marketplace --name REDIS_PORT  --value "6379"
```

### 🔑 Configurazione Api-Gateway

Il container dell’**API Gateway** utilizza i secret con i riferimenti al modulo di autenticazione ed il modulo api-backend, ed i secret per verficare l’autenticazione JWT.  

```dockerfile
az keyvault secret set --vault-name kv-esame-marketplace --name REDIS_HOST --value "xxxxx"
az keyvault secret set --vault-name kv-esame-marketplace --name REDIS_PORT  --value "6379"
```

- **Azure Container Apps**

È stato creato all'interno dell **Azure Container Apps Environment** il seguente container.

  Container Redis
```dockerfile
az containerapp create ^
  --name marketplace-redis-cache ^
  --resource-group rg-esame-cloud ^
  --environment managedEnvironment-rgesamecloud-8803 ^
  --image redis:alpine ^
  --ingress internal ^
  --target-port 6379 ^
  --transport tcp ^
  --cpu 0.25 --memory 0.5Gi
```
----

**Mapping secret e variabili Key Vault con Container Apps**

```dockerfile
# Esempio di mapping secret
az containerapp secret set \
  --name api-gateway-backend-esame \
  --resource-group rg-esame-cloud \
  --secrets secret=keyvaultref:https://kv-esame-marketplace.vault.azure.net/secrets/NomeSecret
```
```dockerfile
# Esempio di mapping secret
az containerapp secret set \
  --name auth-module-backend-esame \
  --resource-group rg-esame-cloud \
  --secrets secret=keyvaultref:https://kv-esame-marketplace.vault.azure.net/secrets/NomeSecret
```

----

## 🌐 Analisi comunicazione tra container

* **Isolamento Totale:** I moduli `auth-module`, `api-backend` e il nuovo container `marketplace-redis-cache` sono ora completamente isolati. Non possiedono un IP pubblico e non sono raggiungibili dall'esterno.
* **Comunicazione via DNS Interno:** L'**API Gateway** comunica con i microservizi e con la cache Redis tramite DNS interno (es. `marketplace-redis-cache:6379`). Questo riduce drasticamente la latenza ed evita che il traffico sensibile transiti sulla rete pubblica.
* **Protocollo TCP:** La comunicazione tra i moduli backend e Redis avviene via **TCP diretto**, ottimizzando le performance rispetto alle classiche chiamate HTTP.

---
