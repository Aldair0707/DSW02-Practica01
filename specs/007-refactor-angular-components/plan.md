# Implementation Plan: Refactor Angular Con Componentes

**Branch**: `007-refactor-angular-components` | **Date**: 2026-03-25 | **Spec**: `/specs/007-refactor-angular-components/spec.md`
**Input**: Feature specification from `/specs/007-refactor-angular-components/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Refactorizar el frontend administrativo Angular desde una pantalla monolítica hacia arquitectura por componentes y contenedores por feature, preservando comportamiento actual de login/CRUD, reutilizando formularios/tablas/feedback y aplicando una estrategia E2E híbrida con 2 smoke reales obligatorios por PR (login+navegación y crear+listar Empleados).

## Technical Context

**Language/Version**: TypeScript 5.9 + Angular 21  
**Primary Dependencies**: Angular standalone APIs, RxJS, Angular HttpClient, Angular Forms, Cypress 15  
**Storage**: N/A (frontend consume API backend existente en Spring Boot + PostgreSQL)  
**Testing**: Karma/Jasmine (`ng test`) + Cypress E2E (smoke real y regresión con stubs)  
**Target Platform**: Web SPA en navegadores modernos (desktop/mobile)
**Project Type**: Web application (frontend Angular + backend Spring existente)  
**Performance Goals**: Mantener UX fluida en dashboard admin y evitar degradación perceptible en navegación/carga CRUD  
**Constraints**: No cambiar contratos backend, mantener compatibilidad con Basic Auth local (`admin`/`admin123`), no romper flujo actual, gate de PR con 2 smoke reales en verde  
**Scale/Scope**: Refactor de auth + empleados + departamentos en `frontend/src/app`, sin cambios funcionales de backend

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

Pre-Phase 0 gate evaluation:

- API versioning (`/api/v{major}/...`): PASS. La feature no agrega ni modifica rutas REST.
- List endpoints default `size=5`: PASS. Se mantiene consumo existente sin alterar semántica.
- Basic Auth defaults local/dev (`admin` / `admin123`): PASS. Se conserva como baseline de integración.
- Auth profile actor distinction (`ADMIN` vs `EMPLEADO`): PASS. Contrato de `/auth/me` preservado y validado en pruebas.
- OpenAPI updated when contract changes: PASS por no existir cambios de contrato HTTP.

Post-Phase 1 re-check:

- PASS. Diseño de componentes y estrategia de pruebas no introduce violaciones constitucionales.
- PASS. Los smoke reales obligatorios refuerzan, no contradicen, los principios de seguridad e integración.

## Project Structure

### Documentation (this feature)

```text
specs/007-refactor-angular-components/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── frontend-component-architecture-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/main/java/
└── src/test/java/

frontend/
├── src/
│   └── app/
│       ├── app.ts
│       ├── app.html
│       ├── app.routes.ts
│       └── core/
├── cypress/
│   ├── e2e/
│   └── support/
└── cypress.config.ts
```

**Structure Decision**: Se mantiene la estructura web application existente, concentrando cambios en `frontend/src/app` para modularización y en `frontend/cypress` para estrategia E2E híbrida.

## Complexity Tracking

No se registran violaciones de constitución que requieran justificación.
