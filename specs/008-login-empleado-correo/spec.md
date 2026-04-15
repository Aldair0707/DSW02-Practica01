# Feature Specification: Login de Empleado por Correo y Contrasena

**Feature Branch**: `008-login-empleado-correo`  
**Created**: 2026-03-26  
**Status**: Draft  
**Input**: User description: "ahora quiero que el tipo de actor empleado pueda iniciar sesion utilizando su correo y contrasena"

## Clarifications

### Session 2026-03-26

- Q: Cual es el alcance funcional de este feature? -> A: Backend + frontend minimo: flujo de login de empleado en UI y consumo real de `/auth/me`.
- Q: Como se maneja la sesion en frontend para actor empleado? -> A: Sesion en memoria de UI, sin persistir credenciales en storage del navegador.
- Q: Que alcance de autorizacion tiene el actor empleado en este feature? -> A: Acceso solo a su perfil autenticado (`SELF`) mediante `/auth/me`.
- Q: Como se expone el flujo de login en frontend para empleado? -> A: Ruta dedicada de login de empleado (ej. `/empleado/login`).
- Q: Que respuesta devuelve el sistema cuando la cuenta de empleado esta inactiva? -> A: Respuesta `401` generica igual que credenciales invalidas; motivo detallado solo en auditoria interna.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Iniciar sesion como empleado en UI (Priority: P1)

Como empleado, quiero iniciar sesion con mi correo y contrasena desde la interfaz para acceder a funcionalidades protegidas.

**Why this priority**: Es el valor principal del feature y habilita el flujo de uso real para actor EMPLEADO.

**Independent Test**: Desde frontend, completar formulario con correo/contrasena validos de un empleado activo y verificar `200` en `/api/v1/empleados/auth/me` con actor `EMPLEADO`.

**Acceptance Scenarios**:

1. **Given** un empleado activo con credenciales validas, **When** inicia sesion en UI, **Then** el sistema autentica y muestra estado autenticado.
2. **Given** un empleado con credenciales invalidas, **When** intenta iniciar sesion, **Then** la UI muestra error generico sin filtrar existencia de cuenta.
3. **Given** un empleado inactivo con credenciales correctas, **When** intenta iniciar sesion, **Then** la UI recibe `401` generico indistinguible de credenciales invalidas.
4. **Given** un empleado autenticado, **When** intenta acceder a recursos administrativos de listado/gestion, **Then** el sistema deniega acceso por alcance `SELF`.
5. **Given** una persona usuaria, **When** accede a `/empleado/login`, **Then** visualiza el formulario dedicado de autenticacion de empleado.

---

### User Story 2 - Mantener separacion de actores (Priority: P2)

Como sistema, quiero distinguir claramente los actores ADMIN y EMPLEADO para aplicar permisos correctos en backend y frontend.

**Why this priority**: Evita regresiones de autorizacion y ambiguedad contractual entre perfiles.

**Independent Test**: Consultar `/api/v1/empleados/auth/me` autenticando como admin y como empleado, validando `actorType` y payload esperado para cada actor.

**Acceptance Scenarios**:

1. **Given** autenticacion admin valida, **When** consulta perfil autenticado, **Then** respuesta identifica actor `ADMIN` sin representarlo como empleado.

---

### User Story 3 - Gobernanza de contrato y pruebas (Priority: P3)

Como equipo, quiero mantener OpenAPI y smoke tests sincronizados para asegurar trazabilidad del feature.

**Why this priority**: Reduce riesgo de drift entre implementacion, contrato y validaciones CI.

**Independent Test**: Ejecutar smoke real de login de empleado y validar contrato OpenAPI actualizado con ejemplos de exito y fallo.

**Acceptance Scenarios**:

1. **Given** el feature implementado, **When** se ejecuta smoke de autenticacion empleado, **Then** pasa con backend real y evidencia reproducible.

### Edge Cases

- Correo con espacios al inicio/fin o mayusculas mixtas.
- Empleado inactivo con credenciales correctas.
- Password incorrecta para correo existente.
- Usuario admin intentando usar flujo de empleado en UI.
- Navegacion directa a ruta de empleado sin autenticar.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST autenticar actor EMPLEADO por correo y contrasena via HTTP Basic.
- **FR-002**: El frontend MUST incluir un flujo minimo de login de empleado (formulario y estado autenticado).
- **FR-003**: El backend MUST exponer perfil autenticado en `GET /api/v1/empleados/auth/me` distinguiendo actor `ADMIN`/`EMPLEADO`.
- **FR-004**: El sistema MUST rechazar credenciales invalidas con respuesta generica y sin enumeracion de cuentas.
- **FR-005**: El sistema MUST permitir autenticacion solo para empleados activos.
- **FR-006**: El contrato OpenAPI MUST incluir ejemplos de login de empleado exitoso y fallido.
- **FR-007**: El feature MUST incluir al menos un smoke test real de login de empleado en frontend.
- **FR-008**: La sesion de frontend para actor EMPLEADO MUST mantenerse en memoria de UI y MUST NOT persistir credenciales en `localStorage` ni `sessionStorage`.
- **FR-009**: El actor EMPLEADO autenticado MUST tener alcance funcional `SELF` y solo acceder a `GET /api/v1/empleados/auth/me` en este feature.
- **FR-010**: El frontend MUST exponer una ruta dedicada para autenticacion de empleado (ej. `/empleado/login`) separada del flujo administrativo.
- **FR-011**: El backend MUST responder `401` generico tanto para credenciales invalidas como para cuenta inactiva y MUST registrar motivo interno de fallo para auditoria sin exponerlo al cliente.

*Additional API governance requirements (when REST applies):*

- **FR-API-VER**: Public REST endpoints MUST include explicit version in path (`/api/v{major}/...`).
- **FR-API-PAG**: Collection/list endpoints MUST support pagination and default to `size=5` when omitted.
- **FR-API-AUTH-DEF**: Local/dev Basic Auth defaults MUST be `admin` (user) and `admin123` (password), with env override support.
- **FR-API-AUTH-PROFILE**: Auth profile endpoints (e.g., `/auth/me`) MUST declare actor type (ADMIN/EMPLEADO or equivalent) and MUST NOT represent admin as empleado.
- **FR-API-EMP-LOGIN**: Employee actor authentication MUST support login with employee email and password, and the contract MUST document success and failure responses.

### Key Entities *(include if feature involves data)*

- **Empleado**: Identidad de negocio autenticable por correo, contrasena hash y estado activo.
- **PerfilAutenticado**: Representacion contractual del actor autenticado con `actorType`, `username`, `displayName`, `permissions` y resumen de empleado cuando aplica.

## Assumptions

- El feature no introduce JWT ni nuevo mecanismo de token; mantiene HTTP Basic existente.
- El frontend no persiste credenciales de empleado en almacenamiento del navegador.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Al menos 95% de intentos de login valido de empleado completan con `200` en `/auth/me` en ambiente de integracion.
- **SC-002**: El 100% de intentos con credenciales invalidas o cuenta inactiva retorna `401` con mensaje generico sin filtrar existencia de cuenta ni estado de activacion.
- **SC-003**: El 100% de respuestas de `/auth/me` distingue correctamente actor `ADMIN` versus `EMPLEADO`.
- **SC-004**: Smoke real de login de empleado pasa en CI y local con backend/frontend levantados.
