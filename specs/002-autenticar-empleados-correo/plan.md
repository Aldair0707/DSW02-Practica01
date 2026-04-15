# Implementation Plan: Autenticacion de empleados por correo y contrasena

**Branch**: `002-autenticar-empleados-correo` | **Date**: 2026-03-12 | **Spec**: `/specs/002-autenticar-empleados-correo/spec.md`
**Input**: Feature specification from `/specs/002-autenticar-empleados-correo/spec.md`

## Summary

Implementar autenticacion de empleados por correo/contrasena usando HTTP Basic sobre Spring Security, con control de cuenta activa, respuesta de error generica para evitar enumeracion de cuentas, auditoria de intentos y gestion de estado de cuenta por actor autorizado (`admin` bootstrap). El alcance incluye `GET /api/v1/empleados/auth/me`, ajuste de alta de empleado con contrasena inicial y contrato OpenAPI actualizado. Cambio de contrasena queda fuera de alcance.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Validation, Data JPA, Security), springdoc-openapi  
**Storage**: PostgreSQL  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, Testcontainers (PostgreSQL)  
**Target Platform**: Contenedores Docker (Linux) para desarrollo e integracion
**Project Type**: Web service (API REST backend)  
**Performance Goals**: p95 <= 200 ms para autenticacion y consulta de perfil autenticado en carga baja/moderada (hasta 50 rps)  
**Constraints**: HTTP Basic obligatorio; defaults local/dev `admin`/`admin123`; password en hash; correo visible solo en `GET /api/v1/empleados/auth/me`; cambio de contrasena fuera de alcance; `PATCH /api/v1/empleados/{clave}/estado` solo para `admin` bootstrap  
**Scale/Scope**: MVP incremental sobre backend existente; extension de modelo `Empleado` y auditoria de autenticacion

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Phase 0 Gate Review

- **I. Stack Tecnologico Obligatorio**: PASS. Se mantiene Spring Boot 3 + Java 17.
- **II. Seguridad por Defecto (Basic Auth)**: PASS. Rutas de negocio bajo HTTP Basic; defaults `admin`/`admin123` configurables por entorno.
- **III. Persistencia en PostgreSQL**: PASS. Datos de credenciales (hash), estado y auditoria se modelan en PostgreSQL.
- **IV. Entorno Reproducible con Docker**: PASS. Flujo soportado por `Dockerfile` + `docker-compose.yml`.
- **V. Contrato API Versionado, Paginacion y Documentacion Viva**: PASS. Endpoints versionados `/api/v1/...`, listados con `size=5` por defecto y OpenAPI actualizado.

Resultado: **Sin violaciones**. Phase 0 habilitada.

### Post-Phase 1 Gate Review

- Modelo de datos contempla correo unico global, `passwordHash`, `activo` y evento auditable con campos minimos: PASS.
- Contrato OpenAPI documenta `auth/me`, reglas de visibilidad de correo y matriz de errores de estado de cuenta: PASS.
- Restriccion de actor autorizado (`admin` bootstrap) para `PATCH /estado` reflejada en diseno y contrato: PASS.
- Paginacion por defecto (`size=5`) en listados versionados se preserva: PASS.
- Quickstart cubre validaciones de acceso permitido/denegado y escenarios de contrato: PASS.

Resultado: **Sin violaciones** tras diseno.

## Project Structure

### Documentation (this feature)

```text
specs/002-autenticar-empleados-correo/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── empleados-auth-openapi.yaml
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

**Structure Decision**: Se mantiene arquitectura monolito backend existente y se extiende por capas (`api`, `application`, `domain`, `infrastructure`) para minimizar riesgo de regresion y respetar lineamientos del proyecto.

## Complexity Tracking

No aplica. No hay violaciones constitucionales que requieran excepcion.
