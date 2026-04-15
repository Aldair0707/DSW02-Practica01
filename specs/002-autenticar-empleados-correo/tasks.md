# Tasks: Autenticacion de empleados por correo y contrasena

**Input**: Design documents from `/specs/002-autenticar-empleados-correo/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/, quickstart.md

**Tests**: No se incluyen tareas de TDD estricto porque no fueron solicitadas explicitamente en la especificacion.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3)
- Cada tarea incluye ruta(s) exacta(s) de archivo

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar base de configuracion del feature y artefactos compartidos.

- [X] T001 Revisar alcance final y clarificaciones del feature en specs/002-autenticar-empleados-correo/spec.md
- [X] T002 [P] Ajustar guia operativa de entorno para credenciales bootstrap en specs/002-autenticar-empleados-correo/quickstart.md
- [X] T003 [P] Confirmar configuracion de datasource y variables de entorno en backend/src/main/resources/application.yml
- [X] T004 [P] Confirmar perfil local/dev y defaults de seguridad en backend/src/main/resources/application-dev.yml

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura bloqueante obligatoria antes de cualquier historia.

**CRITICAL**: Ninguna historia puede iniciar antes de completar esta fase.

- [X] T005 Extender esquema de BD con `correo`, `password_hash`, `activo` y tabla de eventos en backend/src/main/resources/schema.sql
- [X] T006 [P] Extender entidad de persistencia de empleado con correo/passwordHash/activo en backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/EmpleadoEntity.java
- [X] T007 [P] Extender modelo de dominio de empleado con correo y activo en backend/src/main/java/com/dsw02/empleado/domain/Empleado.java
- [X] T008 [P] Agregar busqueda por correo normalizado en backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/EmpleadoRepository.java
- [X] T009 [P] Crear entidad y repositorio de eventos de autenticacion en backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/EventoAutenticacionEntity.java y backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/EventoAutenticacionRepository.java
- [X] T010 [P] Crear normalizador de correo en backend/src/main/java/com/dsw02/empleado/domain/CorreoNormalizer.java
- [X] T011 Definir codigos y excepciones de autenticacion en backend/src/main/java/com/dsw02/empleado/domain/ErrorCode.java y backend/src/main/java/com/dsw02/empleado/domain/exception/
- [X] T012 Configurar PasswordEncoder para credenciales de empleado y mantener bootstrap local/dev en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java
- [X] T013 Implementar UserDetailsService combinado (bootstrap + empleados en BD) en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java
- [X] T014 Ajustar mapeo global de errores para autenticacion y validacion en backend/src/main/java/com/dsw02/empleado/api/GlobalExceptionHandler.java
- [X] T015 [P] Implementar guardrail de alcance para excluir cambio de contrasena en este feature (rechazar campo de cambio de contrasena en update) en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoUpdateRequest.java y backend/src/main/java/com/dsw02/empleado/application/ActualizarEmpleadoService.java
- [X] T016 [P] Asegurar unicidad global de correo en persistencia en backend/src/main/resources/schema.sql y backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/EmpleadoRepository.java
- [X] T017 [P] Mantener versionado de rutas y default de paginacion `size=5` en backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java y backend/src/main/java/com/dsw02/empleado/application/ListarEmpleadosService.java
- [X] T018 [P] Alinear esquema OpenAPI de seguridad global en backend/src/main/java/com/dsw02/empleado/infrastructure/openapi/OpenApiConfig.java

**Checkpoint**: Fundacion lista; historias pueden implementarse.

---

## Phase 3: User Story 1 - Iniciar sesion como empleado (Priority: P1) MVP

**Goal**: Autenticar empleado por correo/contrasena y consultar perfil autenticado protegido.

**Independent Test**: Con credenciales validas y cuenta activa, `GET /api/v1/empleados/auth/me` responde 200 con perfil y correo; sin credenciales, responde 401.

### Implementation for User Story 1

- [X] T019 [P] [US1] Ajustar DTO de alta para requerir correo y contrasena (8-72) en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoCreateRequest.java
- [X] T020 [US1] Persistir correo normalizado y hash de contrasena en alta de empleado en backend/src/main/java/com/dsw02/empleado/application/CrearEmpleadoService.java
- [X] T021 [P] [US1] Crear DTO de perfil autenticado con correo en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoAuthProfileResponse.java
- [X] T022 [US1] Implementar servicio de perfil autenticado por principal en backend/src/main/java/com/dsw02/empleado/application/ObtenerPerfilAutenticadoService.java
- [X] T023 [US1] Exponer endpoint `GET /api/v1/empleados/auth/me` en backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java
- [X] T024 [US1] Garantizar que respuestas generales no incluyan correo ni contrasena en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoResponse.java
- [X] T025 [US1] Actualizar contrato del endpoint auth/me y visibilidad de correo en specs/002-autenticar-empleados-correo/contracts/empleados-auth-openapi.yaml

**Checkpoint**: US1 completa y demostrable de forma independiente.

---

## Phase 4: User Story 2 - Gestionar errores de autenticacion (Priority: P2)

**Goal**: Entregar errores consistentes y seguros para credenciales invalidas y validaciones de entrada.

**Independent Test**: Correo inexistente y contrasena incorrecta devuelven error uniforme; correo/contrasena invalidos en alta devuelven validacion clara.

### Implementation for User Story 2

- [X] T026 [P] [US2] Ajustar DTO de error para codigos de autenticacion y validacion en backend/src/main/java/com/dsw02/empleado/api/dto/ErrorResponse.java
- [X] T027 [US2] Implementar respuesta generica de fallo de autenticacion en backend/src/main/java/com/dsw02/empleado/api/GlobalExceptionHandler.java
- [X] T028 [US2] Validar formato de correo y presencia de contrasena en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoCreateRequest.java
- [X] T029 [P] [US2] Implementar servicio de registro de eventos de autenticacion en backend/src/main/java/com/dsw02/empleado/application/RegistrarEventoAutenticacionService.java
- [X] T030 [US2] Integrar registro de exito/fallo en flujo de autenticacion en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java
- [X] T031 [US2] Actualizar codigos de error y ejemplos en specs/002-autenticar-empleados-correo/contracts/empleados-auth-openapi.yaml

**Checkpoint**: US2 completa y validable sin depender de US3.

---

## Phase 5: User Story 3 - Mantener acceso estable con credenciales vigentes (Priority: P3)

**Goal**: Permitir acceso solo con credenciales validas y cuenta activa, con denegacion inmediata cuando la cuenta este inactiva.

**Independent Test**: Con cuenta inactiva, las mismas credenciales son rechazadas; con cuenta activa, el acceso protegido es permitido.

### Implementation for User Story 3

- [X] T032 [P] [US3] Implementar verificacion de cuenta activa durante autenticacion en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java
- [X] T033 [US3] Implementar gestion de estado de cuenta solo para bootstrap `admin` mediante `PATCH /api/v1/empleados/{clave}/estado`, con validacion de `clave`, control de inexistente/no autorizado e idempotencia (`200` con payload consistente), en backend/src/main/java/com/dsw02/empleado/application/ActualizarEmpleadoService.java, backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java, backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoEstadoRequest.java y backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoEstadoResponse.java
- [X] T034 [US3] Verificar que endpoints de listado mantienen paginacion default `size=5` tras cambios en backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java
- [X] T035 [US3] Actualizar contrato para inactivo y restricciones de visibilidad/autorizacion en specs/002-autenticar-empleados-correo/contracts/empleados-auth-openapi.yaml
- [X] T036 [US3] Actualizar quickstart para flujo permitido/denegado por cuenta activa en specs/002-autenticar-empleados-correo/quickstart.md

**Checkpoint**: US3 completa y consistente con reglas de acceso final.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre transversal, documentación y validación final.

- [X] T037 [P] Sincronizar plan y decisiones finales en specs/002-autenticar-empleados-correo/plan.md y specs/002-autenticar-empleados-correo/research.md
- [X] T038 [P] Alinear modelo de datos y contrato final en specs/002-autenticar-empleados-correo/data-model.md y specs/002-autenticar-empleados-correo/contracts/empleados-auth-openapi.yaml
- [X] T039 [P] Actualizar smoke test de autenticacion de empleados en scripts/smoke/empleados-smoke.sh
- [X] T040 Ejecutar validacion completa de quickstart y registrar evidencia en specs/002-autenticar-empleados-correo/quickstart.md
- [X] T041 [P] Ejecutar prueba de rendimiento de autenticacion y `GET /api/v1/empleados/auth/me` (hasta 50 rps) y registrar evidencia de p95 <= 200 ms en specs/002-autenticar-empleados-correo/quickstart.md
- [X] T042 [P] Definir y ejecutar validacion de primer intento exitoso sobre accion protegida, con evidencia de cumplimiento >= 90% en specs/002-autenticar-empleados-correo/quickstart.md
- [X] T043 [P] Validar matriz de respuestas de `PATCH /api/v1/empleados/{clave}/estado` (`401` no autenticado, `403` no autorizado, `404` no encontrado, `200` idempotente) y reflejar ejemplos en specs/002-autenticar-empleados-correo/contracts/empleados-auth-openapi.yaml y specs/002-autenticar-empleados-correo/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia de inmediato.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias.
- **Phase 3-5 (User Stories)**: dependen de Phase 2 y pueden ejecutarse en paralelo por capacidad.
- **Phase 6 (Polish)**: depende de las historias objetivo completadas.

### User Story Dependencies

- **US1 (P1)**: depende solo de Foundational.
- **US2 (P2)**: depende de Foundational y reutiliza el flujo de autenticacion de US1.
- **US3 (P3)**: depende de Foundational y complementa reglas de estado activo y autorizacion de gestion de estado.

### Story Completion Order (Dependency Graph)

- Orden recomendado: **US1 -> US2 -> US3**.
- Alternativa paralela: completar US1 primero, luego ejecutar US2 y US3 en paralelo.

### Within Each User Story

- DTOs/modelos antes de servicios.
- Servicios antes de endpoints.
- Endpoints antes de contrato y quickstart.

---

## Parallel Opportunities

- **Setup**: T002, T003, T004.
- **Foundational**: T006, T007, T008, T009, T010, T015, T016, T017, T018 tras T005.
- **US1**: T019 y T021 en paralelo; luego T020/T022/T023.
- **US2**: T026 y T029 en paralelo; luego T027/T028/T030/T031.
- **US3**: T032 y T033 en paralelo; luego T034/T035/T036.
- **Polish**: T037, T038, T039, T041, T042, T043 en paralelo; cierre con T040.

---

## Parallel Example: User Story 1

```bash
Task: "T019 [US1] Ajustar DTO de alta para requerir correo y contrasena (8-72) en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoCreateRequest.java"
Task: "T021 [US1] Crear DTO de perfil autenticado con correo en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoAuthProfileResponse.java"
```

## Parallel Example: User Story 2

```bash
Task: "T026 [US2] Ajustar DTO de error para codigos de autenticacion y validacion en backend/src/main/java/com/dsw02/empleado/api/dto/ErrorResponse.java"
Task: "T029 [US2] Implementar servicio de registro de eventos de autenticacion en backend/src/main/java/com/dsw02/empleado/application/RegistrarEventoAutenticacionService.java"
```

## Parallel Example: User Story 3

```bash
Task: "T032 [US3] Implementar verificacion de cuenta activa durante autenticacion en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java"
Task: "T033 [US3] Implementar gestion de estado de cuenta solo para bootstrap admin mediante PATCH /api/v1/empleados/{clave}/estado con validaciones e idempotencia"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar US1 de forma independiente con `GET /api/v1/empleados/auth/me`.

### Incremental Delivery

1. Setup + Foundational.
2. Entregar US1 (autenticacion + perfil autenticado).
3. Entregar US2 (errores seguros + auditoria).
4. Entregar US3 (cuenta activa + unicidad global + consistencia de acceso).
5. Cerrar con polish y validacion de quickstart.

### Suggested MVP Scope

- MVP recomendado: **T001-T025** (hasta cierre de US1).

---

## Notes

- Todas las tareas siguen formato obligatorio de checklist con ID secuencial, etiqueta de paralelizacion opcional y etiqueta de historia cuando aplica.
- Las tareas de historia usan `[US1]`, `[US2]`, `[US3]` para trazabilidad.
- El alcance excluye cambio de contrasena en este feature, conforme a clarificaciones aceptadas.