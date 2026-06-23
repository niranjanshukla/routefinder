# Route-Finder

An application that works on cca3 data of countries and their land borders, to provide the optimal path between two countries entirely based on land traversal.

# Tech stack

Core framework: Spring Boot 3.4.1, Java 21, Maven 3.9.9, Docker
Libraries: Spring boot starter web, JUnit Jupiter, Mockito
Platform: Claude Code, Skill for vulnerability report under .claude/skills, Claude Code Github Workflow, Claude Code Review (See open issue)
Core algorithm: Graph traversal as BFS
Data structures used: HashMap for storage and look up of cca3 indexes with O(1), Dequeue for graph traversal with O(V+E) for a graph of V vertices and E edges

# Documentation references

Please refer to Claude.md and @planning/PLAN.md for further documentation on scoping and architecture, devops, testing.

# Directory Structure

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
# Execution

On a terminal, run one of the following scripts depending on your platform

To start the Spring boot REST service

```
./scripts/start_mac.sh; (mac os)
./scripts/start_windows.ps1 (windows PowerShell)

```

To stop the service

```
./scripts/stop_mac.sh; (mac os)
./scripts/stop_windows.ps1 (windows PowerShell)

```

# Service Endpoint

Once started, please launch a browser and go to http://localhost:8080/routing/{src}/{dst}

For example, http://localhost:8080/routing/NLD/POL