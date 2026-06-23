---
name: dependency-security-hygiene
description: Audit Maven dependencies, plugins for outdated versions, end-of-life frameworks, and any known security vulnerabilities. Use this skill whenever the user asks to check, audit, review, or update dependencies, mentions old libraries, the pom.xml, Spring Boot or Java version, security concerns, or "dependency hygiene" — even if they don't explicitly say "audit". The skill reports and proposes, it does not edit pom.xml on its own.
---

# Dependency Security Hygiene

Audits a Maven project's dependencies and plugins, then reports what's outdated or vulnerable. This skill **never edits `pom.xml` without explicit confirmation** — it surfaces findings and proposes an exact change for the developer to approve.

## Steps

1. **Inventory.** Resolve the full tree so transitive deps are visible:
   ```
   mvn -q dependency:tree
   ```

2. **Find available updates.** Uses the versions plugin via its fully-qualified
   goal:
   ```
   mvn -q versions:display-dependency-updates
   mvn -q versions:display-plugin-updates
   ```

3. **Check framework currency.** Determine whether the Spring Boot line and the
   Java version are still in active/maintained support. Versions move, so verify against the current release/support timeline (web search if available) rather than from memory — flag anything past its supported window as a priority.

4. **Check for known vulnerabilities.** For a quick pass, cross-reference the resolved Spring Boot version against published security advisories. For a thorough scan (heavier — downloads the NVD database on first run), offer:
   ```
   mvn -q org.owasp:dependency-check-maven:check
   ```
   Note : Only run the OWASP scan if the user asks for it.

## Generated Report format

Group findings by priority — **security first**, then major, then minor/patch:

| Dependency | Current | Latest | Type | Note |
|---|---|---|---|---|
| ... | ... | ... | security / major / minor | e.g. CVE-XXXX, or "EOL — upgrade advised" |

End with a short recommendation. Major-version bumps are flagged as "review needed" (they can break things), not applied. If the user approves a change, show the exact `pom.xml` diff and wait for a clear yes before editing.

## Boundary

This skill is diagnostic. It does not modify `pom.xml`, lockfiles, or any source unless the user explicitly approves a specific proposed change.
