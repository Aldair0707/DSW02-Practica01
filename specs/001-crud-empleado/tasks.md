# Tasks: CRUD de empleado

**Input**: Design documents from `/specs/001-crud-empleado/`
**Prerequisites**: `plan.md` (required), `spec.md` (required for user stories), `research.md`, `data-model.md`, `contracts/`, `quickstart.md`

**Tests**: No se incluyen tareas de TDD estricto porque no fueron solicitadas explícitamente en la especificación.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Cada tarea incluye ruta(s) exacta(s) de archivo

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Inicialización del servicio backend y base de configuración del proyecto.

- [X] T001 Crear estructura base del backend en `backend/src/main/java/com/dsw02/empleado/` y `backend/src/test/java/com/dsw02/empleado/`
- [X] T002 Inicializar proyecto Spring Boot con dependencias requeridas en `backend/pom.xml`
- [X] T003 [P] Configurar propiedades de aplicación y datasource en `backend/src/main/resources/application.yml`
- [X] T004 [P] Configurar perfil de desarrollo en `backend/src/main/resources/application-dev.yml`
- [X] T005 [P] Definir variables de entorno de referencia en `.env.example`
- [X] T006 [P] Configurar orquestación local inicial en `docker-compose.yml`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura obligatoria previa a cualquier historia.

**⚠️ CRITICAL**: Ninguna historia puede iniciar antes de completar esta fase.

- [X] T007 Configurar autenticación HTTP Basic y rutas públicas mínimas en `backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java`
- [X] T008 [P] Configurar credenciales por defecto local/dev (`admin`/`admin123`) con override por entorno en `backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java`
- [X] T009 [P] Configurar esquema de seguridad OpenAPI en `backend/src/main/java/com/dsw02/empleado/infrastructure/openapi/OpenApiConfig.java`
- [X] T010 [P] Modelar PK compuesta (`prefijo`, `consecutivo`) en `backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/EmpleadoId.java` y `backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/EmpleadoEntity.java`
- [X] T011 [P] Implementar acceso a persistencia y secuencia autonumérica en `backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/EmpleadoRepository.java` y `backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/ConsecutivoRepository.java`
- [X] T012 [P] Implementar parseo/formato de clave y errores de dominio en `backend/src/main/java/com/dsw02/empleado/domain/ClaveParser.java`, `backend/src/main/java/com/dsw02/empleado/domain/ErrorCode.java` y `backend/src/main/java/com/dsw02/empleado/domain/exception/`
- [X] T013 Implementar manejo global de errores HTTP en `backend/src/main/java/com/dsw02/empleado/api/GlobalExceptionHandler.java`
- [X] T014 [P] Definir DTOs base de request/response en `backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoCreateRequest.java`, `backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoUpdateRequest.java`, `backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoResponse.java` y `backend/src/main/java/com/dsw02/empleado/api/dto/ErrorResponse.java`
- [X] T015 [P] Preparar secuencia de consecutivo en `backend/src/main/resources/schema.sql`
- [X] T016 Alinear contrato OpenAPI base con versionado y paginación por defecto en `specs/001-crud-empleado/contracts/empleados-openapi.yaml`

**Checkpoint**: Base técnica lista para implementar historias de usuario.

---

## Phase 3: User Story 1 - Registrar empleado (Priority: P1) 🎯 MVP

**Goal**: Dar de alta empleados con clave autogenerada `EMP-<autonumérico>`, ignorando cualquier `clave` enviada por cliente.

**Independent Test**: Crear empleado válido (201) y verificar clave generada; reenviar alta con `clave` en payload y verificar que se ignora.

### Implementation for User Story 1

- [X] T017 [P] [US1] Implementar modelo de dominio `Empleado` en `backend/src/main/java/com/dsw02/empleado/domain/Empleado.java`
- [X] T018 [US1] Implementar servicio de creación con clave autogenerada e ignorando `request.clave` en `backend/src/main/java/com/dsw02/empleado/application/CrearEmpleadoService.java`
- [X] T019 [P] [US1] Aplicar validaciones de longitud para alta en `backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoCreateRequest.java`
- [X] T020 [US1] Implementar endpoint `POST /api/v1/empleados` en `backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java`
- [X] T021 [US1] Documentar comportamiento de alta (clave autogenerada e ignorar clave enviada) en `backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java` y `specs/001-crud-empleado/contracts/empleados-openapi.yaml`

**Checkpoint**: US1 funcional y demostrable de forma independiente.

---

## Phase 4: User Story 2 - Consultar empleados (Priority: P2)

**Goal**: Consultar por clave y listar empleados con paginación (`page`, `size`) y `size=5` por defecto.

**Independent Test**: Consultar por clave existente/no existente y listar sin `size` verificando aplicación de `size=5`.

### Implementation for User Story 2

- [X] T022 [P] [US2] Implementar consulta por clave en `backend/src/main/java/com/dsw02/empleado/application/ObtenerEmpleadoService.java`
- [X] T023 [P] [US2] Implementar listado paginado con default `size=5` en `backend/src/main/java/com/dsw02/empleado/application/ListarEmpleadosService.java`
- [X] T024 [US2] Implementar endpoint `GET /api/v1/empleados/{clave}` en `backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java`
- [X] T025 [US2] Implementar endpoint `GET /api/v1/empleados?page={page}&size={size}` con default `size=5` en `backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java`
- [X] T026 [US2] Crear DTO de respuesta paginada en `backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoPageResponse.java`
- [X] T027 [US2] Actualizar contrato de listado paginado y parámetros por defecto en `specs/001-crud-empleado/contracts/empleados-openapi.yaml`

**Checkpoint**: US2 funcional de forma independiente y alineada con paginación requerida.

---

## Phase 5: User Story 3 - Actualizar y eliminar empleado (Priority: P3)

**Goal**: Actualizar `nombre`, `direccion`, `telefono` y eliminar por `clave`, manteniendo inmutabilidad de `clave`.

**Independent Test**: Actualizar y eliminar empleado existente; update/delete sobre inexistente retorna 404.

### Implementation for User Story 3

- [X] T028 [P] [US3] Implementar actualización con bloqueo de modificación de `clave` en `backend/src/main/java/com/dsw02/empleado/application/ActualizarEmpleadoService.java`
- [X] T029 [P] [US3] Implementar eliminación por clave en `backend/src/main/java/com/dsw02/empleado/application/EliminarEmpleadoService.java`
- [X] T030 [US3] Implementar endpoint `PUT /api/v1/empleados/{clave}` en `backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java`
- [X] T031 [US3] Implementar endpoint `DELETE /api/v1/empleados/{clave}` en `backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java`
- [X] T032 [US3] Reforzar mapeo de errores para no encontrado y formato inválido en `backend/src/main/java/com/dsw02/empleado/api/GlobalExceptionHandler.java`

**Checkpoint**: CRUD completo con las tres historias funcionales.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Endurecimiento final, documentación operativa y validación transversal.

- [X] T033 [P] Actualizar quickstart con credenciales Basic Auth por defecto y ejemplos paginados en `specs/001-crud-empleado/quickstart.md`
- [X] T034 [P] Actualizar script de smoke para cubrir create/get/list paginado/delete en `scripts/smoke/empleados-smoke.sh`
- [ ] T035 [P] Validar build de contenedor con resultado verificable (imagen construye, servicio arranca y `health` responde `UP`) en `backend/Dockerfile` y `docker-compose.yml`
- [ ] T036 Validar configuración final de servicios y variables en `docker-compose.yml`
- [ ] T037 Ejecutar validación de quickstart y registrar notas finales en `specs/001-crud-empleado/quickstart.md`
- [X] T038 [P] Documentar decisiones finales y alcance de release en `specs/001-crud-empleado/plan.md`
- [ ] T039 [P] Definir escenario de prueba de rendimiento (CRUD + listado paginado) en `scripts/perf/empleados-perf.sh`
- [ ] T040 Ejecutar prueba de rendimiento y verificar objetivo `p95 < 200 ms` hasta `50 rps`; registrar evidencia en `specs/001-crud-empleado/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia de inmediato.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias.
- **Phase 3-5 (User Stories)**: dependen de Phase 2; pueden ir por prioridad o en paralelo por capacidad de equipo.
- **Phase 6 (Polish)**: depende de las historias objetivo completadas e incluye validación de performance con evidencia de cumplimiento de NFR.

### User Story Dependencies

- **US1 (P1)**: depende solo de Foundational.
- **US2 (P2)**: depende solo de Foundational; puede desarrollarse en paralelo con US1 pero su demo completa requiere datos de alta.
- **US3 (P3)**: depende solo de Foundational; utiliza componentes compartidos ya disponibles.

### Within Each User Story

- DTOs/modelos antes de servicios.
- Servicios antes de endpoints.
- Endpoints antes de actualización contractual/documental.

---

## Parallel Opportunities

- **Setup**: `T003`, `T004`, `T005`, `T006` en paralelo tras `T002`.
- **Foundational**: `T008`, `T009`, `T010`, `T011`, `T012`, `T014`, `T015` en paralelo tras `T007`.
- **US1**: `T017` y `T019` en paralelo antes de integrar `T018` y `T020`.
- **US2**: `T022` y `T023` en paralelo; luego `T024` y `T025`.
- **US3**: `T028` y `T029` en paralelo; luego `T030` y `T031`.
- **Polish**: `T033`, `T034`, `T035`, `T038` en paralelo.

---

## Parallel Example: User Story 2

```bash
# Ejecutar en paralelo por dos desarrolladores:
Task: "T022 [US2] Implementar consulta por clave en backend/src/main/java/com/dsw02/empleado/application/ObtenerEmpleadoService.java"
Task: "T023 [US2] Implementar listado paginado con default size=5 en backend/src/main/java/com/dsw02/empleado/application/ListarEmpleadosService.java"

# Integrar endpoints después de ambos servicios:
Task: "T024 [US2] Implementar endpoint GET /api/v1/empleados/{clave} en backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java"
Task: "T025 [US2] Implementar endpoint GET /api/v1/empleados?page={page}&size={size} en backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar alta con clave autogenerada e ignorar `clave` enviada.
5. Publicar demo interna de MVP.

### Incremental Delivery

1. Setup + Foundational.
2. Entregar US1 (alta).
3. Entregar US2 (consulta + listado paginado).
4. Entregar US3 (actualización + eliminación).
5. Cerrar con polish y validación quickstart.

### Suggested MVP Scope

- MVP recomendado: **US1 solamente** (`T001` a `T021`).

---

## Notes

- Todas las tareas cumplen formato obligatorio: `- [ ] T### [P?] [US?] Descripción con ruta de archivo`.
- Las tareas de historia incluyen etiqueta `[US1]`, `[US2]`, `[US3]`.
- Las tareas son lo suficientemente específicas para ejecución directa por agente/LLM.
