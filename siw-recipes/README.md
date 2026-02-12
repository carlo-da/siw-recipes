# SiwRecipes - Piattaforma Web per la Condivisione Culinaria

## 1. Descrizione del Progetto

Il progetto **SiwRecipes** è stato sviluppato nel contesto del corso di *Sistemi Informativi su Web* (SIW) presso l'Università degli Studi Roma Tre. L'obiettivo primario dell'elaborato è la progettazione e l'implementazione di un'applicazione web *enterprise* basata sull'architettura Model-View-Controller (MVC), che permetta la gestione, la condivisione e la fruizione di contenuti culinari.

La piattaforma si configura come un ricettario digitale collaborativo, volto a creare un punto di incontro tra appassionati di cucina. Attraverso un'interfaccia utente moderna e intuitiva, il sistema permette agli utenti di consultare un vasto catalogo di ricette e, previa registrazione, di contribuire attivamente all'arricchimento del database condividendo le proprie creazioni. Il progetto pone particolare enfasi sulla robustezza del backend, sulla sicurezza dei dati e sull'usabilità lato client.

## 2. Casi d'Uso e Attori del Sistema

Il sistema è progettato per supportare diverse tipologie di interazione in base al ruolo dell'utente autenticato. Vengono identificati tre attori principali:

### A. Utente Generico (Non Autenticato)
L'utente ospite ha accesso alle funzionalità di consultazione pubblica della piattaforma:
* **Navigazione:** Accesso alla Home Page e visualizzazione delle ricette in evidenza.
* **Consultazione:** Visualizzazione dei dettagli di una ricetta (titolo, ingredienti, procedimento, foto).
* **Ricerca:** Utilizzo della barra di ricerca globale per filtrare le ricette tramite parole chiave nel titolo.
* **Registrazione/Login:** Accesso ai form di autenticazione o registrazione alla piattaforma.

### B. Utente Registrato (Role: DEFAULT)
L'utente autenticato eredita i permessi dell'utente generico e acquisisce funzionalità di *authoring*:
* **Creazione Ricette:** Inserimento di nuove ricette tramite un wizard dedicato, con possibilità di specificare titolo, descrizione, tempi di cottura, difficoltà, categoria e lista degli ingredienti.
* **Gestione Immagini:** Caricamento (upload) di immagini personalizzate per le proprie ricette.
* **Gestione Personale:** Modifica o cancellazione delle proprie ricette precedentemente inserite.
* **Accesso Profilo:** Visualizzazione dei propri dati e storico delle attività.

### C. Amministratore (Role: ADMIN)
L'amministratore possiede privilegi elevati per la supervisione e la manutenzione del sistema:
* **Gestione Utenti:** Visualizzazione della lista completa degli iscritti, con facoltà di bannare (disabilitare) o riattivare account in caso di violazioni.
* **Gestione Categorie:** Creazione, modifica e cancellazione delle categorie merceologiche (es. "Primi Piatti", "Dolci") per mantenere organizzato il catalogo.
* **Supervisione Contenuti:** Facoltà di eliminare qualsiasi ricetta ritenuta inappropriata, indipendentemente dall'autore.

## 3. Specifiche Tecniche e Features Implementate

Il progetto è stato realizzato utilizzando lo stack tecnologico Java/Spring Boot, adottando le migliori pratiche di ingegneria del software.

* **Backend & Framework:** Sviluppato in **Java** su framework **Spring Boot**. L'architettura rispetta il pattern MVC, garantendo una netta separazione tra logica di business, persistenza dei dati e interfaccia utente.
* **Persistenza dei Dati (JPA/Hibernate):** L'interazione con il database relazionale (**PostgreSQL**) è gestita tramite **Spring Data JPA**. Il modello ER è mappato attraverso annotazioni Hibernate, gestendo relazioni complesse (One-to-Many, Many-to-One) e propagazione delle operazioni (Cascade).
* **Sicurezza (Spring Security):** L'autenticazione e l'autorizzazione sono gestite tramite **Spring Security**. Le password sono criptate mediante algoritmo **BCrypt**. L'accesso alle rotte sensibili (es. `/admin/**`) è protetto da controlli basati sui ruoli (Role-Based Access Control).
* **Autenticazione Federata (OAuth2):** È stata implementata l'integrazione con **Google Sign-In** tramite il protocollo OAuth 2.0. Questo permette agli utenti di accedere rapidamente utilizzando le proprie credenziali Google, con un meccanismo di *JIT Provisioning* che registra automaticamente i nuovi utenti nel database locale.
* **Gestione Immagini Ibrida:** Il sistema supporta sia immagini statiche (per i dati di inizializzazione) sia l'upload dinamico di file da parte degli utenti, salvati in una directory esterna e serviti tramite un controller dedicato per garantire la persistenza oltre il ciclo di vita dell'applicazione.
* **Frontend:** Realizzato con il motore di template **Thymeleaf** integrato con **Bootstrap 5** per garantire un design semplice, responsivo e moderno.
* **Data Initializer:** Implementazione di un `CommandLineRunner` per il popolamento automatico del database all'avvio (mock data), facilitando il testing e la dimostrazione delle funzionalità.

-----