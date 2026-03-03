# 🛒 Marketplace Web Application

Progetto marketplace full-stack basato su **Spring Boot** e **Angular**. L'applicazione gestisce un sistema multi-venditore in cui gli utenti possono operare come `SELLER` o `CUSTOMER` per gestire shop, prodotti e ordini in tempo reale. La versione attuale è sviluppata seguendo una struttura modulare e facilmente estendibile.
Tuttavia, il sistema non è pensato per gestire un’elevata complessità di dati o carichi ad alta scala, né per scenari enterprise con requisiti avanzati di performance e concorrenza distribuita. Rappresenta piuttosto una base solida, sicura ed organizzata, adatta a progetti di media dimensione e pronta a future evoluzioni architetturali.

---

## 🚀 Obiettivi Fase 4 : Introduzione Microsoft Azure

L'obiettivo è migrare l'applicazione presente nel branch stage/local-dockerV2 in un ambiente cloud professionale, come Microsoft Azure, con lo scopo di trasformare l'applicazione in un approccio cloud-native.
Docker Compose è uno strumento molto utile in fase di sviluppo e test, ma risulta limitato in contesti produttivi perché opera principalmente su un ambiente single-host e richiede una gestione manuale di aspetti fondamentali come scalabilità, disponibilità e monitoraggio. Al contrario, adottando servizi Azure per il frontend Angular e per il backend , è possibile ottenere un’infrastruttura più moderna e adatta ad ambienti reali di produzione, sfruttando i vantaggi del cloud.
---

