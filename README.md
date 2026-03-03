# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.


# 🚀 Obiettivi Fase 4 : Introduzione Microsoft Azure

L'obiettivo è migrare l'applicazione presente nel branch stage/local-dockerV2 in un ambiente cloud professionale, come Microsoft Azure, con lo scopo di trasformare l'applicazione in un approccio cloud-native.
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

La migrazione da Docker Compose locale a un’infrastruttura Azure basata su servizi gestiti consente di ottenere un sistema più **scalabile, sicuro, monitorabile e ottimizzato**, rendendo l’applicazione più adatta a un utilizzo reale e pienamente allineata ai principi di un’architettura **cloud-native**.
