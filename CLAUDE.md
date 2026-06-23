# Route Finder Project

All project documentation is in the `planning` directory.

The key document is PLAN.md included in full below. The remainder of the app is still to be developed.

@planning/PLAN.md

## Working agreement — scope boundary

Build all of the scaffolding described in PLAN.md, with ONE exception below.
The generated project must compile and boot, and the controller wiring around
the exception must be complete and correct.

**You implement:**
- `pom.xml`, `Dockerfile`, `.dockerignore`, `application.yml`
- `CountryApiApplication.java`, `Country.java`, `CountryStore.java`
- `RoutingController.java` — full request mapping, path-variable binding,
  HTTP 400 validation, and the `RouteResult` response shape
- Tests for `CountryStore` and `RoutingController` (mock the routing call)

**Please do NOT implement — leave for me:**
- The body of `RouteService.findRoute(origin, destination)`. Leave it exactly
  as a stub that throws `UnsupportedOperationException` with a `// TODO` marker.
  
  Do not implement the BFS.
- Any unit tests for `RouteService` / the BFS algorithm.

If a task requires implementing `findRoute`, stop at the stub and say so
rather than filling it.