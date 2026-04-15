# Implementation Plan: Login de Empleado por Correo y Contrasena

**Branch**: `008-login-empleado-correo` | **Date**: 2026-03-26 | **Spec**: /specs/008-login-empleado-correo/spec.md
**Input**: Feature specification from `/specs/008-login-empleado-correo/spec.md`

## Summary

Implementar un flujo dedicado de login para actor EMPLEADO en frontend (`/empleado/login`) y reutilizar autenticacion HTTP Basic existente en backend con validacion real contra `GET /api/v1/empleados/auth/me`. El alcance exige sesion en memoria de UI, permiso `SELF` para empleado, respuesta generica `401` tanto para credenciales invalidas como cuenta inactiva y sincronizacion de contrato OpenAPI + smoke E2E real.

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: Java 17 (backend), TypeScript 5.9 (frontend Angular 21)  
**Primary Dependencies**: Spring Boot 3.x, Spring Security (HTTP Basic), Spring Data JPA, Angular standalone APIs, RxJS, Angular HttpClient, Cypress 15  
**Storage**: PostgreSQL (backend), sesion de frontend en memoria de UI (sin persistencia)  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc, Vitest, Cypress E2E  
**Target Platform**: API backend en contenedor Linux + SPA Angular en navegador moderno
**Project Type**: Web application (backend + frontend)  
**Performance Goals**: p95 <= 200 ms para autenticacion/perfil en carga baja-moderada (hasta 50 rps), smoke E2E de login empleado < 30s  
**Constraints**: respuesta generica `401` para credenciales invalidas e inactivo; actor EMPLEADO alcance `SELF`; no JWT nuevo; ruta dedicada `/empleado/login`  
**Scale/Scope**: 1 flujo nuevo de login empleado en frontend, sin cambios de modelo persistente mayor

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Gate inicial (antes de investigacion)

- I. Stack Tecnologico Obligatorio: PASS. Se mantiene Spring Boot 3 + Java 17 y Angular existente.
- II. Seguridad por Defecto (Basic Auth + Login de Empleado): PASS. El flujo usa HTTP Basic y habilita login de empleado por correo+contrasena.
- III. Persistencia en PostgreSQL: PASS. No se cambia motor ni estrategia de persistencia.
- IV. Entorno Reproducible con Docker: PASS. Validaciones ejecutables sobre stack docker-compose.
- V. Contrato API Versionado, Paginacion, Identidad de Actor y Documentacion Viva: PASS. Endpoint versionado `/api/v1/empleados/auth/me`; contrato se actualiza con actor y errores.

Resultado: sin violaciones; fase de investigacion habilitada.

### Re-check post-diseno

- Versionado API: PASS (`/api/v1/...` sin endpoints no versionados).
- Paginacion default size=5 en listados: PASS (sin regresion en listados admin existentes).
- Defaults Basic Auth local/dev: PASS (`admin`/`admin123` configurable por entorno).
- Login empleado correo+contrasena definido y validado: PASS (UI dedicada + consumo real de `/auth/me`).
- Distincion ADMIN/EMPLEADO en `/auth/me`: PASS (contrato y pruebas).
- OpenAPI sincronizado: PASS (artifact contrato incluido en feature).

Resultado: sin violaciones tras diseno.

<!--
  If feature includes REST endpoints, constitution checks MUST explicitly verify:
  - API versioning in route path (`/api/v{major}/...`)
  - List endpoints define pagination with default `size=5`
  - Basic Auth defaults for local/dev are `admin` / `admin123` (overridable by env)
  - Employee actor login by email+password is defined and validated when feature touches authentication
  - Auth profile endpoints (e.g. `/auth/me`) distinguish ADMIN vs EMPLEADO actors in contract
  - OpenAPI reflects route version and pagination parameters
-->

## Project Structure

### Documentation (this feature)

```text
specs/008-login-empleado-correo/
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
│   │   └── infrastructure/
│   └── test/java/com/dsw02/empleado/integration/
└── src/main/resources/

frontend/
├── src/app/
│   ├── app.routes.ts
│   ├── core/
│   └── features/
└── cypress/e2e/

scripts/
└── smoke/
```

**Structure Decision**: Se usa estructura web app existente con backend y frontend. Backend mantiene seguridad/autenticacion en capa infrastructure y API contractual en capa api. Frontend agrega ruta y contenedor dedicado para actor empleado para evitar mezclar flujo admin y empleado.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | No aplica | No aplica |
