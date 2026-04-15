# Tasks: Vista de Empleado Solo Lectura

**Input**: Design documents from `/specs/009-empleado-readonly-listados/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: No se definen tareas de test-first (TDD) porque no fueron solicitadas explícitamente; sí se incluyen tareas de validación funcional y ejecución de suites existentes al cierre.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar artefactos base y puntos de entrada para la feature sin tocar aún reglas de negocio de historias.

- [X] T001 Alinear objetivos técnicos y alcance en `specs/009-empleado-readonly-listados/plan.md`
- [X] T002 [P] Consolidar decisiones de diseño en `specs/009-empleado-readonly-listados/research.md`
- [X] T003 [P] Confirmar proyecciones de datos readonly en `specs/009-empleado-readonly-listados/data-model.md`
- [X] T004 [P] Actualizar contrato API base de la feature en `specs/009-empleado-readonly-listados/contracts/empleado-readonly-openapi.yaml`
- [X] T005 [P] Actualizar contrato UI readonly en `specs/009-empleado-readonly-listados/contracts/frontend-empleado-readonly-ui-contract.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura de autorización y contratos compartidos que bloquean todas las historias.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T006 Definir matriz de acceso por actor/metodo para empleados y departamentos en `backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java`
- [X] T007 [P] Introducir DTO paginado readonly de empleados en `backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoReadOnlyPageResponse.java`
- [X] T008 [P] Introducir DTO readonly de item empleado en `backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoReadOnlyResponse.java`
- [X] T009 [P] Introducir DTO paginado readonly de departamentos en `backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoReadOnlyPageResponse.java`
- [X] T010 [P] Introducir DTO readonly de item departamento en `backend/src/main/java/com/dsw02/empleado/api/dto/DepartamentoReadOnlyResponse.java`
- [X] T011 Definir mapeo de identificador de dominio (`clave`) a contrato (`id`) en `backend/src/main/java/com/dsw02/empleado/api/dto/`
- [X] T012 [P] Adaptar modelo frontend readonly para empleados/departamentos en `frontend/src/app/core/models/empleado.models.ts`
- [X] T013 [P] Preparar utilitario de errores de autorización para actor empleado en `frontend/src/app/core/http/api-error.util.ts`

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Ver listados al iniciar sesion (Priority: P1) 🎯 MVP

**Goal**: Permitir que empleado autenticado consulte listados de empleados y departamentos tras login.

**Independent Test**: Iniciar sesión como empleado y confirmar visualización de ambos listados paginados sin depender del flujo admin.

### Implementation for User Story 1

- [X] T014 [US1] Habilitar `GET /api/v1/empleados` para actor EMPLEADO conservando paginación en `backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java`
- [X] T015 [US1] Habilitar `GET /api/v1/departamentos` para actor EMPLEADO conservando paginación en `backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java`
- [X] T016 [US1] Exponer proyección readonly de empleados para actor EMPLEADO en `backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java`
- [X] T017 [US1] Exponer proyección readonly de departamentos para actor EMPLEADO en `backend/src/main/java/com/dsw02/empleado/api/DepartamentoController.java`
- [X] T018 [P] [US1] Añadir consumo de listados readonly en fachada de empleado en `frontend/src/app/core/auth/empleado-auth.facade.ts`
- [X] T019 [P] [US1] Añadir llamadas paginadas de consulta para flujo empleado en `frontend/src/app/core/http/empleados-api.service.ts`
- [X] T020 [US1] Renderizar listados de empleados y departamentos tras login en `frontend/src/app/features/empleado/empleado-login.container.html`
- [X] T021 [US1] Gestionar estado de carga/paginación de listados en `frontend/src/app/features/empleado/empleado-login.container.ts`

**Checkpoint**: User Story 1 funcional y demostrable (empleado ve ambos listados)

---

## Phase 4: User Story 2 - Operar en modo solo lectura (Priority: P2)

**Goal**: Garantizar que empleado no pueda ejecutar operaciones CRUD sobre empleados/departamentos.

**Independent Test**: Con sesión de empleado activa, intentar operaciones de escritura y verificar denegación `403` en backend y ausencia de acciones de escritura en UI.

### Implementation for User Story 2

- [X] T022 [US2] Restringir `POST/PUT/PATCH/DELETE` a rol ADMIN en `backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java`
- [X] T023 [US2] Asegurar respuesta de acceso denegado `403` para escrituras de EMPLEADO en `backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java`
- [X] T024 [US2] Ajustar contrato de operaciones denegadas para EMPLEADO en `specs/009-empleado-readonly-listados/contracts/empleado-readonly-openapi.yaml`
- [X] T025 [P] [US2] Ocultar/deshabilitar acciones de crear/editar/eliminar en vista empleado en `frontend/src/app/features/empleado/empleado-login.container.html`
- [X] T026 [P] [US2] Blindar handlers de escritura no permitida en flujo empleado en `frontend/src/app/features/empleado/empleado-login.container.ts`
- [X] T027 [US2] Alinear contrato UI readonly con reglas de no-CRUD en `specs/009-empleado-readonly-listados/contracts/frontend-empleado-readonly-ui-contract.md`

**Checkpoint**: User Story 2 completa (EMPLEADO solo lectura, sin CRUD)

---

## Phase 5: User Story 3 - Mensajes claros ante acciones no permitidas (Priority: P3)

**Goal**: Mostrar feedback entendible cuando una acción es rechazada por permisos.

**Independent Test**: Forzar acción restringida en sesión empleado y validar mensaje claro de autorización denegada.

### Implementation for User Story 3

- [X] T028 [US3] Normalizar mensaje de `403` para actor EMPLEADO en `frontend/src/app/core/http/api-error.util.ts`
- [X] T029 [US3] Mostrar mensaje contextual de solo lectura en `frontend/src/app/features/empleado/empleado-login.container.html`
- [X] T030 [US3] Gestionar estado de error de permisos en `frontend/src/app/features/empleado/empleado-login.container.ts`
- [X] T031 [US3] Documentar comportamiento de mensajes en `specs/009-empleado-readonly-listados/quickstart.md`

**Checkpoint**: User Story 3 completa (mensajería clara en denegaciones)

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de consistencia documental y validación integral de la feature.

- [X] T032 [P] Sincronizar decisiones finales de arquitectura y autorización en `specs/009-empleado-readonly-listados/research.md`
- [X] T033 [P] Verificar y ajustar criterios de salida operativos en `specs/009-empleado-readonly-listados/quickstart.md`
- [X] T034 Ejecutar validación funcional API/UI siguiendo `specs/009-empleado-readonly-listados/quickstart.md`
- [ ] T035 Ejecutar regresión backend en `backend/` con `./mvnw test`
- [X] T036 Ejecutar regresión frontend en `frontend/` con `npm run test -- --watch=false`
- [X] T037 Validar contrato de perfil autenticado por actor (`ADMIN`/`EMPLEADO`) en `backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java`
- [X] T038 Verificar defaults de Basic Auth (`admin`/`admin123`) y override por entorno en `docker-compose.yml`
- [X] T039 Ejecutar regresión del flujo login empleado correo+contrasena con `frontend/cypress/e2e/empleado-login-smoke.cy.ts`
- [X] T040 Verificar versionado obligatorio `/api/v1/...` en endpoints y contrato OpenAPI en `specs/009-empleado-readonly-listados/contracts/empleado-readonly-openapi.yaml`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: Depend on Foundational completion
- **Polish (Phase 6)**: Depends on user stories completion

### User Story Dependencies

- **US1 (P1)**: Empieza tras Phase 2; define base funcional de listados para empleado
- **US2 (P2)**: Depende de US1 para validar restricciones sobre mismo flujo de listados
- **US3 (P3)**: Depende de US2 para mostrar mensajes sobre denegaciones reales

### Within Each User Story

- Backend authorization and API behavior before frontend integration
- Data projection/contracts before UI rendering
- UI states before cross-cutting validation

### Parallel Opportunities

- T002, T003, T004, T005 en paralelo (documentación/contratos)
- T007, T008, T009, T010 en paralelo (DTOs readonly)
- T012 y T013 en paralelo (modelos/utilidades frontend)
- T018 y T019 en paralelo (consumo frontend)
- T025 y T026 en paralelo (bloqueo UI escritura)
- T032 y T033 en paralelo (cierre documental)

---

## Parallel Example: User Story 1

```bash
# Implementacion paralela de consumo frontend (US1)
Task: "T018 [US1] Añadir consumo de listados readonly en frontend/src/app/core/auth/empleado-auth.facade.ts"
Task: "T019 [US1] Añadir llamadas paginadas de consulta para flujo empleado en frontend/src/app/core/http/empleados-api.service.ts"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup)
2. Completar Phase 2 (Foundational)
3. Completar Phase 3 (US1)
4. Validar flujo end-to-end de listados para EMPLEADO

### Incremental Delivery

1. Entregar MVP con visibilidad de listados (US1)
2. Endurecer autorización readonly (US2)
3. Mejorar UX de errores de permisos (US3)
4. Ejecutar polish y regresión final

### Parallel Team Strategy

1. Dev A: backend security + controllers (T014-T017, T022-T024)
2. Dev B: frontend estado/listados readonly (T018-T021, T025-T030)
3. Dev C: contratos + quickstart + validación de cierre (T004-T005, T031-T040)

---

## Notes

- Todas las tareas cumplen formato checklist requerido (`- [ ] Txxx ...`).
- Las tareas `[P]` evitan conflicto directo de archivo o dependencia incompleta.
- Los paths apuntan a archivos reales del workspace o a archivos nuevos definidos en plan/data-model/contracts.
- No se incluyen tareas de test-first obligatorias porque no hubo solicitud explícita de TDD.
