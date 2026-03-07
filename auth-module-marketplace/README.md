# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.

---

# 🚀 Obiettivi : Introduzione Auth Service 

L'obiettivo di questa fase è introdurre un auth service backend che funga da servizio di autenticazione all'interno del sistema, centralizzando la logica di autenticazione. Il suo ruolo principale è autenticare gli utenti dell'applicazione e generare un **JWT (JSON Web Token)** firmato che verrà utilizzato per autenticare le richieste verso i servizi backend. In aggiunta dovrà rispettare i seguenti aspetti all'interno dell'infrastruttura:

1. **Registrazione utente:**                                                                                                                                                          
    Il seguente servizio dovrà permettere a nuovi utenti di registrarsi all'interno dell'applicazione, registrando tutte le informazioni necessarie che saranno utili in un secondo momento ad autenticare l'utente.

2. **Login utente:**  
  Il seguente servizio dovrà permettere l'autenticazione e quindi l'accesso degli utenti all'interno dell'applicazione, andando a verificare che le informazioni inserite nella fase di autenticazione siano coerenti con i dati nel sistema   

3. **Hashing e chiavi segrete:**
   Il seguente servizio dovrà garantire la segretezza delle informazioni utente che andranno registrate nell'applicazione, dalle sue informazioni personali a quelle legate alla logica del jwt token che gli permetterà di autenticarsi. È molto importante che queste informazioni non possano essere manipolate da fonti esterne.
          
 4. **Generazione token JWT:**
  Una volta verificate le informazioni utente nella fase di autenticazione, verrà generato il token jwt con all'interno le informazioni necessarie per procedere nell' effettuare ulteriori verifiche . Il token jwt serve ad indicare che l'utente è effettivamente autenticato nel sistema, una volta generato verrà inoltrato ai servizi backend.

L'Auth Service è sviluppato utilizzando **Spring Boot** e **Spring Security**.  
Spring Security gestisce il processo di autenticazione degli utenti, il controllo delle credenziali e la protezione degli endpoint esposti dal servizio.

## ⚙️FLusso funzionamento Auth Service

   1. Il client richiede di autenticarsi al Auth Service, inserendo le proprie informazioni
   2. Auth Service verfica le informazioni inserite, recupera l'utente dal sistema (database) e genera token jwt firmato
   3. Il token jwt viene restuito al client , che lo inserirà nell'header delle richieste future
---
## ⚙️ Claim token jwt
   L' Auth service nella generazione del token jwt, inserisce nel suo body una serie di informazioni utili per la logica di business dell'applicazione. Questa collezione di informazioni 
   prende il nome di claims. Nell'applicazione in questione, vengono aggiunte le seguenti claims:

    - `profileId`
    - `username`
    - `role`
    - `shopId` (opzionale se l'utente è un seller)


