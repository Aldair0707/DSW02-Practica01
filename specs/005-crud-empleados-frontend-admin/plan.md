# Implementation Plan: CRUD Empleados Desde Admin

**Branch**: `005-crud-empleados-frontend-admin` | **Date**: 2026-03-19 | **Spec**: `/specs/005-crud-empleados-frontend-admin/spec.md`
**Input**: Feature specification from `/specs/005-crud-empleados-frontend-admin/spec.md`

## Summary

Implementar en frontend un flujo administrativo completo para empleados con acceso autenticado, listado paginado y operaciones de alta/edicion/baja, incorporando las clarificaciones funcionales acordadas: correo unico global, contrasena opcional en edicion (si vacia conserva la actual), departamento opcional con estado sin asignar, y sesion sin temporizador por inactividad en esta iteracion.

## Technical Context

**Language/Version**: TypeScript 5.9, HTML, SCSS (Angular 21.2.x en este entorno)  
**Primary Dependencies**: Angular standalone APIs, `@angular/common/http`, `@angular/forms`, RxJS  
**Storage**: N/A para persistencia nueva; sesion y estado de UI en memoria  
**Testing**: Angular TestBed/Vitest (unit), validacion manual end-to-end con backend local  
**Target Platform**: Navegador moderno en entorno local/desarrollo
**Project Type**: Web application (frontend Angular + backend Spring Boot)  
**Performance Goals**: login y primera carga del listado de empleados en <= 2 segundos con backend local operativo  
**Constraints**: Basic Auth obligatorio, no crear endpoints backend nuevos, no modificar contratos OpenAPI backend, respetar versionado `/api/v1/...` y paginacion backend con default `size=5`, bloquear entradas vacias/espacios/formato invalido  
**Scale/Scope**: 1 modulo administrativo de empleados (login + CRUD + paginacion) en frontend existente

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Phase 0 Gate Review

- **I. Stack Tecnologico Obligatorio**: PASS. No se altera backend Spring Boot 3 + Java 17.
- **II. Seguridad por Defecto (Basic Auth)**: PASS. Se mantiene Basic Auth y credenciales default local `admin`/`admin123` como baseline.
- **III. Persistencia en PostgreSQL**: PASS. Sin cambios de persistencia backend.
- **IV. Entorno Reproducible con Docker**: PASS. Backend sigue operable con Docker Compose.
- **V. Contrato API Versionado, Paginacion y Documentacion Viva**: PASS. Solo consumo de endpoints existentes `/api/v1/...`, listados paginados y sin cambios de contrato.

Resultado: **Sin violaciones**. Fase 0 habilitada.

### Post-Phase 1 Gate Review

- Research resuelve decisiones de sesion, validaciones y reglas de formulario sin ampliar alcance backend: PASS.
- Data model incorpora correo unico global, contrasena opcional en edicion y departamento opcional: PASS.
- Contract funcional documenta comportamiento frontend sobre endpoints existentes versionados/paginados: PASS.
- Quickstart valida casos funcionales y de error alineados a clarificaciones: PASS.

Resultado: **Sin violaciones** tras diseno.

## Project Structure

### Documentation (this feature)

```text
specs/005-crud-empleados-frontend-admin/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── frontend-empleados-admin-crud-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/dsw02/empleado/
├── src/main/resources/
└── src/test/java/com/dsw02/empleado/integration/

frontend/
├── src/app/
│   ├── app.ts
│   ├── app.html
│   ├── app.scss
│   └── core/
├── src/styles.scss
├── proxy.conf.json
└── package.json

scripts/
├── quality/
└── smoke/

docker-compose.yml
specs/
```

**Structure Decision**: Se conserva la arquitectura web app actual (`frontend` + `backend`), implementando esta feature unicamente en frontend y reutilizando APIs backend existentes.

## Complexity Tracking

No aplica. No hay violaciones constitucionales que requieran excepcion.
