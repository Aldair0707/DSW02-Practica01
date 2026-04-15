# Implementation Plan: CRUD de Departamentos y Relacion con Empleados

**Branch**: `003-crud-departamentos-relacion` | **Date**: 2026-03-15 | **Spec**: `/specs/003-crud-departamentos-relacion/spec.md`
**Input**: Feature specification from `/specs/003-crud-departamentos-relacion/spec.md`

## Summary

Implementar CRUD completo de departamentos con clave de negocio autogenerada `DEP-XXXX`, integrar relacion 1:N con empleados via `departamentoClave`, exponer endpoints REST versionados en `/api/v1/...`, y asegurar reglas de integridad: no asignar departamentos inexistentes (404), no eliminar departamentos con empleados (409), y migrar historicos sin departamento al departamento tecnico `DEP-0000` (`Sin asignar`).

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Validation, Data JPA, Security), springdoc-openapi  
**Storage**: PostgreSQL  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, Testcontainers (PostgreSQL)  
**Target Platform**: Contenedores Docker (Linux) para desarrollo e integracion
**Project Type**: Web service (API REST backend)  
**Performance Goals**: p95 <= 250 ms para operaciones CRUD de departamento y asignacion de departamento de empleado en carga baja/moderada (hasta 50 rps)  
**Constraints**: HTTP Basic obligatorio; defaults local/dev `admin`/`admin123`; listados con paginacion y `size=5` por defecto; rutas versionadas `/api/v1/...`; contrato OpenAPI actualizado; sin ruptura del CRUD actual de empleado  
**Scale/Scope**: Feature incremental sobre backend existente, 5 endpoints de departamento + 2 endpoints relacionales de empleado/departamento

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Phase 0 Gate Review

- **I. Stack Tecnologico Obligatorio**: PASS. Se mantiene Spring Boot 3 + Java 17.
- **II. Seguridad por Defecto (Basic Auth)**: PASS. Endpoints de negocio protegidos por Basic Auth; defaults de desarrollo `admin`/`admin123` se respetan.
- **III. Persistencia en PostgreSQL**: PASS. Entidades y relacion se modelan en PostgreSQL.
- **IV. Entorno Reproducible con Docker**: PASS. Flujo compatible con `Dockerfile` y `docker-compose.yml` existentes.
- **V. Contrato API Versionado, Paginacion y Documentacion Viva**: PASS. Endpoints definidos en `/api/v1/...`, listados paginados con default `size=5`, contrato OpenAPI a actualizar en `contracts`.

Resultado: **Sin violaciones**. Phase 0 habilitada.

### Post-Phase 1 Gate Review

- Data model incorpora `Departamento` y `departamentoClave` en `Empleado` con reglas de validacion e integridad referencial: PASS.
- Contrato OpenAPI incluye rutas versionadas, paginacion (`page`, `size`, `size=5`), codigos 404/409 y endpoint dedicado de asignacion: PASS.
- Diseño contempla bloqueo de eliminacion de departamento con empleados y migracion de historicos a `DEP-0000`: PASS.
- Quickstart valida autenticacion Basic, endpoints versionados y escenarios de error de negocio: PASS.

Resultado: **Sin violaciones** tras diseno.

## Project Structure

### Documentation (this feature)

```text
specs/003-crud-departamentos-relacion/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── departamentos-openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/com/dsw02/empleado/
│   │   │   ├── api/
│   │   │   ├── application/
│   │   │   ├── domain/
│   │   │   └── infrastructure/
│   │   └── resources/
│   └── test/java/com/dsw02/empleado/integration/
├── pom.xml
└── Dockerfile

docker-compose.yml
scripts/smoke/
```

**Structure Decision**: Se mantiene la arquitectura backend existente por capas y se extiende en los mismos paquetes para minimizar riesgo de regresion y conservar consistencia con features 001 y 002.

## Complexity Tracking

No aplica. No hay violaciones constitucionales que requieran excepcion.
