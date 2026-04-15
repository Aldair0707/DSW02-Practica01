# Tasks: Refactor Angular Con Componentes

**Input**: Design documents from `/specs/007-refactor-angular-components/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: Incluidas porque la especificacion exige actualizar/anadir pruebas frontend (FR-009) y definir estrategia E2E hibrida (FR-011, FR-012).

**Organization**: Tareas agrupadas por historia de usuario para permitir implementacion incremental y validacion independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar estructura base del refactor frontend y cimientos de pruebas.

- [X] T001 Crear estructura por features y shared UI en frontend/src/app/features/ y frontend/src/app/shared/components/
- [X] T002 Configurar rutas base del shell administrativo en frontend/src/app/app.routes.ts
- [X] T003 [P] Definir barrel de componentes compartidos en frontend/src/app/shared/components/index.ts
- [ ] T004 [P] Ajustar bootstrap y providers para nuevo arbol de componentes en frontend/src/app/app.config.ts

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura bloqueante para todas las historias.

**CRITICAL**: Ninguna historia debe comenzar antes de completar esta fase.

- [X] T005 Definir modelos de estado compartidos en frontend/src/app/core/models/view-state.models.ts
- [ ] T006 Estandarizar mapeo de errores API en frontend/src/app/core/http/api-error.util.ts
- [X] T007 Implementar utilidades de transicion de estado por feature en frontend/src/app/core/state/feature-view-state.ts
- [X] T008 [P] Implementar componentes shared de feedback (loading/error/empty) en frontend/src/app/shared/components/feedback/
- [X] T009 [P] Consolidar configuracion Cypress base en frontend/cypress.config.ts y frontend/cypress/support/e2e.ts
- [ ] T010 Configurar contrato de consumo backend sin cambios funcionales en frontend/src/app/core/http/empleados-api.service.ts
- [X] T011 Definir pipeline de PR para smoke reales E2E en .github/workflows/frontend-ci.yml

**Checkpoint**: Base lista para implementar historias por prioridad.

---

## Phase 3: User Story 1 - Modularizar La Pantalla Principal (Priority: P1) 🎯 MVP

**Goal**: Separar pantalla principal en contenedor admin y componentes de dominio sin regresion funcional.

**Independent Test**: Login y navegacion principal operan tras modularizacion, y smoke real login+navegacion pasa en CI.

### Tests for User Story 1

- [X] T012 [P] [US1] Crear smoke E2E real de login+navegacion en frontend/cypress/e2e/admin-mvp.cy.ts
- [X] T013 [P] [US1] Crear pruebas del contenedor administrativo en frontend/src/app/features/admin/admin-shell.container.spec.ts

### Implementation for User Story 1

- [X] T014 [P] [US1] Crear componente de autenticacion presentacional en frontend/src/app/features/auth/auth-login.component.ts
- [X] T015 [P] [US1] Crear componente presentacional de panel empleados en frontend/src/app/features/empleados/empleados-panel.component.ts
- [X] T016 [P] [US1] Crear componente presentacional de panel departamentos en frontend/src/app/features/departamentos/departamentos-panel.component.ts
- [X] T017 [US1] Implementar contenedor orquestador principal en frontend/src/app/features/admin/admin-shell.container.ts
- [X] T018 [US1] Integrar plantilla del contenedor principal en frontend/src/app/features/admin/admin-shell.container.html
- [X] T019 [US1] Reducir app root a host del shell en frontend/src/app/app.ts
- [X] T020 [US1] Simplificar plantilla raiz a host de navegacion en frontend/src/app/app.html

**Checkpoint**: US1 completa, independiente y validada por smoke real de login+navegacion.

---

## Phase 4: User Story 2 - Reutilizar Componentes De Formularios Y Listados (Priority: P2)

**Goal**: Reutilizar formularios/tablas/feedback en empleados y departamentos minimizando duplicacion.

**Independent Test**: Crear y listar Empleados con componentes reutilizables funciona, y smoke real create+list Empleados pasa en CI.

### Tests for User Story 2

- [ ] T021 [P] [US2] Crear pruebas unitarias de formulario reutilizable en frontend/src/app/shared/components/admin-form/admin-form.component.spec.ts
- [ ] T022 [P] [US2] Crear pruebas unitarias de tabla reutilizable en frontend/src/app/shared/components/admin-table/admin-table.component.spec.ts
- [X] T023 [P] [US2] Crear smoke E2E real de crear+listar Empleados en frontend/cypress/e2e/empleados-create-list-smoke.cy.ts
- [ ] T024 [P] [US2] Crear prueba de integracion de reutilizacion en frontend/src/app/features/admin/admin-shell.container.spec.ts

### Implementation for User Story 2

- [ ] T025 [P] [US2] Implementar componente formulario reutilizable en frontend/src/app/shared/components/admin-form/admin-form.component.ts
- [ ] T026 [P] [US2] Implementar componente tabla reutilizable en frontend/src/app/shared/components/admin-table/admin-table.component.ts
- [ ] T027 [US2] Migrar modulo empleados a componentes shared en frontend/src/app/features/empleados/empleados-feature.container.ts
- [ ] T028 [US2] Migrar modulo departamentos a componentes shared en frontend/src/app/features/departamentos/departamentos-feature.container.ts
- [ ] T029 [US2] Reemplazar markup duplicado por shared components en frontend/src/app/features/admin/admin-shell.container.html

**Checkpoint**: US2 completa, create+list Empleados validado en smoke real y UI reutilizable aplicada.

---

## Phase 5: User Story 3 - Estandarizar Estado, Errores Y Convenciones (Priority: P3)

**Goal**: Unificar convenciones de estado async, errores y comunicacion entre contenedores y presentacionales.

**Independent Test**: Escenarios de error y loading son consistentes, sin estados intermedios invalidos en modulos administrativos.

### Tests for User Story 3

- [ ] T030 [P] [US3] Crear pruebas de mapeo de errores funcionales en frontend/src/app/core/http/api-error.util.spec.ts
- [ ] T031 [P] [US3] Crear pruebas de transiciones de estado del shell en frontend/src/app/features/admin/admin-shell.container.spec.ts

### Implementation for User Story 3

- [ ] T032 [P] [US3] Robustecer helper de estado por feature en frontend/src/app/core/state/feature-view-state.ts
- [ ] T033 [US3] Estandarizar manejo de loading/error en consumo API en frontend/src/app/core/http/empleados-api.service.ts
- [ ] T034 [US3] Estandarizar mensajes de autenticacion y permisos en frontend/src/app/core/auth/auth-session.service.ts
- [ ] T035 [US3] Evitar submits/acciones duplicadas en formulario reutilizable en frontend/src/app/shared/components/admin-form/admin-form.component.ts
- [ ] T036 [US3] Gestionar expiracion de sesion y limpieza de estado en frontend/src/app/features/admin/admin-shell.container.ts

**Checkpoint**: US3 completa con convenciones homogeneas y cobertura de errores/estado.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad, estabilizacion CI y documentacion final.

- [ ] T037 [P] Actualizar documentacion tecnica y estrategia de pruebas en frontend/README.md
- [X] T038 [P] Configurar ejecucion de smoke reales en PR en .github/workflows/frontend-ci.yml
- [X] T039 [P] Configurar ejecucion de regresion con stubs en .github/workflows/frontend-ci.yml
- [ ] T040 Ejecutar y ajustar build de produccion en frontend/angular.json
- [ ] T041 Ejecutar y ajustar suite completa de pruebas desde frontend/package.json
- [ ] T042 Limpiar codigo obsoleto del monolito anterior en frontend/src/app/app.ts
- [ ] T043 Verificar quickstart y registrar comandos finales en specs/007-refactor-angular-components/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- Setup (Phase 1): inicia de inmediato.
- Foundational (Phase 2): depende de Setup y bloquea historias.
- User Stories (Phases 3-5): dependen de Foundational.
- Polish (Phase 6): depende de historias completadas.

### User Story Dependencies

- US1 (P1): inicia tras Foundational y define MVP funcional.
- US2 (P2): inicia tras US1 para reutilizacion de componentes sobre shell estable.
- US3 (P3): inicia tras US1 y US2 para estandarizar sobre arquitectura final.

### Within Each User Story

- Pruebas primero y en fallo inicial antes de implementar.
- Componentes base antes de integracion en contenedor.
- Integracion antes de endurecer validaciones y mensajes.
- Validacion independiente antes de pasar a siguiente prioridad.

### Parallel Opportunities

- Setup: T003 y T004 en paralelo.
- Foundational: T008 y T009 en paralelo.
- US1: T012 y T013 en paralelo; T014/T015/T016 en paralelo.
- US2: T021/T022/T023/T024 en paralelo; T025 y T026 en paralelo.
- US3: T030 y T031 en paralelo; T032 puede iniciar en paralelo a ajustes de API.
- Polish: T037/T038/T039 en paralelo.

---

## Parallel Example: User Story 1

```bash
# Tests US1 en paralelo
Task: T012 [US1] frontend/cypress/e2e/admin-mvp.cy.ts
Task: T013 [US1] frontend/src/app/features/admin/admin-shell.container.spec.ts

# Componentes US1 en paralelo
Task: T014 [US1] frontend/src/app/features/auth/auth-login.component.ts
Task: T015 [US1] frontend/src/app/features/empleados/empleados-panel.component.ts
Task: T016 [US1] frontend/src/app/features/departamentos/departamentos-panel.component.ts
```

## Parallel Example: User Story 2

```bash
# Tests US2 en paralelo
Task: T021 [US2] frontend/src/app/shared/components/admin-form/admin-form.component.spec.ts
Task: T022 [US2] frontend/src/app/shared/components/admin-table/admin-table.component.spec.ts
Task: T023 [US2] frontend/cypress/e2e/empleados-create-list-smoke.cy.ts

# Shared components US2 en paralelo
Task: T025 [US2] frontend/src/app/shared/components/admin-form/admin-form.component.ts
Task: T026 [US2] frontend/src/app/shared/components/admin-table/admin-table.component.ts
```

## Parallel Example: User Story 3

```bash
# Tests US3 en paralelo
Task: T030 [US3] frontend/src/app/core/http/api-error.util.spec.ts
Task: T031 [US3] frontend/src/app/features/admin/admin-shell.container.spec.ts
```

---

## Implementation Strategy

### MVP First (US1)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar smoke real de login+navegacion y pruebas de US1.

### Incremental Delivery

1. Entregar MVP con US1.
2. Agregar US2 y validar smoke real crear+listar Empleados.
3. Agregar US3 para homogeneizar estado/errores.
4. Cerrar con Phase 6 y gates de PR.

### Suggested MVP Scope

- Incluir: Phase 1 + Phase 2 + Phase 3 (US1).
- Diferir: US2, US3 y parte de polish.
