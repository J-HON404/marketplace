# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.


# 🚀 Obiettivi Fase 4 : Introduzione Microsoft Azure

L'obiettivo è migrare l'applicazione presente nel **branch stage/local-dockerV2** in un ambiente cloud professionale, come Microsoft Azure, con lo scopo di avvaire l'applicazione in un approccio cloud-native con un architettura containerizzata composta da: container frontend, container backend e database. Nelle iterazioni successive si andrà a migliorare l'archiettura ed applicare meglio i principi cloud native.
Docker Compose è uno strumento molto utile in fase di sviluppo e test, ma risulta limitato in contesti produttivi perché opera principalmente su un ambiente single-host e richiede una gestione manuale di aspetti fondamentali come scalabilità, disponibilità e monitoraggio. Al contrario, adottando servizi Azure per il frontend Angular e per il backend , è possibile ottenere un’infrastruttura più moderna e adatta ad ambienti reali di produzione, sfruttando i vantaggi del cloud.


## Vantaggi principali della migrazione su Azure

- **Scalabilità automatica**  
  Azure permette di aumentare o ridurre dinamicamente le risorse in base al carico di lavoro, arrivando anche alla possibilità di *scalare a zero* quando l’applicazione non viene utilizzata, riducendo così i costi.  
  Questo introduce un modello pay-per-use, molto più efficiente rispetto all’approccio tradizionale in cui una macchina deve rimanere sempre attiva anche in assenza di traffico.

- **Gestione centralizzata di credenziali e secret**  
  È possibile gestire in modo centralizzato e professionale credenziali e secret applicativi, evitando di inserirli in file locali o variabili d’ambiente non protette.  
  Questo migliora notevolmente la protezione dei dati e la sicurezza complessiva del sistema.

- **Backup automatici e manutenzione gestita**  
  Backup automatici, aggiornamenti e manutenzione gestita, maggiore affidabilità e strumenti integrati di monitoraggio riducono il rischio di malfunzionamenti e il lavoro operativo rispetto a un database containerizzato in locale.

- **Deployment migliorato**  
  È possibile versionare e distribuire in modo ordinato le immagini Docker, integrando facilmente pipeline CI/CD e rendendo il rilascio dell’applicazione più stabile e ripetibile rispetto a un semplice docker compose up.

- **Strumenti di osservabilità**  
  Raccolta centralizzata di log, metriche, tracciamento delle richieste e analisi degli errori, per una visione completa dello stato dell’applicazione.

- **Architettura più modulare (microservizi)**  
  Possibilità di rendere frontend, backend e database servizi distinti, ottenendo un’architettura più modulare e coerente con il paradigma dei microservizi, aumentando la manutenibilità e la facilità di evoluzione del progetto.

---

### Conclusione

La migrazione da Docker Compose locale a un’infrastruttura Azure basata su servizi gestiti consente di ottenere un sistema più scalabile, sicuro, monitorabile e ottimizzato, rendendo l’applicazione più adatta a un utilizzo reale e pienamente allineata ai principi di un’architettura cloud-native.


## Architettura del sistema

Tra i numerosi servizi disponibili, sono stati selezionati quelli più coerenti con la filosofia dell’applicazione e con gli obiettivi di crescita ed evoluzione della versione attuale.
L’obiettivo finale non è mantenere un’applicazione cloud-native di tipo monolitico, come nella sua forma attuale, ma evolverla progressivamente verso un’architettura basata su microservizi. 
Questo approccio consente di sfruttare pienamente la flessibilità, la scalabilità e l’affidabilità offerte da un ambiente cloud come Azure, permettendo a ciascun componente dell’applicazione di essere sviluppato, distribuito e scalato in modo indipendente.

> *Nota:* I comandi inseriti sono solo a scopo descrittivo, non rappresentano tutte le configurazioni associate per ogni singolo componente.

- **Resource Group**
  
    È il contentitore logico in cui vengono raggruppate tutte le risorse dell'applicazione, permette di monitorare facilmente le componenti di una stessa applicazione.

```dockerfile
az group create \
  --name rg-esame-cloud \
  --location swedencentral
```

- **Azure Container Registry**
  
    È il registro logico in cui vengono raggruppate tutte le immagini docker, pronte per il deploy dei container dell'applicazione
  
```dockerfile
az acr create \
  --resource-group rg-esame-cloud \
  --name acresamecloud \
  --sku Basic \
  --admin-enabled true \
  --location swedencentral
```

- **MySQL Flexible Server**
  
  È stato scelto un database relazionale MySQL per garantire una gestione dei dati efficiente, scalabile e sicura.  
  L’approccio relazionale è stato preferito rispetto a soluzioni NoSQL, in quanto meglio adatto al modello dati e alle esigenze dell’applicazione.
  Inoltre permette:   - *Backup automatici* - *Restoring dati* - *Alta disponbilità*  

```dockerfile
az mysql flexible-server create \
    --name mysql-backend-esame \
    --resource-group rg-esame-cloud \
    --location francecentral \
    --admin-user xxxxx \
    --admin-password xxxxx \
    --sku-name B1ms \
    --tier Burstable \
    --version 8.0 \
    --storage-size 20 \
    --backup-retention 7 \
    --geo-redundant-backup Disabled \
    --high-availability Disabled \
    --lower-case-table-names 1 \
    --public-access 0.0.0.0
```

```dockerfile

az mysql flexible-server db create \
  --resource-group rg-esame-cloud  \
  --server-name server-database-esame \
  --database-name marketplace
```

- **Azure Key Vault**
  
  È stato scelto per la gestione sicura di secret, chiavi e variabili di ambiente, evitando di inserirli direttamente nei container o in chiaro nelle immagini Docker.  
  Garantisce sicurezza e controllo granulare degli accessi, permettendo di concedere l’accesso ai dati in base ai ruoli degli utenti o dei servizi.

```dockerfile
az keyvault create \
  --name kv-esame-marketplace \
  --resource-group rg-database-esame \
  --location francecentral
```

```dockerfile
az keyvault secret set --vault-name kv-esame-marketplace --name DbPassword --value "xxxx"
```

```dockerfile
az keyvault secret set --vault-name kv-esame-marketplace --name JwtSecret --value "yyyyyy"
```

- **Azure Container Apps**
  
È stato scelto per la gestione dei container, perché permette di eseguire container Docker senza doversi occupare manualmente dell’orchestrazione tramite macchine virtuali o Kubernetes, poiché questa parte viene gestita automaticamente dal servizio internamente.

In pratica, sarà sufficiente caricare i container dell’applicazione e **Azure** si occuperà della loro esecuzione e gestione. Inoltre, permette la gestione automatica di:

- **Auto Scaling**
- **Networking**
- **Load Balancer**
- **Aggiornamenti e deployment**

**Container Apps Environment**

È l'ambiente che gestisce in modo centralizzato i diversi **Container Apps** di cui è composta un’applicazione, fornendo i seguenti vantaggi:

- **Networking interno condiviso tra i container**
- **Auto Scaling**
- **Monitoraggio e logging**
        
```dockerfile
    az containerapp env create \
    --name managedEnvironment-rgesamecloud-8803 \
    --resource-group rg-esame-cloud \
    --location "France Central"
```

  Container Backend
```dockerfile
az containerapp create ^
  --name ca-backend-esame ^
  --resource-group rg-esame-cloud ^
  --environment managedEnvironment-rgesamecloud-8803 ^
  --image acresamecloud.azurecr.io/backend:v1 ^
  --registry-server acresamecloud.azurecr.io ^
  --registry-username %ACR_USER% ^
  --registry-password %ACR_PASS% ^
  --ingress external ^
  --target-port 8080 ^
  --cpu 0.25 ^
  --memory 0.5Gi ^
  --set-env-vars ^
    DB_URL = 
    DB_HOST=  ^
    ..ecc....
```

Container Frontend
```dockerfile
  az containerapp create \
  --name ca-frontend-esame \
  --resource-group rg-esame-cloud \
  --environment managedEnvironment-rgesamecloud-8803 \
  --image acresamecloud.azurecr.io/frontend:v1 \
  --registry-server acresamecloud.azurecr.io \
  --registry-username <ACR_USER> \
  --registry-password <ACR_PASS> \
  --ingress external \
  --target-port 80 \
  --cpu 0.25 \
  --memory 0.5Gi
    --set-env-vars ^
    BACKEND_URL = 
    HOST_BACKEND =  ^
```
----

**Autorizzare le Managed Identity dei Container App con il ruolo Key Vault Secrets User (permessi lettura)**
```dockerfile
az role assignment create \
  --assignee ca-frontend-esame \
  --role "Key Vault Secrets User" \
  --scope /subscriptions/<SUBSCRIPTION_ID>/resourceGroups/rg-esame-cloud/providers/Microsoft.KeyVault/vaults/<KEY_VAULT_NAME>
```
```dockerfile
az role assignment create \
  --assignee ca-backend-esame \
  --role "Key Vault Secrets User" \
  --scope /subscriptions/<SUBSCRIPTION_ID>/resourceGroups/rg-esame-cloud/providers/Microsoft.KeyVault/vaults/<KEY_VAULT_NAME>
```
----

**Mapping secret e variabili Key Vault con Container Apps**
```dockerfile
# Esempio di mapping secret
az containerapp secret set \
  --name ca-backend-esame \
  --resource-group rg-esame-cloud \
  --secrets db-password=keyvaultref:https://kv-esame-marketplace.vault.azure.net/secrets/DbPassword
```
```dockerfile
# Esempio di mapping secret
az containerapp secret set \
  --name ca-frontend-esame \
  --resource-group rg-esame-cloud \
  --secrets backend_url=keyvaultref:https://kv-esame-marketplace.vault.azure.net/secrets/BackendUrl
```
----

**Caricamento immagini docker sul ACR AZURE (Container Registry)**
```dockerfile
az acr login --name acresamecloud 
docker tag marketplace-frontend acresamecloud.azurecr.io/frontend:v2
docker tag marketplace-backend acresamecloud.azurecr.io/backend:v2
docker push acresamecloud.azurecr.io/frontend:v2
docker push acresamecloud.azurecr.io/backend:v2
```
Le immagini Docker vengono caricata sul container registry Azure e saranno disponibili per il deploy su Container App.

**Verifica immagini presenti nel ACR AZURE (Container Registry)**
```dockerfile
az acr repository list --name acresamecloud --output table
az acr repository show-tags --name acresamecloud --repository backend --output table
```

##  Analisi Comunicazione tra il Container frontend e backend

**Rete Interna del Container Apps Environment:**  
   La comunicazione tra i container avviene all'interno della rete interna del **Container Apps Environment**, sfruttando il DNS presente all'interno . Il container frontend ricevuta la       richiesta da parte del browser, tramite reverse proxy Nginx risolve internamente indrizzo backend che è privato, ed inoltra la richiesta.
   Di conseguenza, si registra una minore latenza nelle risposte, perché le richieste sfruttando il DNS interno non vengono inoltrate attraverso la rete pubblica.

## ⚙️ Analisi delle Criticità

1. **Esposizione dei Container:**  
   I container frontend e backend creati hanno un **ingress type `external`** e quindi sono esposti pubblicamente.

2. **Architettura rigida:**  
   Nella versione attuale, l'applicazione è poco modulare e flessibile.  
   Il backend non sfrutta microservizi, ma contiene tutta la logica applicativa all'interno dello stesso container.
