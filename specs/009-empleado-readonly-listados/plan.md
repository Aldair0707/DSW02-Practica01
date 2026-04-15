# Implementation Plan: Vista de Empleado Solo Lectura

**Branch**: `009-empleado-readonly-listados` | **Date**: 2026-03-26 | **Spec**: /specs/009-empleado-readonly-listados/spec.md
**Input**: Feature specification from `/specs/009-empleado-readonly-listados/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Habilitar al actor EMPLEADO autenticado el acceso de solo lectura a los listados de empleados y departamentos reutilizando endpoints versionados existentes (`/api/v1/empleados`, `/api/v1/departamentos`), manteniendo paginacion (`size=5` por defecto), restringiendo CRUD con `403 Forbidden`, y reflejando el alcance de permisos tanto en backend como en frontend (ocultar/deshabilitar acciones de escritura).

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: Java 17 (backend), TypeScript 5.9 (frontend Angular 21)  
**Primary Dependencies**: Spring Boot 3.x, Spring Security (HTTP Basic), Spring Data JPA, springdoc-openapi, Angular standalone APIs, Angular HttpClient, RxJS, Cypress 15  
**Storage**: PostgreSQL (sin nuevos cambios de esquema obligatorios para esta feature)  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc, Vitest, Cypress E2E, smoke scripts bash  
**Target Platform**: Servicios backend en contenedor Linux + SPA Angular en navegador moderno
**Project Type**: Web application (backend + frontend)  
**Performance Goals**: p95 <= 300ms para listados paginados y validaciones de permisos en entorno local; carga inicial de vista empleado con ambos listados <= 5s en entorno local de desarrollo  
**Constraints**: Reutilizar endpoints existentes (sin rutas nuevas exclusivas para empleado), permitir solo `GET` para actor EMPLEADO en empleados/departamentos, responder `403` en escrituras denegadas, mantener `size=5` por defecto  
**Scale/Scope**: 1 nuevo flujo de consulta para empleado autenticado, cambios en autorizacion backend + presentacion frontend + contratos y pruebas de regresion

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Gate inicial (antes de Phase 0)

- I. Stack Tecnologico Obligatorio: PASS. Se mantiene backend Spring Boot 3 + Java 17 y frontend Angular existente.
- II. Seguridad por Defecto (Basic Auth + Login de Empleado): PASS. Se mantiene HTTP Basic y se extiende alcance de EMPLEADO con permisos de solo lectura.
- III. Persistencia en PostgreSQL: PASS. No se cambia motor ni estrategia de persistencia.
- IV. Entorno Reproducible con Docker: PASS. Validacion planteada con `docker compose` y smoke tests.
- V. Contrato API Versionado, Paginacion, Identidad de Actor, Alcance de Permisos y Documentacion Viva: PASS. Rutas siguen versionadas `/api/v1/...`, listados con `size=5`, y se documenta explicitamente denegacion de CRUD para EMPLEADO.

Resultado: PASS. Sin violaciones constitucionales; habilita investigacion y diseno.

### Re-check post-diseno (tras Phase 1)

- Versionado en ruta API: PASS. Se mantienen endpoints versionados existentes.
- Paginacion default: PASS. Listados de empleados/departamentos mantienen `size=5` por defecto.
- Basic Auth local/dev defaults: PASS. `admin` / `admin123` se conserva con override por entorno.
- Login de empleado por correo+contrasena: PASS. Se reutiliza contrato existente de autenticacion.
- Distincion ADMIN/EMPLEADO en perfil: PASS. `/api/v1/empleados/auth/me` sigue declarando actor.
- Alcance de permisos EMPLEADO: PASS. Solo lecturas permitidas y escrituras con `403`.
- OpenAPI/contratos sincronizados: PASS. Se define contrato de solo lectura por actor para endpoints impactados.

Resultado: PASS. Sin excepciones ni deuda de gobernanza.

<!--
  If feature includes REST endpoints, constitution checks MUST explicitly verify:
  - API versioning in route path (`/api/v{major}/...`)
  - List endpoints define pagination with default `size=5`
  - Basic Auth defaults for local/dev are `admin` / `admin123` (overridable by env)
  - Employee actor login by email+password is defined and validated when feature touches authentication
  - Employee actor authorization scope is read-only for empleados/departamentos listings (no CRUD)
  - Auth profile endpoints (e.g. `/auth/me`) distinguish ADMIN vs EMPLEADO actors in contract
  - OpenAPI reflects route version and pagination parameters
-->

## Project Structure

### Documentation (this feature)

```text
specs/009-empleado-readonly-listados/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->

```text
backend/
├── src/
│   ├── main/java/com/dsw02/empleado/
│   │   ├── api/
│   │   ├── application/
│   │   ├── domain/
│   │   └── infrastructure/security/
│   ├── main/resources/
│   └── test/java/com/dsw02/empleado/
│       ├── integration/
│       └── api/

frontend/
├── src/
│   └── app/
│       ├── core/
│       │   ├── auth/
│       │   ├── http/
│       │   └── models/
│       └── features/
│           ├── admin/
│           └── empleado/
└── cypress/e2e/

scripts/
└── smoke/
```

**Structure Decision**: Se usa la estructura web app existente (backend + frontend) y se evita crear servicios o apps nuevas. El cambio se concentra en reglas de seguridad backend, proyecciones de lectura para actor EMPLEADO y composicion de UI en `features/empleado` para visualizacion de listados sin CRUD.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | No aplica | No aplica |
