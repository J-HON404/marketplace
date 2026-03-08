# CI/CD Pipeline – Build and Push Docker Images

Questa pipeline GitHub Actions esegue automaticamente il **build e il push delle immagini Docker** dei servizi del marketplace su **Azure Container Registry (ACR)**.

La pipeline si attiva ogni volta che viene fatto un **push sul branch `final/modular-marketplace`**.

---

# Workflow

Il workflow esegue i seguenti passaggi:

1. **Checkout del repository**
   - Clona il codice del progetto.

2. **Login su Azure**
   - Autenticazione su Azure tramite **Service Principal** utilizzando i secrets del repository.

3. **Generazione versione immagine**
   - Viene creata automaticamente una variabile `IMAGE_VERSION` nel formato: v<GITHUB_RUN_NUMBER>

Questo garantisce che ogni build abbia una **versione univoca**.

---

# Build e Push delle immagini

La pipeline costruisce e pubblica le immagini Docker dei seguenti servizi:

| Servizio | Cartella | Nome immagine |
|--------|--------|--------|
| Auth Module | `marketplace-auth-module` | `acresamecloud.azurecr.io/auth-module-marketplace-backend` |
| Backend API | `marketplace-backend-api` | `acresamecloud.azurecr.io/backend` |
| Frontend | `marketplace-frontend` | `acresamecloud.azurecr.io/frontend` |
| Gateway API | `marketplace-gateway-api` | `acresamecloud.azurecr.io/api-backend-gateway-marketplace` |

Ogni immagine viene taggata con la versione generata: acresamecloud.azurecr.io/<image-name>:v<run-number>

# Secrets necessari

Il workflow utilizza i seguenti **GitHub Secrets** per autenticarsi su Azure:

AZURE_CLIENT_ID
AZURE_CLIENT_SECRET
AZURE_TENANT_ID
AZURE_SUBSCRIPTION_ID

Questi sono i dati del **Service Principal** con accesso al Container Registry (acresamecloud)


Questi sono i dati del **Service Principal** con accesso al Container Registry.

---

# Risultato della pipeline

Alla fine della pipeline:

- tutte le immagini Docker vengono **buildate**
- vengono **versionate automaticamente**
- vengono **pubblicate su Azure Container Registry**

Queste immagini possono poi essere utilizzate per il **deploy su Azure Container Apps
