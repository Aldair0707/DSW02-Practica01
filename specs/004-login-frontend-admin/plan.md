# Implementation Plan: Login Frontend con Admin

**Branch**: `004-login-frontend-admin` | **Date**: 2026-03-18 | **Spec**: `/specs/004-login-frontend-admin/spec.md`
**Input**: Feature specification from `/specs/004-login-frontend-admin/spec.md`

## Summary

Entregar login frontend correcto contra Basic Auth del backend (incluyendo credenciales por defecto `admin/admin123` en entorno local), con estados de sesion consistentes, manejo de errores de autenticacion/conectividad y acceso habilitado a vistas protegidas de empleados/departamentos sin cambiar contratos backend existentes.

## Technical Context

**Language/Version**: TypeScript 5.9, SCSS, HTML (Angular 21.2.x en este entorno; Angular 22 no disponible en npm al momento)  
**Primary Dependencies**: Angular standalone APIs, `@angular/common/http`, `@angular/forms`, RxJS  
**Storage**: N/A para persistencia nueva (sesion solo en memoria de UI)  
**Testing**: Angular TestBed/Vitest (unit), verificacion manual end-to-end con backend local  
**Target Platform**: Navegador moderno en desarrollo local (macOS/Linux/Windows)
**Project Type**: Web application (frontend Angular + backend Spring Boot)  
**Performance Goals**: Login y carga inicial del panel en <= 2 segundos con backend local operativo  
**Constraints**: Basic Auth obligatorio, usar rutas versionadas `/api/v1/...`, no crear endpoints backend nuevos, mantener paginacion existente (`size=5` default backend), no persistir credenciales en almacenamiento inseguro  
**Scale/Scope**: 1 pantalla de autenticacion + control de sesion para 2 vistas principales (empleados/departamentos) en frontend existente

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Phase 0 Gate Review

- **I. Stack Tecnologico Obligatorio**: PASS. No se altera backend Spring Boot 3 + Java 17.
- **II. Seguridad por Defecto (Basic Auth)**: PASS. El frontend consume Basic Auth y valida credenciales default `admin`/`admin123` en local.
- **III. Persistencia en PostgreSQL**: PASS. No hay cambios de persistencia ni modelo backend.
- **IV. Entorno Reproducible con Docker**: PASS. Se mantiene flujo local con backend en Docker y frontend por `ng serve`.
- **V. Contrato API Versionado, Paginacion y Documentacion Viva**: PASS. Se consumen rutas `/api/v1/...` existentes, con listados paginados actuales; no hay cambios de contrato.

Resultado: **Sin violaciones**. Phase 0 habilitada.

### Post-Phase 1 Gate Review

- Research define estrategia de sesion frontend en memoria y manejo de errores `401/403` sin alterar backend: PASS.
- Data model define entidades de UI (`CredencialesLogin`, `SesionFrontend`, `PerfilAutenticado`) alineadas al contrato existente: PASS.
- Contrato funcional frontend documenta consumo de endpoints versionados y continuidad de paginacion backend: PASS.
- Quickstart incluye validacion explicita de login con `admin/admin123` y errores de autenticacion: PASS.

Resultado: **Sin violaciones** tras diseno.

## Project Structure

### Documentation (this feature)

```text
specs/004-login-frontend-admin/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── frontend-login-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/
│   ├── main/java/com/dsw02/empleado/
│   └── resources/
└── src/test/java/com/dsw02/empleado/integration/

frontend/
├── src/
│   ├── app/
│   ├── main.ts
│   └── styles.scss
├── proxy.conf.json
└── package.json

docker-compose.yml
scripts/smoke/
specs/
```

**Structure Decision**: Se mantiene estructura web app existente (`frontend/` + `backend/`) y la feature se implementa solo en frontend, consumiendo los endpoints ya disponibles en backend.

## Complexity Tracking

No aplica. No hay violaciones constitucionales que requieran excepcion.

## Implementation Validation Evidence

- Date: 2026-03-19
- Branch: `004-login-frontend-admin`
- Command: `cd frontend && npm test -- --watch=false`
	- Result: PASS (5/5 tests)
- Command: `cd frontend && npm run build`
	- Result: PASS (bundle generated in `frontend/dist/frontend`)
- Command: `./scripts/quality/verify-no-backend-endpoints-or-contract-changes.sh`
	- Result: PASS (sin cambios de endpoints backend ni contratos OpenAPI YAML)
