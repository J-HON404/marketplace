# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.


# 🚀 Obiettivi Fase 6 : Aggiornamento Infrastuttura Microsoft Azure

L'obiettivo è aggiornare l'infrastruttura definita nel **branch stage/azure-app**, andando ad aggiungere i moduli introdotti nell'applicazione

## Architettura del sistema

In particolare al momento l'architettura è composta solo da container backend e container frontend. Di conseguenza è necessario introdurre i moduli: Api-Gateway e Auth-module affinché possano interagire correttamente con i container già definiti precedentemente.

> *Nota:* I comandi inseriti sono solo a scopo descrittivo, non rappresentano tutte le configurazioni associate per ogni singolo componente.

- **Azure Container Registry**
  
 Nel registro responsabile per le immagini docker inserite nell'ambiente Azure ed utilizzate dai container-app, sono state aggiunte le seguenti immagini:
  
Immagine container Api-Gateway
```dockerfile
az acr login --name acresamecloud 
docker tag api-backend-gateway-marketplace:v1 acresamecloud.azurecr.io/api-backend-gateway-marketplace:v1
docker push acresamecloud.azurecr.io/api-backend-gateway-marketplace:v1
```
Immagine container Auth-module
```dockerfile
az acr login --name acresamecloud 
docker tag auth-module-marketplace-backend:v1 acresamecloud.azurecr.io/auth-module-marketplace-backend:v1
docker push acresamecloud.azurecr.io/auth-module-marketplace-backend:v1
```

- **Azure Key Vault**
  
  Sono stati aggiunti i seguenti secret per concedere l’accesso ai dati da parte dei container introdotti

### 🔑 Configurazione Auth-module

Il container dell’**Auth Module** utilizza i secret per connettersi al database e gestire l’autenticazione JWT:

```bash
az keyvault secret set --vault-name kv-esame-marketplace --name DB_URL --value "xxxxx"
az keyvault secret set --vault-name kv-esame-marketplace --name JWT_SECRET --value "xxxx"

### 🔑 Configurazione Api-Gateway

Il container dell’**API Gateway** utilizza i secret con i riferimenti al modulo di autenticazione ed il modulo api-backend, ed i secret per verficare l’autenticazione JWT.  

```bash
az keyvault secret set --vault-name kv-esame-marketplace --name API_URL --value "xxxxx"
az keyvault secret set --vault-name kv-esame-marketplace --name BACKEND_URL --value "xxxxx"
az keyvault secret set --vault-name kv-esame-marketplace --name JWT_SECRET --value "xxxx"

- **Azure Container Apps**

  Creazione Container Api-Gateway
```dockerfile
az containerapp create ^
  --name api-gateway-backend-esame ^
  --resource-group rg-esame-cloud ^
  --environment managedEnvironment-rgesamecloud-8803 ^
  --image acresamecloud.azurecr.io/backend:v1 ^
  --registry-server acresamecloud.azurecr.io ^
  --registry-username %ACR_USER% ^
  --registry-password %ACR_PASS% ^
  --ingress external ^
  --target-port 8082 ^
  --cpu 0.25 ^
  --memory 0.5Gi ^
  --set-env-vars ^
    API_URL = 
    BACKEND_URL = 
    ..ecc....
```

  Creazione Container Auth-Module
```dockerfile
az containerapp create ^
  --name auth-module-backend-esame ^
  --resource-group rg-esame-cloud ^
  --environment managedEnvironment-rgesamecloud-8803 ^
  --image acresamecloud.azurecr.io/backend:v1 ^
  --registry-server acresamecloud.azurecr.io ^
  --registry-username %ACR_USER% ^
  --registry-password %ACR_PASS% ^
  --ingress external ^
  --target-port 8081 ^
  --cpu 0.25 ^
  --memory 0.5Gi ^
  --set-env-vars ^
    DB_URL = 
    JWT_SECRET = 
    ..ecc....
```
----

**Autorizzare le Managed Identity dei Container App con il ruolo Key Vault Secrets User (permessi lettura)**
```dockerfile
az role assignment create \
  --assignee auth-module-backend-esame \
  --role "Key Vault Secrets User" \
  --scope /subscriptions/<SUBSCRIPTION_ID>/resourceGroups/rg-esame-cloud/providers/Microsoft.KeyVault/vaults/<KEY_VAULT_NAME>
```
```dockerfile
az role assignment create \
  --assignee api-gateway-backend-esame \
  --role "Key Vault Secrets User" \
  --scope /subscriptions/<SUBSCRIPTION_ID>/resourceGroups/rg-esame-cloud/providers/Microsoft.KeyVault/vaults/<KEY_VAULT_NAME>
```
----

**Mapping secret e variabili Key Vault con Container Apps**
```dockerfile
# Esempio di mapping secret
az containerapp secret set \
  --name api-gateway-backend-esame \
  --resource-group rg-esame-cloud \
  --secrets db-password=keyvaultref:https://kv-esame-marketplace.vault.azure.net/secrets/NomeSecret
```
```dockerfile
# Esempio di mapping secret
az containerapp secret set \
  --name auth-module-backend-esame \
  --resource-group rg-esame-cloud \
  --secrets backend_url=keyvaultref:https://kv-esame-marketplace.vault.azure.net/secrets/NomeSecret
```
----

## Modifiche comunicazione tra container

**Rete Interna del Container Apps Environment:**  
  Rispetto alla versione del **branch stage/azure-app**, viene sfruttata maggiormente la rete interna privata VLAN definita all'interno dell' **Azure Container Apps Environment**, in particolare tutti i container definiti appartengono allo stesso environnement e di conseguenza possono comuncare usufruendo della stessa rete. Questa nuova versione, permette ai container:  **auth-module** e **api-backend**, di essere isolati all'interno di questa rete, senza essere esposti verso l'esterno.

Il container api-gateway sfrutta il DNS presente all'interno della rete per comunicare con i moduli e proteggendo quindi l'applicazione.
Di conseguenza, si registra una minore latenza nelle risposte, perché le richieste sfruttando il DNS interno non vengono inoltrate attraverso la rete pubblica.

I moduli che per facendo parte della rete interna, sono esposti all'interno dell'architettura, sono:  **frontend-marketplace** e **api-gateway**, dal momento che il loro scopo è ricevere richieste provenienti dalla rete pubblica e saperle filtrare.



## ⚙️ Vantaggi ottenuti

1. **Esposizione dei Container:**  
   Con questa versione i moduli backend non hanno quindi più ingress type 'external' , ma bensì 'internal' e non saranno accessibili tramite riferimenti pubblici.

2. **Architettura rigida e centralizzata:**  
     Nella versione attuale, l'applicazione è più modulare e flessibile.  
      Il backend sfrutta il supporto di moduli separati, non contenendo tutta la logica applicativa all'interno dello stesso container.
      In particolare il container auth-mdoule si occupa della parte di autenticazione e generazione del token jwt, mentre il container api-backend dell'esposizione degli endpoint api e relative autorizzazioni.

3. **Supporto layer intermedio:**  
   Il container frontend non comunica direttamente con i container backend, perchè api-gatewat container funge da layer intermedio per instradare le richieste e di verificando le regole di routing.
   



