# Tasks: CRUD de Departamentos y Relacion con Empleados

**Input**: Design documents from /specs/003-crud-departamentos-relacion/
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/, quickstart.md

**Tests**: No se incluyen tareas de TDD porque no fueron solicitadas explicitamente en la especificacion.

**Organization**: Tareas agrupadas por historia de usuario para habilitar implementacion y validacion independiente.

## Format: [ID] [P?] [Story] Description

- [P]: tarea paralelizable (archivos distintos, sin dependencia directa)
- [Story]: US1, US2, US3 para trazabilidad funcional

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar base de trabajo y alineacion documental para el feature.

- [ ] T001 Revisar alcance y clarificaciones en specs/003-crud-departamentos-relacion/spec.md
- [ ] T002 [P] Validar constraints de arquitectura y seguridad en specs/003-crud-departamentos-relacion/plan.md
- [ ] T003 [P] Confirmar escenarios de validacion manual en specs/003-crud-departamentos-relacion/quickstart.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura bloqueante que debe completarse antes de cualquier historia.

**CRITICAL**: Ninguna historia puede iniciar antes de completar esta fase.

- [X] T004 Extender esquema para Departamento y referencia en Empleado en backend/src/main/resources/schema.sql
- [X] T004A [P] Asegurar que `nombre` de departamento no tenga restriccion unica en backend/src/main/resources/schema.sql
- [X] T005 [P] Crear entidad de persistencia Departamento en backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/DepartamentoEntity.java
- [X] T006 [P] Crear clave compuesta de Departamento en backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/DepartamentoId.java
- [X] T007 [P] Crear repositorio de Departamento con busqueda por clave en backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/DepartamentoRepository.java
- [X] T008 [P] Extender EmpleadoEntity con departamentoClave en backend/src/main/java/com/dsw02/empleado/infrastructure/persistence/EmpleadoEntity.java
- [X] T009 [P] Extender Empleado de dominio con departamentoClave en backend/src/main/java/com/dsw02/empleado/domain/Empleado.java
- [X] T010 [P] Implementar generador de clave DEP-XXXX en backend/src/main/java/com/dsw02/empleado/domain/DepartamentoClaveGenerator.java
- [X] T011 [P] Implementar parser/normalizador de clave DEP en backend/src/main/java/com/dsw02/empleado/domain/DepartamentoClaveParser.java
- [X] T012 [P] Agregar codigos de error de departamento en backend/src/main/java/com/dsw02/empleado/domain/ErrorCode.java
- [X] T013 [P] Agregar excepciones de dominio de departamento en backend/src/main/java/com/dsw02/empleado/domain/exception/DepartamentoNotFoundException.java
- [X] T014 [P] Agregar excepcion de conflicto de eliminacion en backend/src/main/java/com/dsw02/empleado/domain/exception/DepartamentoConEmpleadosException.java
- [X] T015 Ajustar mapeo de errores HTTP 404 y 409 en backend/src/main/java/com/dsw02/empleado/api/GlobalExceptionHandler.java

**Checkpoint**: Fundacion lista; historias habilitadas.

---

## Phase 3: User Story 1 - Crear y mantener departamentos (Priority: P1)

**Goal**: Entregar CRUD completo de departamentos con clave de negocio autogenerada e inmutable.

**Independent Test**: Crear, listar, obtener, actualizar y eliminar departamento sin empleados asociados cumpliendo validaciones de nombre y formato de clave.

### Implementation for User Story 1

- [X] T016 [P] [US1] Crear DTO request de alta de departamento en backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoCreateRequest.java
- [X] T017 [P] [US1] Crear DTO request de actualizacion de departamento en backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoUpdateRequest.java
- [X] T018 [P] [US1] Crear DTO response de departamento en backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoResponse.java
- [X] T019 [P] [US1] Crear DTO paginado de departamentos en backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoPageResponse.java
- [X] T020 [US1] Implementar servicio de creacion de departamento en backend/src/main/java/com/dsw02/empleado/application/CrearDepartamentoService.java
- [X] T021 [US1] Implementar servicio de listado paginado de departamentos en backend/src/main/java/com/dsw02/empleado/application/ListarDepartamentosService.java
- [X] T022 [US1] Implementar servicio de consulta por clave de departamento en backend/src/main/java/com/dsw02/empleado/application/ObtenerDepartamentoService.java
- [X] T023 [US1] Implementar servicio de actualizacion de departamento en backend/src/main/java/com/dsw02/empleado/application/ActualizarDepartamentoService.java
- [X] T024 [US1] Implementar servicio de eliminacion de departamento en backend/src/main/java/com/dsw02/empleado/application/EliminarDepartamentoService.java
- [X] T025 [US1] Exponer endpoints CRUD versionados en backend/src/main/java/com/dsw02/empleado/api/DepartamentoController.java
- [X] T026 [US1] Asegurar paginacion default size=5 en listado de departamentos en backend/src/main/java/com/dsw02/empleado/api/DepartamentoController.java

**Checkpoint**: US1 implementable y validable de forma independiente.

---

## Phase 4: User Story 2 - Asignar empleados a departamentos (Priority: P2)

**Goal**: Permitir asignar/cambiar departamento de empleado mediante endpoint dedicado y migrar historicos sin departamento.

**Independent Test**: Asignar departamento existente a empleado, rechazar departamento inexistente con 404 y verificar que alta inicial no permite setear manualmente departamentoClave.

### Implementation for User Story 2

- [X] T027 [P] [US2] Crear DTO request de asignacion de departamento de empleado en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoDepartamentoRequest.java
- [X] T028 [US2] Implementar servicio de asignacion/cambio de departamento de empleado en backend/src/main/java/com/dsw02/empleado/application/AsignarDepartamentoEmpleadoService.java
- [X] T029 [US2] Exponer endpoint PATCH versionado de asignacion en backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java
- [X] T030 [US2] Validar rechazo 404 para departamento inexistente en backend/src/main/java/com/dsw02/empleado/application/AsignarDepartamentoEmpleadoService.java
- [X] T031 [US2] Bloquear asignacion manual de departamentoClave en alta de empleado en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoCreateRequest.java
- [X] T032 [US2] Mantener flujo de alta ignorando/invalidando departamentoClave en backend/src/main/java/com/dsw02/empleado/application/CrearEmpleadoService.java
- [X] T033 [US2] Implementar migracion de historicos a DEP-0000 Sin asignar en backend/src/main/resources/schema.sql
- [X] T034 [US2] Incluir departamentoClave en respuesta general de empleado en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoResponse.java

**Checkpoint**: US2 implementable y validable sin depender de US3.

---

## Phase 5: User Story 3 - Consultar relacion departamento-empleados (Priority: P3)

**Goal**: Consultar composicion de departamentos sin embeber empleados en detalle y con endpoint dedicado para listado por departamento.

**Independent Test**: Obtener detalle de departamento sin lista embebida y listar empleados por departamento de forma paginada.

### Implementation for User Story 3

- [X] T035 [P] [US3] Implementar servicio de listado de empleados por departamento en backend/src/main/java/com/dsw02/empleado/application/ListarEmpleadosPorDepartamentoService.java
- [X] T036 [US3] Exponer endpoint GET /api/v1/departamentos/{clave}/empleados en backend/src/main/java/com/dsw02/empleado/api/DepartamentoController.java
- [X] T037 [US3] Asegurar detalle de departamento sin empleados embebidos en backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoResponse.java
- [X] T038 [US3] Aplicar paginacion default size=5 en listado de empleados por departamento en backend/src/main/java/com/dsw02/empleado/api/DepartamentoController.java

**Checkpoint**: US3 implementable y validable de forma independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre transversal de contrato, documentación y verificación final.

- [ ] T039 [P] Actualizar contrato OpenAPI de la feature en specs/003-crud-departamentos-relacion/contracts/departamentos-openapi.yaml
- [ ] T040 [P] Sincronizar reglas de modelo y decisiones finales en specs/003-crud-departamentos-relacion/data-model.md y specs/003-crud-departamentos-relacion/research.md
- [ ] T041 [P] Actualizar quickstart con flujo final validado en specs/003-crud-departamentos-relacion/quickstart.md
- [ ] T042 Ejecutar suite de pruebas y registrar resultado en specs/003-crud-departamentos-relacion/quickstart.md
- [ ] T043 [P] Validar acceso Basic Auth con defaults `admin`/`admin123` y registrar evidencia en specs/003-crud-departamentos-relacion/quickstart.md
- [ ] T044 [P] Medir y registrar cumplimiento de SC-002 (>=95% CRUD exitoso en primer intento con datos validos) en specs/003-crud-departamentos-relacion/quickstart.md
- [ ] T045 [P] Definir y ejecutar validacion de SC-005 (>=90% usuarios internos en <=30s) con evidencia en specs/003-crud-departamentos-relacion/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- Phase 1 (Setup): inicia de inmediato.
- Phase 2 (Foundational): depende de Phase 1 y bloquea historias.
- Phase 3 (US1), Phase 4 (US2), Phase 5 (US3): dependen de Phase 2.
- Phase 6 (Polish): depende de historias objetivo completadas.

### User Story Dependencies

- US1: depende solo de Foundational.
- US2: depende de Foundational y reutiliza estructuras de Empleado y Departamento.
- US3: depende de Foundational y de endpoints/base de Departamento implementados en US1.

### Story Completion Order

- Orden recomendado: US1 -> US2 -> US3.
- Orden paralelo posible: completar US1 y luego ejecutar US2 y US3 en paralelo.

### Within Each User Story

- DTOs y modelos antes de servicios.
- Servicios antes de controladores.
- Endpoints y validaciones antes de cierre documental.

### Parallel Opportunities

- Setup: T002, T003.
- Foundational: T005-T014 en paralelo tras T004.
- US1: T016-T019 en paralelo; luego T020-T026.
- US2: T027 y T031 en paralelo; luego T028-T034.
- US3: T035 y T037 en paralelo; luego T036 y T038.
- Polish: T039-T041 en paralelo; cierre tecnico con T042 y evidencias de calidad T043-T045.

---

## Parallel Example: User Story 1

- T016 [US1] Crear DTO request de alta de departamento en backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoCreateRequest.java
- T017 [US1] Crear DTO request de actualizacion de departamento en backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoUpdateRequest.java
- T018 [US1] Crear DTO response de departamento en backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoResponse.java
- T019 [US1] Crear DTO paginado de departamentos en backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoPageResponse.java

## Parallel Example: User Story 2

- T027 [US2] Crear DTO request de asignacion de departamento de empleado en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoDepartamentoRequest.java
- T031 [US2] Bloquear asignacion manual de departamentoClave en alta de empleado en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoCreateRequest.java

## Parallel Example: User Story 3

- T035 [US3] Implementar servicio de listado de empleados por departamento en backend/src/main/java/com/dsw02/empleado/application/ListarEmpleadosPorDepartamentoService.java
- T037 [US3] Asegurar detalle de departamento sin empleados embebidos en backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoResponse.java

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1.
2. Completar Phase 2.
3. Completar Phase 3 (US1).
4. Validar CRUD de departamentos en aislamiento.

### Incremental Delivery

1. Setup + Foundational.
2. Entregar US1 (CRUD departamento).
3. Entregar US2 (asignacion y migracion de empleados).
4. Entregar US3 (consulta relacional por departamento).
5. Cerrar con Polish y evidencia de validacion.

### Suggested MVP Scope

- MVP recomendado: T001-T026.

---

## Notes

- Todas las tareas siguen formato obligatorio con checkbox, ID secuencial y ruta de archivo.
- Las tareas de historia incluyen etiqueta [US1], [US2], [US3].
- Los endpoints contemplados permanecen versionados bajo /api/v1/.
