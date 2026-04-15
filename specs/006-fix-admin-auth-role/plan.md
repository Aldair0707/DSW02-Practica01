# Implementation Plan: Corregir Rol Admin En Login

**Branch**: `006-fix-admin-auth-role` | **Date**: 2026-03-25 | **Spec**: `/specs/006-fix-admin-auth-role/spec.md`
**Input**: Feature specification from `/specs/006-fix-admin-auth-role/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Corregir el flujo de autenticacion/autorizacion para que el principal bootstrap `admin` sea tratado como actor administrativo valido en `/api/v1/empleados/auth/me`, mantener frontera de permisos entre ADMIN y EMPLEADO, y formalizar la gobernanza de listados (`page`, `size`, default `size=5`) bajo versionado `/api/v1` para endpoints actuales y futuros del dominio admin.

## Technical Context

**Language/Version**: Java 17 (backend), TypeScript 5.9 (frontend Angular 21)  
**Primary Dependencies**: Spring Boot 3.3.x, Spring Security (HTTP Basic), Spring Data JPA, PostgreSQL driver, Angular standalone APIs, RxJS  
**Storage**: PostgreSQL (sin cambios de esquema obligatorios para esta feature)  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc + Spring Security Test; pruebas de frontend con TestBed/Vitest; smoke E2E por script bash  
**Target Platform**: Web app local/dev sobre Docker Compose (app + postgres) en macOS/Linux  
**Project Type**: Web application (backend + frontend)  
**Performance Goals**: Login/admin profile y primer listado administrativo en <= 2s en entorno local  
**Constraints**: Mantener Basic Auth; preservar rutas `/api/v1/...`; distinguir 401 vs 403 con codigos funcionales fijos; conservar `size=5` por defecto en listados  
**Scale/Scope**: Ajuste acotado a autenticacion/autorizacion admin, contrato `auth/me`, y gobernanza de listados de administracion

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Phase 0 Gate Review

- **I. Stack Tecnologico Obligatorio**: PASS. Se mantiene Spring Boot 3 + Java 17.
- **II. Seguridad por Defecto (Basic Auth)**: PASS. Se mantienen Basic Auth y defaults `admin`/`admin123` con override por entorno.
- **III. Persistencia en PostgreSQL**: PASS. No se altera la tecnologia de persistencia.
- **IV. Entorno Reproducible con Docker**: PASS. Verificacion con `docker compose` y smoke script.
- **V. Contrato API Versionado, Paginacion, Perfil de Acceso y Documentacion Viva**: PASS con accion de diseno: consolidar contrato de `auth/me`, confirmar 3 listados minimos con `page/size` default `size=5`, y dejar trazabilidad de ajuste OpenAPI asociado.

Resultado: **Sin violaciones**. Fase 0 habilitada.

### Post-Phase 1 Gate Review

- Research formaliza decisiones de alcance (FR-009 fuera de scope de esta feature): PASS.
- Data model define actor de autenticacion y errores funcionales `AUTH_INVALIDA`/`NO_AUTORIZADO`: PASS.
- Contract incluye `auth/me` y gobernanza de listados versionados con `size=5` por defecto: PASS.
- OpenAPI canónico del dominio autenticación/empleados se mantiene en `specs/002-autenticar-empleados-correo/contracts/empleados-auth-openapi.yaml`; esta feature 006 actualiza ese archivo para evitar duplicación de fuentes de verdad: PASS.
- Quickstart valida casos admin/empleado, 401/403 y endpoints de listado versionados: PASS.

Resultado: **Sin violaciones** tras diseno.

## Project Structure

### Documentation (this feature)

```text
specs/006-fix-admin-auth-role/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── auth-profile-admin-role-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/dsw02/empleado/
│   ├── api/
│   ├── application/
│   ├── domain/
│   └── infrastructure/
├── src/main/resources/
└── src/test/java/com/dsw02/empleado/integration/

frontend/
├── src/app/
│   ├── core/
│   ├── app.ts
│   └── app.html
└── package.json

scripts/
├── quality/
└── smoke/

docker-compose.yml
```

**Structure Decision**: Se mantiene arquitectura actual web monorepo (backend Spring + frontend Angular). La feature se concentra en backend (contrato y seguridad) con ajustes de consumo frontend y pruebas/instrumentacion de validacion.

## Complexity Tracking

No aplica. No hay violaciones constitucionales que requieran excepcion.

## Deferred Scope Tracking

- FR-009 (trazabilidad de rechazos administrativos) queda diferido en esta feature 006.
- Referencia de continuidad: crear feature posterior dedicada a auditoria de rechazos de acceso (correlativo a definir por el equipo) para agregar persistencia/log estructurado sin ampliar el alcance actual.
