# Tasks: Login Frontend con Admin

**Input**: Design documents from `/specs/004-login-frontend-admin/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/, quickstart.md

**Tests**: No se generan tareas de pruebas automatizadas en esta iteracion porque la especificacion no exige TDD ni suites nuevas; se incluyen validaciones funcionales en quickstart.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar la base del frontend para separar autenticacion, estado de sesion y manejo de errores.

- [X] T001 Crear estructura base de autenticacion frontend en `frontend/src/app/core/auth/auth-session.service.ts`
- [X] T002 [P] Definir modelos de autenticacion y perfil en `frontend/src/app/core/models/auth.models.ts`
- [X] T003 [P] Definir utilidades de cabecera Basic Auth en `frontend/src/app/core/auth/basic-auth.util.ts`
- [X] T004 [P] Definir utilidades de parseo de errores HTTP en `frontend/src/app/core/http/api-error.util.ts`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura obligatoria previa a cualquier historia de usuario.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T005 Integrar `AuthSessionService` en el bootstrap de app en `frontend/src/app/app.config.ts`
- [X] T006 [P] Configurar flujo de proxy API para desarrollo en `frontend/proxy.conf.json`
- [X] T007 [P] Ajustar comandos de ejecucion con proxy en `frontend/package.json`
- [X] T008 Implementar estado global de sesion (autenticado/no autenticado, perfil, feedback) en `frontend/src/app/app.ts`
- [X] T009 Preparar layout base de pantalla de login y contenedor protegido en `frontend/src/app/app.html`
- [X] T010 Ajustar estilos base de estados de autenticacion en `frontend/src/app/app.scss`

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Login exitoso de administrador (Priority: P1) 🎯 MVP

**Goal**: Permitir acceso al sistema desde frontend con `admin/admin123` y cargar panel protegido.

**Independent Test**: Con backend levantado, iniciar sesion desde UI con `admin/admin123` y verificar carga de empleados/departamentos.

### Implementation for User Story 1

- [X] T011 [US1] Implementar `login()` contra `GET /api/v1/empleados/auth/me` en `frontend/src/app/app.ts`
- [X] T012 [US1] Implementar construccion de `Authorization: Basic` desde formulario en `frontend/src/app/app.ts`
- [X] T013 [US1] Cargar empleados y departamentos solo tras login exitoso en `frontend/src/app/app.ts`
- [X] T014 [US1] Conectar submit del formulario de login y estado de carga en `frontend/src/app/app.html`
- [X] T015 [US1] Mostrar encabezado de usuario autenticado y acciones de panel en `frontend/src/app/app.html`

**Checkpoint**: User Story 1 fully functional and independently testable

---

## Phase 4: User Story 2 - Manejo de credenciales invalidas (Priority: P2)

**Goal**: Mostrar mensajes claros para credenciales invalidas y errores de red sin entrar al sistema.

**Independent Test**: Enviar credenciales incorrectas y comprobar que la UI permanece en login con error visible.

### Implementation for User Story 2

- [X] T016 [US2] Implementar mapeo de errores `401/403` y red a mensajes de login en `frontend/src/app/app.ts`
- [X] T017 [US2] Evitar carga de recursos protegidos cuando falla login en `frontend/src/app/app.ts`
- [X] T018 [US2] Renderizar alertas de error y reset de feedback en `frontend/src/app/app.html`
- [X] T019 [US2] Refinar estilos visuales de mensajes de error/login fallido en `frontend/src/app/app.scss`
- [X] T019a [US2] Bloquear login con `username` o `password` vacios/solo espacios en `frontend/src/app/app.ts` y `frontend/src/app/app.html`
- [X] T019b [US2] Bloquear login con cualquier espacio en `username` o `password` en `frontend/src/app/app.ts` y `frontend/src/app/app.html`

**Checkpoint**: User Stories 1 and 2 work independently

---

## Phase 5: User Story 3 - Cierre de sesion en frontend (Priority: P3)

**Goal**: Permitir logout para volver a estado no autenticado y limpiar datos de sesion.

**Independent Test**: Iniciar sesion, ejecutar logout y validar retorno a login con datos limpiados.

### Implementation for User Story 3

- [X] T020 [US3] Implementar `logout()` limpiando perfil, credenciales en memoria y datos cargados en `frontend/src/app/app.ts`
- [X] T021 [US3] Conectar boton `Cerrar sesion` y control de visibilidad por estado de sesion en `frontend/src/app/app.html`
- [X] T022 [US3] Ajustar estados visuales post-logout (mensaje y layout de retorno) en `frontend/src/app/app.scss`

**Checkpoint**: All user stories independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad, documentacion y validacion funcional end-to-end.

- [X] T023 [P] Actualizar prueba unitaria minima de UI de login en `frontend/src/app/app.spec.ts`
- [X] T024 [P] Documentar flujo final de login/logout en `specs/004-login-frontend-admin/quickstart.md`
- [X] T025 Ejecutar validacion de build final y registrar resultado en `specs/004-login-frontend-admin/plan.md`
- [X] T026 Verificar automaticamente que el feature no agrega endpoints ni modifica contrato backend ejecutando `scripts/quality/verify-no-backend-endpoints-or-contract-changes.sh`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
- **Polish (Phase 6)**: Depends on all user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - MVP
- **User Story 2 (P2)**: Can start after US1 is stable (reusa flujo de login)
- **User Story 3 (P3)**: Can start after US1 (depende de sesion autenticada existente)

### Within Each User Story

- Estado y reglas de negocio en `app.ts` antes de ajustes de UI en `app.html`
- Ajustes visuales en `app.scss` despues de comportamiento funcional
- Validacion de historia completa antes de pasar a la siguiente

### Parallel Opportunities

- `T002`, `T003`, `T004` pueden ejecutarse en paralelo (archivos distintos)
- `T006` y `T007` pueden ejecutarse en paralelo (config separada)
- En polish, `T023` y `T024` pueden ejecutarse en paralelo

---

## Parallel Example: User Story 1

```bash
# Luego de completar Foundation, avanzar US1 en paralelo por area:
Task: "Implementar login() contra /api/v1/empleados/auth/me en frontend/src/app/app.ts"
Task: "Conectar submit del formulario de login en frontend/src/app/app.html"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup)
2. Completar Phase 2 (Foundational)
3. Completar Phase 3 (US1)
4. Validar login admin/admin123 en entorno local

### Incremental Delivery

1. MVP con US1 (login exitoso)
2. Agregar US2 (errores de autenticacion y red)
3. Agregar US3 (logout y limpieza de sesion)
4. Ejecutar polish y validaciones finales

### Parallel Team Strategy

1. Dev A: `app.ts` (estado + login/logout)
2. Dev B: `app.html` (formularios + estados de vista)
3. Dev C: `app.scss` + `app.spec.ts` + `quickstart.md`

---

## Notes

- [P] tasks = archivos diferentes y sin dependencias directas
- Cada tarea incluye ruta exacta para ejecucion por LLM
- Cada historia conserva criterio de prueba independiente
- No se agregan endpoints backend nuevos en este feature
