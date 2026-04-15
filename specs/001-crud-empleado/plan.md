# Implementation Plan: CRUD de empleado

**Branch**: `001-crud-empleado` | **Date**: 2026-02-26 | **Spec**: `/specs/001-crud-empleado/spec.md`
**Input**: Feature specification from `/specs/001-crud-empleado/spec.md`

## Summary

Implementar un CRUD REST de `Empleado` con validaciones de negocio: `clave` autogenerada en formato `EMP-<autonumérico>` como representación externa de una PK compuesta (`prefijo`, `consecutivo`) e inmutable, y `nombre`, `dirección`, `telefono` con máximo 100 caracteres. La implementación seguirá Spring Boot 3 + Java 17, seguridad HTTP Basic, persistencia PostgreSQL y contrato OpenAPI actualizado.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Validation, Data JPA, Security), springdoc-openapi  
**Storage**: PostgreSQL  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, Testcontainers (PostgreSQL)  
**Target Platform**: Contenedores Docker (Linux) para desarrollo e integración  
**Project Type**: Web service (API REST backend)  
**Performance Goals**: p95 < 200 ms para operaciones CRUD con carga baja/moderada (hasta 50 rps), incluyendo generación de `clave`  
**Constraints**: HTTP Basic obligatorio en rutas de negocio, secretos por variables de entorno, `clave` autogenerada e inmutable, patrón externo `EMP-<autonumérico>`, unicidad por PK compuesta (`prefijo`, `consecutivo`), campos de texto <= 100 caracteres  
**Scale/Scope**: MVP backend monolítico con una entidad principal (`Empleado`) y 5 endpoints CRUD sin permitir clave manual en altas

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Phase 0 Gate Review

- **I. Stack Tecnológico Obligatorio**: PASS. Se define Spring Boot 3 + Java 17.
- **II. Seguridad por Defecto (Basic Auth)**: PASS. API de negocio protegida con HTTP Basic y credenciales default local/dev definidas como `admin`/`admin123` con override por variables de entorno.
- **III. Persistencia en PostgreSQL**: PASS. Modelo y repositorio orientados a PostgreSQL parametrizado por entorno.
- **IV. Entorno Reproducible con Docker**: PASS. Diseño contempla `Dockerfile` + `docker-compose.yml` (app + postgres).
- **V. Contrato API Versionado, Paginación y Documentación Viva**: PASS. Se define versionado de rutas (`/api/v1/...`), paginación en listados con `size=5` por defecto y sincronización de contrato OpenAPI.

Resultado: **Sin violaciones**. Se puede iniciar Phase 0.

### Post-Phase 1 Gate Review

- Modelo de datos definido y consistente con PostgreSQL: PASS.
- Contratos de endpoints CRUD definidos con seguridad y respuestas de error: PASS.
- Configuración y validación de acceso local con `APP_BASIC_USER=admin` y `APP_BASIC_PASSWORD=admin123`: PASS.
- Quickstart incluye ejecución local y en contenedor con variables de entorno: PASS.

Resultado: **Sin violaciones** tras diseño.

## Project Structure

### Documentation (this feature)

```text
specs/001-crud-empleado/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── empleados-openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/.../empleado/
│   │   │   ├── api/
│   │   │   ├── application/
│   │   │   ├── domain/
│   │   │   └── infrastructure/
│   │   └── resources/
│   └── test/
│       ├── java/.../empleado/
│       │   ├── unit/
│       │   ├── integration/
│       │   └── contract/
│       └── resources/
├── Dockerfile
└── pom.xml

docker-compose.yml
```

**Structure Decision**: Se adopta una estructura de web service backend (`backend/`) por alineación con la constitución (Spring Boot + PostgreSQL + Docker) y porque el alcance de esta feature es exclusivamente API REST sin frontend.

## Complexity Tracking

No aplica. No hay violaciones de constitución que requieran justificación.

## Release Scope Notes

- Incluye rutas versionadas `/api/v1/empleados` con seguridad HTTP Basic.
- Incluye listado paginado con `size=5` por defecto cuando el cliente omite el parámetro.
- En alta, cualquier `clave` enviada por cliente se ignora y se genera una clave oficial `EMP-<autonumérico>`.
