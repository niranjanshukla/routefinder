# Route Finder

## Project Specification

## 1. Vision

We will create an application using Spring Boot and Maven to traverse a Graph structure made out of JSON data using BFS with the goal of finding the optimal path between two countries, abbreviated by their cca3 acronyms. JSON Data can be obtained from https://raw.githubusercontent.com/mledoze/countries/master/countries.json. Agents interact through files in `planning/`.

## 2. User Experience

### REST Endpoint

The application exposes REST endpoint `http://localhost:8080/routing/{origin}/{destination}` that
returns a list of border crossings to get from origin to destination, or HTTP 400 where one does not exist

## 3. Architecture Overview

### Single Container, Single Port

```
┌─────────────────────────────────────────────────┐
│  Docker Container (port 8080)                   │
│                                                 │
│  Spring Boot (Java 21/25 LTS · Maven)           │
│  ├── /api/*          REST endpoints (Spring MVC)│
│  └── /actuator/*     health, info, metrics      │
│                                                 │
│  In-memory country index/graph (built at boot)  │
└─────────────────────────────────────────────────┘

```

### Architecture Decision Rationale

| Decision | Rationale |
|---|---|
| In-memory Map keyed by cca3 | The JSON data is small and read only. Using a HashMap to give lookup capability in ms |
| Bundle JSON at Build time | To remove network dependency to fetch the static countries.json at startup |

---

## 4. Directory Structure

```
Route-Finder/
├── CLAUDE.md
├── pom.xml                          
├── Dockerfile
├── .dockerignore
├── README.md
├── .claude/
│   └── skills/
│       └── dependency-security-hygiene/
│           └── SKILL.md
├── planning/
│   └── PLAN.md   
└── src/
    └── main/
        ├── java/com/example/countryapi/
        │   ├── CountryApiApplication.java
        │   ├── model/
        │   │   ├── Country.java          ← record, ignores unknown fields
        │   │   ├── CountryStore.java     ← loads JSON, internal store (not exposed)
        │   │   └── RouteService.java     ← findRoute() stub for your BFS
        │   └── web/
        │       └── RoutingController.java ← GET /routing/{origin}/{destination}
        └── resources/
            ├── application.yml           ← port 8080
            └── countries.json            ← bundled dataset (250 records)
```

---


## 5. API Endpoints

### Routing
| Method | Path | Description |
|--------|------|-------------|
| GET | `/routing/{origin}/{destination}` | Fetching a route between two countries through land borders, if one exists |


`GET /routing/{origin}/{destination}` — 
```json
{
  "origin": "NLD",
  "destination": "POL",
  "found": true,
  "path": ["NLD", "DEU", "POL"],
  "hops": 2
}
```

---

## 6. Docker & Deployment

### Multi-Stage Dockerfile

Stage 1: maven:3.9-eclipse-temurin-21 (build)
  - Copy pom.xml, go-offline to cache dependencies
  - Copy src/, mvn clean package -DskipTests (produces fat JAR)

Stage 2: eclipse-temurin:21-jre (runtime)
  - Copy route-finder-0.0.1-SNAPSHOT.jar from build stage
  - Expose port 8080
  - ENTRYPOINT: java -jar app.jar

  Alternative: skip the Dockerfile entirely with
  `mvn spring-boot:build-image` (Cloud Native Buildpacks → OCI image).

### No Volume

The dataset is read-only and lives in memory. The container is stateless and disposable:

  docker run -p 8080:8080 route-finder

No volume mount, no --env-file required.

### Start/Stop Scripts

scripts/start_mac.sh (macOS/Linux):
  - Builds the image if missing (or if --build passed)
  - Runs the container with port mapping
  - Prints the URL (http://localhost:8080)
  - Optionally opens the browser

scripts/stop_mac.sh (macOS/Linux):
  - Stops and removes the running container

scripts/start_windows.ps1 / scripts/stop_windows.ps1: PowerShell equivalents.

### Optional Cloud Deployment

Stateless container deploys cleanly to AWS App Runner, Render, Fly.io, or
any container platform — no volume or managed DB to provision. A Terraform
config for App Runner may live in deploy/ as a stretch goal, not core build.

---

## 7. Testing Strategy

### Unit — CountryStore
Plain JUnit, no Spring context. Confirms the JSON loaded and indexed right:
  - all 250 records present after load
  - findByCca3("nld") resolves case-insensitively; "ZZZ" → Optional.empty()
  - borders populated (NLD → [BEL, DEU])

### Web slice — RoutingController
@WebMvcTest + MockMvc, real store, BFS mocked. Asserts the HTTP contract only:
  - GET /routing/NLD/POL  → 200, well-formed RouteResult (found, path, hops)
  - GET /routing/ZZZ/POL  → 400 (unknown code rejected before the algorithm)
  - no-route case         → 200, found:false, empty path, hops:-1

### BFS — RouteService
Pure-function tests, no web layer (the algorithm is yours, so are these):
  - known multi-hop:     NLD→POL = [NLD, DEU, POL]
  - no land route:       island endpoint → empty
  - origin == destination
  - adjacent endpoints:  single hop

### Tooling
spring-boot-starter-test (JUnit 5 + MockMvc + AssertJ) comes transitively with
the web starter — nothing to add. No Testcontainers, no fixtures, no WireMock.

---

## 8. Maintenance

Dependency and security hygiene is handled by the `dependency-security-hygiene` skill
(`.claude/skills/dependency-security-hygiene/`). Run it periodically — or before a
release — to surface outdated dependencies, end-of-life framework versions, and
known CVEs against `pom.xml`. The skill is read-only: it reports and proposes,
and never edits `pom.xml` without explicit approval.

---