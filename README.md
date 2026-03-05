# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.

---

# 🚀 Obiettivi : Introduzione API Gateway 

L'obiettivo di questa fase è introdurre un api-gateway che funga da ponte nella comuncazione tra le parti del sistema. Il suo ruolo principale sarà quello di instradare le richieste che arrivano dall'host e quindi dal frontend, ed inoltrarle al backend. In aggiunta dovrà rispettare i seguenti aspetti all'interno dell'infrsatruttura:

1. **Punto di ingresso unico:**                                                                                                                                                          
Il seguente api-gateway rappresenterà il punto nevralgico e centrale dell'applicazione. L'intera infrastruttura dipenderà da esso, il quale dovrà coordinare la comunicazione tra i          vari servizi, valutando in base alla richiesta, a quale servizio instradarla

3. **Controlli centralizzati:**  
Un'altro aspetto da considerare è quello della sicurezza. Il seguente api-gateway dovrà validare la richiesta, verificando la sua validità. In un architettura REST-API questo si traduce nel verificare se la richiesta presenta nell'header un token jwt valido, quindi accertandosi della sua firma, scadenza e formato. Se il controllo non viene passato con scuccesso, dovrà scartare la richiesta e non dovrà inoltrarla al backend.
   
4. **Supporto verso servizi backend:**  
  L'api-gateway dovrà fornire supporto nei confronti dei servizi backend, non delegando semplicemente le richieste ricevute, ma una volta identificata e validata la richiesta, potrà            manipolare il suo header, con lo scopo informare le parti backend, che quella richiesta è stata effettivamente analizzata e validata, permettendo così di procedere applicando controlli di autorizzazione approfonditi e di responsabilità della logica di business. In questo modo la parte backend non dovrà preoccuparsi di verficare la richiesta dall'inzio, ma potrà svolgere        controlli mirati e non ripetere controlli già effettuati.

 5. **Tempi di risposta brevi:**  
L'api-gateway per garantire tempi di risposta brevi e quindi efficienza all'interno del sistema, dovrà occuparsi di pochi compiti specifici. In questo modo non avrà alcun tipo di sovraccarico da gestire e le richieste possono passare velocemente alle altre parti del sistema, comportando meno latenza possibile. Il vantaggio nel limitare le operazioni da svolgere sta nel fatto che se si occupasse di effettuare controlli di sicurezza approfonditi legati ad esempio alla logica di autorizzazione dell'applicazione, andrebbe a prendere le vesti della logica di business e quindi del backend, portando così ad uno svantaggio in termini di manutenzione o espansione futura in termini di regole di business, obbligando ad aggiornare le regole da entrambe le parti.

6. **Protezione del sistema:**  
  L'api-gateway farà da protezione del sistema nei confronti del backend, dovrà quindi proteggere i servizi interni dell'applicazione da esposizioni esterne e pericoli, un utente esterno potrà interagire con l'applcazione solo passando da esso e quindi ricevendo la sua autorizzazione

