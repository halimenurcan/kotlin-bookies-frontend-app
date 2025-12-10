# Kotlin Bookies Frontend App — Buchverwaltungs-Frontend

Ein modernes Mobil-Applikation für die Bookies‑Anwendung, entwickelt in **Kotlin**.  
Das Projekt stellt die Benutzeroberfläche für eine Buchverwaltungsplattform bereit und kommuniziert mit einem separaten Backend‑Service.

---

## Übersicht

Dieses Repository enthält ausschließlich das **Frontend** der Anwendung.  
Benutzer können Bücher anzeigen, hinzufügen, bearbeiten oder löschen – abhängig von der verfügbaren API im Backend.

Das Projekt verwendet **Gradle** mit **Kotlin‑DSL**, eine saubere Projektstruktur und lässt sich plattformunabhängig über den Gradle‑Wrapper ausführen.

---

## Technologien

| Ebene | Technologien |
|-------|--------------|
| **Frontend** | Kotlin, Kotlin/JS (abhängig vom Build‑Target) |
| **Buildsystem** | Gradle, Kotlin‑DSL |
| **Projektstruktur** | Multi‑Module‑Ansatz mit `app/`, `docs/`, `gradle/` usw. |

---

## Projektstruktur (Auszug)

```
.idea/
app/
docs/
gradle/
gruppe-12---book-app/
build.gradle.kts
gradle.properties
gradlew / gradlew.bat
settings.gradle.kts
```

---

## Installation & Lokale Ausführung

Voraussetzung:  
- Java 17 oder höher  
- Gradle Wrapper ist bereits enthalten

### Repository klonen

```bash
git clone https://github.com/halimenurcan/kotlin-bookies-frontend-app.git
cd kotlin-bookies-frontend-app
```

### Build ausführen

```bash
./gradlew build
```

### Anwendung starten

Je nach Projektkonfiguration (Kotlin/JS / Webpack / Development‑Server) kann der Startbefehl variieren:

```bash
./gradlew run
```

Falls der Dev‑Server anders heißt:

```bash
./gradlew browserDevelopmentRun
```

*(Bitte ggf. anpassen – abhängig von der tatsächlichen Konfiguration.)*

---

## Funktionen (Frontend)

- Anzeige der Buchliste
- Detailansicht eines Buches
- Hinzufügen und Bearbeiten von Büchern
- Löschen von Einträgen
- Formulare mit Validierung
- Kommunikation mit Backend‑API

*(Die konkreten Funktionen hängen vom jeweiligen Backend ab.)*

---

## API‑Integration

Dieses Frontend benötigt ein Backend mit passenden Endpunkten (REST oder GraphQL).  
Typische Beispiel‑Konfigurationen:

```
BASE_URL=http://localhost:8080/api/v1/books
```

Bitte folgende Punkte beachten:
- API‑URL muss im Frontend konfiguriert werden
- Authentifizierung (falls vorhanden) hinzufügen
- CORS‑Konfiguration im Backend sicherstellen

---

## Deployment

Das Frontend kann wie folgt bereitgestellt werden:

### Produktion bauen

```bash
./gradlew build
```

Der finale Build befindet sich danach in:

```
build/distributions/
```

Diese Dateien können auf jedem Webserver gehostet werden (Nginx, Apache, S3, uvm.).

---


## Bekannte Einschränkungen

- Backend‑Dokumentation liegt nicht im selben Repo vor
- API‑Spezifikationen müssen manuell ergänzt werden
- CI/CD‑Konfiguration fehlt und kann hinzugefügt werden
- Keine Beschreibung zur State‑Verwaltung (Redux, Kotlin Store, o.ä.)

---

## Weiterentwicklung

Beiträge sind willkommen.

### Schritte für neue Features

1. Repository forken  
2. Neuen Branch erstellen (`feature/...`)  
3. Änderungen implementieren und committen  
4. Pull‑Request mit Beschreibung erstellen  

---

## Dokumentation (PDF)

Falls Du eine Projektdokumentation im PDF‑Format hinzufügen möchtest, lege sie in den Ordner `docs/` und verlinke sie wie folgt:

```markdown
[📄 Projektdokumentation herunterladen](docs/projektdokumentation.pdf)
```

---

## Lizenz

Dieses Projekt ist derzeit ohne festgelegte Lizenz.  
Falls eine Nutzung für Dritte vorgesehen ist, sollte eine Open‑Source‑Lizenz ergänzt werden.
