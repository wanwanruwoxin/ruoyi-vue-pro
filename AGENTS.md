# Repository Guidelines

## Project Structure & Module Organization
This repository is a multi-module Maven project. Core backend modules live under `yudao-framework/` and `yudao-module-*/`, and the runnable backend entrypoint is `yudao-server/`. SQL schemas for multiple databases are in `sql/` (e.g., `sql/mysql/`, `sql/postgresql/`, `sql/sqlserver/`). Frontend code is not embedded here; `yudao-ui/` contains placeholders/README files for separate UI repos.

Key module layout:
- `yudao-framework/` shared infrastructure, common utilities, base components
- `yudao-module-system/`, `yudao-module-infra/`, `yudao-module-*` business modules
- `yudao-server/` Spring Boot application that assembles modules

To enable optional modules, uncomment dependencies in `yudao-server/pom.xml` and module entries in `pom.xml`.

## Build, Test, and Development Commands
Backend build (all enabled modules):
```bash
mvn clean package
```
Run the server locally:
```bash
mvn -pl yudao-server -am spring-boot:run
```
Run a single module’s tests:
```bash
mvn -pl yudao-module-system -am test
```
Configuration is typically managed in `yudao-server/src/main/resources/application-*.yaml` (e.g., `application-local.yaml`).

## Coding Style & Naming Conventions
Use standard Java 17 conventions with 4-space indentation. Lombok and MapStruct are used heavily; keep data objects and converters consistent with existing patterns.
Naming patterns are widely used:
- `*DO` for data objects, `*VO` for request/response objects, `*DTO` for cross-module transfer
- `*Service`, `*ServiceImpl`, `*Mapper` follow Spring/MyBatis conventions

## Testing Guidelines
JUnit 5 is the default test framework (Maven Surefire). Tests live in `src/test/java` with fixtures in `src/test/resources`.
Name tests with `*Test` or `*Tests`, mirroring existing modules.

## Commit & Pull Request Guidelines
Git history indicates a conventional style such as `feat(scope): ...` and `fix(scope): ...`, sometimes using Chinese punctuation (e.g., `feat（iot）：...`). Follow that style for consistency.
PRs should include a brief summary, list of impacted modules, and test results. Add screenshots if UI artifacts are involved.

## Security & Configuration Tips
Do not commit secrets. Use local profiles (e.g., `application-local.yaml`) for dev-only settings and keep credentials in environment-specific configs.
