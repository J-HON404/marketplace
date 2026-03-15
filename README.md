
# Modifiche Introdotte

rispetto i concetti descritti nel branch  `final/modular-marketplace`  sono state apportate le seguenti modifiche:
1) è stato inserito un container dedicato per la gestione di una memoria cache, con lo scopo di isolare e velocizzare la fase di controllo del token per ogni richiesta ricevuta.
2) migliorata la logica di logout per prevenire l’uso di token scaduti o già invalidati.
  Ora il logout cancella esplicitamente il token lato backend (Redis) oltre che lato client.
  In questo modo, anche se l’utente tenta di riutilizzare lo stesso token dopo il logout, il gateway lo invalida immediatamente.
   Previene problemi di sicurezza legati a sessioni attive o token residui. 

## 📌 Cos'è Redis?

Redis è un database **in-memory** che risiede nella RAM, garantendo performance elevate e isolamento dei dati.

* **Protocollo:** Lavora con protocollo **TCP** (non HTTP).
* **Performance:** Accessi estremamente veloci ai dati, tipicamente **< 1ms**.
* **Modello:** Database Key-Value ad alta velocità.

---

## 🏗️ Struttura della Cache

I dati vengono salvati in un formato stringa semplice, ottimizzato per operazioni di split rapide.

* **Key:** `auth:token:<stringa_del_token>`
* **Value:** `profileId|role|shopId`
* **TTL:** 86400 secondi (Default: 24 ore)

**Esempio:**
> **KEY:** `auth:token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`  
> **VALUE:** `21|SELLER|20`

---

## ⚙️ Logica di Funzionamento

### 1. Fase di Autenticazione (Auth Module)
1. Il client invia le credenziali al **Gateway**.
2. Il Gateway inoltra la richiesta all'**Auth Module**.
3. L'Auth Module valida le credenziali su **MySQL** e genera un **JWT**.
4. L'Auth Module scrive su Redis:  
   `SET <token> <claims_data> EX 86400`
5. Il token viene restituito al client.

### 2. Richieste Successive (Gateway)
1. Il Gateway riceve una richiesta con il token e interroga Redis (`GET <token>`).
2. **Cache Hit:** Se il token è presente, le claims sono già pronte. Il Gateway costruisce gli header per il backend senza decodificare il JWT.
3. **Cache Miss:** Se il token non è presente, il Gateway esegue la validazione JWT standard (Fallback).
---

## 🚀 Vantaggi dell'Approccio

* **Risparmio di tempo:** Accesso diretto alla RAM invece di eseguire decodifiche Base64 e verifiche crittografiche.
* **Revoca istantanea:** È sufficiente eliminare la chiave su Redis per invalidare immediatamente un token.
* **High Availability:** Se Redis non risponde, il sistema scala automaticamente sulla validazione JWT standard.
* **Auto-Cleanup:** Il meccanismo di TTL (Time To Live) elimina i token scaduti automaticamente.
* **Statelessness:** La sessione è centralizzata in Redis, il Gateway rimane senza stato.
* * **Migliore User Experience:** La navigazione tra le varie pagine all'interno del sito è più fulminea.

---

## 🌐 Comunicazione tra Container

### Azure Container Apps
Il service discovery avviene tramite DNS interno all'environment:
* **Host:** `marketplace-redis-cache:6379`
* **Tipo:** Connessione TCP diretta (no HTTP/Ingress).

### Docker (Local)
I container comunicano sulla stessa rete bridge (`marketplace-network`):
* **Host:** `marketplace-redis:6379`
* **Risoluzione:** Il DNS interno di Docker mappa il nome del servizio all'IP privato del container.

---

## 🛠️ Esempi di Comandi Redis (CLI)

### Visualizzare operazioni effettuate nella cache Redis
 ```bash
    redis-cli
   MONITOR
```

### Inserimento di un Token
Inserisce un token con claims e scadenza a 24 ore.
```bash
SET auth:token:eyJhbGciOiJIUzI1... "21|SELLER|20" EX 86400
```

### Lettura di un Token
Utilizzato dal Gateway per verificare la validità di una sessione e recuperare le claims associate.
```bash
# Verifica la presenza del token e restituisce il valore (es. "profileId|role|shopId")
GET auth:token:eyJhbGciOiJIUzI1...
```

### Rimozione di un Token
```bash
# Rimuove immediatamente la chiave dalla memoria RAM
DEL auth:token:eyJhbGciOiJIUzI1...
```


### Controllo scadenza Token (TTL)
```bash
# Mostra il tempo residuo (Time To Live) in secondi
TTL auth:token:eyJhbGciOiJIUzI1...
```
* **`n` (Valore positivo):** Indica il numero esatto di **secondi rimanenti** prima della scadenza e cancellazione automatica.
* **`-2`:** Il token **non esiste** (è già scaduto, è stato rimosso manualmente o non è mai stato creato).
* **`-1`:** Il token esiste ma **non ha una scadenza** definita (è persistente in memoria).
---

## Modificato Auth Controller 
Al momento di un login avvenuto con successo, il sistema genera un token JWT e invoca il metodo privato `saveTokenInRedis` per persistere lo stato della sessione.
```bash
private void saveTokenInRedis(String token, Long profileId, ProfileRole role, Long shopId) {
    // Formato: profileId|ROLE|shopId
    String cacheValue = profileId + "|" + role.name() + "|" + (shopId != null ? shopId : "");
    try {
        // Salvataggio con Time-To-Live (TTL) di 24 ore
        redisTemplate.opsForValue().set("auth:token:" + token, cacheValue, 24, TimeUnit.HOURS);
    } catch (Exception e) {
        System.err.println("Errore salvataggio Redis: " + e.getMessage());
    }
}
```
Inoltre è stato introdotto il seguente enpoint logout per eseguire la cancellazione del token jwt in modo sicuro 
```bash
 @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // rimuove "Bearer "
                redisTemplate.delete("auth:token:" + token); // cancella il token dalla cache
                return ResponseEntity.ok(ApiResponse.success(null));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("Authorization header mancante o non valido"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("Errore durante il logout: " + e.getMessage()));
        }
    }
```
---

# 🚀 Modifica Gateway Filter

L'obiettivo di questa modifica è ottimizzare le prestazioni del gateway intercettando le richieste in entrata. Il sistema verifica se i dati dell'utente sono già presenti in memoria Redis) e solo in caso di "cache miss", procede con la validazione standard del JWT, popolando poi la cache per le chiamate successive.

---

## 🔍 Scenario 1: Lettura dalla Cache

Prima di eseguire il parsing del JWT (operazione CPU-intensive), il filtro interroga Redis utilizzando il token come chiave. Se il token è presente, le informazioni vengono estratte direttamente dalla memoria.

```bash
private Mono<Void> checkCache(String token, ServerWebExchange exchange, GatewayFilterChain chain) {
    String cacheKey = "auth:token:" + token;

    return redisTemplate.opsForValue()
            .get(cacheKey) // Recupera il valore: "profileId|ROLE|shopId"
            .flatMap(cachedData -> {
                String[] parts = cachedData.split("\\|");
                String profileId = parts[0];
                String role = parts[1];
                String shopId = parts.length > 2 ? parts[2] : "";
                
                // Muta la richiesta con i dati estratti dalla cache
                ServerWebExchange mutatedExchange = buildExchange(exchange, profileId, role, shopId);
                return chain.filter(mutatedExchange);
            });
}
```
 I microservizi a valle ricevono gli header X-Profile-Id, X-Role e X-Shop-Id senza che il Gateway debba decodificare il JWT, riducendo drasticamente la latenza.

## 🔍 Scenario 2: Validazione e Fallback

Se il token non è presente in Redis, il sistema esegue la validazione JWT standard. Una volta confermata la validità, i dati vengono memorizzati in modo asincrono per ottimizzare le richieste future.

```bash
// Estratto da handleJwtValidation
String cacheKey = "auth:token:" + token;
String cacheValue = profileId + "|" + role + "|" + shopIdStr;

redisTemplate.opsForValue()
    .setIfAbsent(cacheKey, cacheValue, Duration.ofHours(24)) // Salva con TTL di 24h
    .subscribe(); // Operazione asincrona non bloccante
```
Il salvataggio avviene con un TTL (Time-To-Live) di 24 ore, garantendo che la sessione in cache scada automaticamente.

---

## 🔗 Flusso Finale

Dopo il recupero dei dati (sia da cache che da validazione diretta), il Gateway costruisce gli header della richiesta e la inoltra al modulo api-backend. Quest'ultimo si occuperà esclusivamente dei controlli di autorizzazione specifici per l'endpoint richiesto.

---
