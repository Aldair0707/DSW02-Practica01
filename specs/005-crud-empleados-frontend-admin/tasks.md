# Tasks: CRUD Empleados Desde Admin

**Input**: Design documents from `/specs/005-crud-empleados-frontend-admin/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: No se agregan tareas de tests en este plan porque la especificacion no exige enfoque TDD ni test-first para esta feature.

**Organization**: Tasks grouped by user story so each story can be implemented and validated independently.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Alinear base de frontend y documentacion operativa para implementar el CRUD administrativo.

- [X] T001 Ajustar configuracion de proxy API en frontend/proxy.conf.json
- [X] T002 Validar scripts de ejecucion local para frontend en frontend/package.json
- [X] T003 [P] Documentar variables y prerrequisitos de ejecucion en specs/005-crud-empleados-frontend-admin/quickstart.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura comun que bloquea la implementacion de todas las historias.

**⚠️ CRITICAL**: Ninguna historia de usuario inicia antes de completar esta fase.

- [X] T004 Extender modelos tipados de autenticacion/sesion en frontend/src/app/core/models/auth.models.ts
- [X] T005 [P] Crear modelos de dominio de empleados paginados en frontend/src/app/core/models/empleado.models.ts
- [X] T006 [P] Crear cliente HTTP de empleados CRUD en frontend/src/app/core/http/empleados-api.service.ts
- [X] T007 Actualizar manejo de errores HTTP compartidos en frontend/src/app/core/http/api-error.util.ts
- [X] T008 Actualizar servicio de sesion admin para gating de operaciones e invalidacion por backend (sin temporizador de inactividad) en frontend/src/app/core/auth/auth-session.service.ts
- [X] T009 Integrar providers necesarios de servicios core en frontend/src/app/app.config.ts

**Checkpoint**: Base lista; historias US1-US3 pueden ejecutarse de forma incremental.

---

## Phase 3: User Story 1 - Acceso Admin A Gestion De Empleados (Priority: P1) 🎯 MVP

**Goal**: Permitir login admin valido y acceso seguro a la vista de gestion de empleados.

**Independent Test**: Iniciar sesion como admin y verificar que se habilita la vista de gestion; usuarios no autorizados quedan bloqueados.

### Implementation for User Story 1

- [X] T010 [US1] Implementar flujo de autenticacion y autorizacion admin, incluyendo reautenticacion tras no autorizado de backend, en frontend/src/app/app.ts
- [X] T011 [P] [US1] Implementar estados y mensajes de login/denegacion en frontend/src/app/app.html
- [X] T012 [P] [US1] Ajustar estilos de estados de autenticacion y feedback en frontend/src/app/app.scss
- [X] T013 [US1] Integrar carga inicial de listado protegido tras login en frontend/src/app/app.ts

**Checkpoint**: US1 funcional y validable de forma independiente.

---

## Phase 4: User Story 2 - Alta Y Edicion De Empleados (Priority: P2)

**Goal**: Permitir al administrador crear y editar empleados con validaciones de formulario.

**Independent Test**: Crear un empleado valido y editar uno existente verificando confirmacion y refresco del listado.

### Implementation for User Story 2

- [X] T014 [US2] Definir estructura y reglas del formulario de alta/edicion alineadas con campos obligatorios exigidos por backend en frontend/src/app/app.ts
- [X] T015 [US2] Implementar accion de crear empleado conectada a API en frontend/src/app/app.ts
- [X] T016 [US2] Implementar accion de editar empleado conectada a API, conservando contrasena cuando llegue vacia, en frontend/src/app/app.ts
- [X] T017 [P] [US2] Implementar UI de formulario y modo edicion (con departamento opcional y estado sin asignar) en frontend/src/app/app.html
- [X] T018 [P] [US2] Aplicar feedback visual de validacion de campos y estados de guardado en frontend/src/app/app.scss
- [X] T019 [US2] Manejar errores de negocio (correo duplicado/validacion) en frontend/src/app/core/http/api-error.util.ts

**Checkpoint**: US2 funcional y validable de forma independiente.

---

## Phase 5: User Story 3 - Baja Controlada Y Consulta Paginada (Priority: P3)

**Goal**: Habilitar listado paginado de empleados y eliminacion con confirmacion explicita.

**Independent Test**: Navegar por paginas, eliminar un empleado con confirmacion y verificar refresco consistente del listado.

### Implementation for User Story 3

- [X] T020 [US3] Implementar lectura paginada de empleados con metadatos en frontend/src/app/app.ts
- [X] T021 [US3] Implementar controles de navegacion de pagina en frontend/src/app/app.html
- [X] T022 [US3] Implementar eliminacion con confirmacion y refresco de pagina en frontend/src/app/app.ts
- [X] T023 [US3] Implementar acciones de fila para eliminar/editar en frontend/src/app/app.html
- [X] T024 [P] [US3] Ajustar estilos de tabla, paginacion y acciones peligrosas en frontend/src/app/app.scss

**Checkpoint**: US3 funcional y validable de forma independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad y validacion transversal de la feature.

- [X] T025 [P] Actualizar contrato funcional de frontend para CRUD admin en specs/005-crud-empleados-frontend-admin/contracts/frontend-empleados-admin-crud-contract.md
- [X] T026 [P] Actualizar guia de validacion manual end-to-end en specs/005-crud-empleados-frontend-admin/quickstart.md
- [X] T027 Ejecutar verificacion de no cambios de endpoints/contratos backend con scripts/quality/verify-no-backend-endpoints-or-contract-changes.sh
- [X] T028 Ejecutar build de frontend para validar integracion final en frontend/package.json

---

## Dependencies & Execution Order

### Phase Dependencies

- Setup (Phase 1): sin dependencias.
- Foundational (Phase 2): depende de Setup y bloquea todas las historias.
- User Stories (Phase 3-5): dependen de completar Foundational.
- Polish (Phase 6): depende de completar historias objetivo.

### User Story Dependencies

- US1 (P1): inicia despues de Foundational; no depende de otras historias.
- US2 (P2): inicia despues de Foundational; reutiliza base de US1 para sesion autenticada.
- US3 (P3): inicia despues de Foundational; reutiliza sesion y modelos de empleados.

### Recommended Story Order

- US1 -> US2 -> US3

---

## Parallel Opportunities

- Setup en paralelo: T003.
- Foundational en paralelo: T005, T006.
- US1 en paralelo: T011 y T012 (una vez iniciado T010).
- US2 en paralelo: T017 y T018 (tras definir formulario en T014).
- US3 en paralelo: T024 en paralelo con implementacion de logica de T020/T022.
- Polish en paralelo: T025 y T026.

---

## Parallel Example: User Story 1

```bash
Task: T011 [US1] Implementar estados y mensajes de login/denegacion en frontend/src/app/app.html
Task: T012 [US1] Ajustar estilos de estados de autenticacion y feedback en frontend/src/app/app.scss
```

## Parallel Example: User Story 2

```bash
Task: T017 [US2] Implementar UI de formulario y modo edicion en frontend/src/app/app.html
Task: T018 [US2] Aplicar feedback visual de validacion de campos y estados de guardado en frontend/src/app/app.scss
```

## Parallel Example: User Story 3

```bash
Task: T020 [US3] Implementar lectura paginada de empleados con metadatos en frontend/src/app/app.ts
Task: T024 [US3] Ajustar estilos de tabla, paginacion y acciones peligrosas en frontend/src/app/app.scss
```

---

## Implementation Strategy

### MVP First (US1)

1. Completar Phase 1 y Phase 2.
2. Entregar Phase 3 (US1).
3. Validar acceso admin + gating de autorizacion.
4. Demo interna de flujo de entrada al CRUD.

### Incremental Delivery

1. Base lista (Phase 1 + 2).
2. Agregar US1 y validar.
3. Agregar US2 y validar.
4. Agregar US3 y validar.
5. Cerrar con polish y validaciones finales.

### Parallel Team Strategy

1. Equipo A: tareas core de Foundational.
2. Equipo B: UI de US2/US3 en paralelo cuando Foundational termine.
3. Equipo C: documentacion/quickstart/contrato en fase de cierre.

---

## Notes

- Cada tarea incluye ruta exacta para ejecucion directa.
- Tareas [P] estan marcadas solo cuando no dependen de archivo en conflicto o de una tarea incompleta.
- Cada historia queda con criterio de validacion independiente, permitiendo entregas incrementales.
