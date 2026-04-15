# Tasks: Login de Empleado por Correo y Contrasena

**Input**: Design documents from `/specs/008-login-empleado-correo/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/, quickstart.md

**Tests**: Se incluyen tareas de pruebas porque la especificacion exige smoke real de login empleado (FR-007) y validacion contractual de seguridad/actor.

**Organization**: Tareas agrupadas por historia de usuario para permitir implementacion y validacion independiente por incremento.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar estructura base backend/frontend para el feature 008.

- [X] T001 Crear estructura de feature frontend para actor empleado en frontend/src/app/features/empleado/
- [X] T002 Configurar rutas base para login empleado en frontend/src/app/app.routes.ts
- [X] T003 [P] Crear esqueleto de contrato de frontend empleado en specs/008-login-empleado-correo/contracts/frontend-empleado-login-contract.md
- [X] T004 [P] Crear base de contrato OpenAPI de feature en specs/008-login-empleado-correo/contracts/empleados-login-openapi.yaml
- [X] T005 [P] Preparar placeholders de smoke E2E de empleado en frontend/cypress/e2e/empleado-login-smoke.cy.ts

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura de autenticacion y estado que bloquea todas las historias.

**CRITICAL**: Ninguna historia debe comenzar antes de completar esta fase.

- [X] T006 Ajustar politica de respuesta generica 401 para credenciales invalidas/inactivo en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java
- [X] T007 [P] Registrar motivo interno de cuenta inactiva en auditoria sin exponerlo al cliente en backend/src/main/java/com/dsw02/empleado/infrastructure/security/AuthenticationAuditListener.java
- [X] T008 [P] Alinear codigos de error y estructura de respuesta de autenticacion en backend/src/main/java/com/dsw02/empleado/api/dto/ErrorResponse.java
- [X] T009 [P] Refinar servicio de perfil autenticado por correo normalizado en backend/src/main/java/com/dsw02/empleado/application/ObtenerPerfilAutenticadoService.java
- [X] T010 [P] Consolidar helper de sesion en memoria para actor empleado en frontend/src/app/core/auth/auth-session.service.ts
- [X] T011 [P] Crear servicio de autenticacion de empleado para UI dedicada en frontend/src/app/core/auth/empleado-auth.facade.ts
- [X] T012 [P] Definir modelo de estado de login empleado en frontend/src/app/features/empleado/empleado-login.state.ts
- [X] T013 Mantener versionado y defaults de paginacion sin regresion en backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java
- [X] T014 Alinear quickstart tecnico inicial con rutas/flujo de empleado en specs/008-login-empleado-correo/quickstart.md

**Checkpoint**: Base lista para implementar historias por prioridad.

---

## Phase 3: User Story 1 - Iniciar sesion como empleado en UI (Priority: P1) 🎯 MVP

**Goal**: Entregar flujo funcional de login empleado en ruta dedicada con sesion en memoria y consumo real de `/auth/me`.

**Independent Test**: Abrir `/empleado/login`, autenticar con empleado activo y obtener estado autenticado EMPLEADO sin persistencia en storage.

### Tests for User Story 1

- [X] T015 [P] [US1] Crear pruebas unitarias del componente de login empleado en frontend/src/app/features/empleado/empleado-login.container.spec.ts
- [X] T016 [P] [US1] Crear prueba de integracion backend para auth/me con empleado activo en backend/src/test/java/com/dsw02/empleado/integration/SecurityIntegrationTest.java
- [X] T017 [P] [US1] Implementar smoke E2E de login empleado exitoso en frontend/cypress/e2e/empleado-login-smoke.cy.ts

### Implementation for User Story 1

- [X] T018 [P] [US1] Implementar contenedor de login empleado en frontend/src/app/features/empleado/empleado-login.container.ts
- [X] T019 [P] [US1] Implementar plantilla de login empleado con feedback de estado en frontend/src/app/features/empleado/empleado-login.container.html
- [X] T020 [P] [US1] Implementar estilos de pantalla dedicada en frontend/src/app/features/empleado/empleado-login.container.scss
- [X] T021 [US1] Registrar ruta dedicada `/empleado/login` en frontend/src/app/app.routes.ts
- [X] T022 [US1] Integrar facade de auth empleado con session en memoria en frontend/src/app/features/empleado/empleado-login.container.ts
- [X] T023 [US1] Validar y normalizar correo en submit de login empleado en frontend/src/app/features/empleado/empleado-login.container.ts
- [X] T024 [US1] Asegurar no persistencia en localStorage/sessionStorage en frontend/src/app/core/auth/auth-session.service.ts

**Checkpoint**: US1 funcional y demostrable de forma independiente.

---

## Phase 4: User Story 2 - Mantener separacion de actores (Priority: P2)

**Goal**: Garantizar separacion contractual y funcional entre ADMIN y EMPLEADO con alcance SELF para empleado.

**Independent Test**: Validar `/auth/me` para ambos actores y denegar accesos administrativos a EMPLEADO autenticado.

### Tests for User Story 2

- [X] T025 [P] [US2] Añadir prueba de actor ADMIN en auth/me en backend/src/test/java/com/dsw02/empleado/integration/SecurityIntegrationTest.java
- [X] T026 [P] [US2] Añadir prueba de denegacion de listados para EMPLEADO en backend/src/test/java/com/dsw02/empleado/integration/SecurityIntegrationTest.java
- [X] T027 [P] [US2] Añadir prueba frontend de error por actor no EMPLEADO en frontend/src/app/features/empleado/empleado-login.container.spec.ts

### Implementation for User Story 2

- [X] T028 [P] [US2] Ajustar mapeo de perfil por actor en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoAuthProfileResponse.java
- [X] T029 [US2] Endurecer reglas de autorizacion por rol en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java
- [X] T030 [US2] Refinar manejo de actorType en flujo frontend empleado en frontend/src/app/features/empleado/empleado-login.container.ts
- [X] T031 [US2] Mostrar mensaje de permisos cuando actor no sea EMPLEADO en frontend/src/app/features/empleado/empleado-login.container.html
- [X] T032 [US2] Alinear mensajes de error generico en frontend para 401/inactivo en frontend/src/app/core/http/api-error.util.ts

**Checkpoint**: US2 valida separacion de actores sin depender de US3.

---

## Phase 5: User Story 3 - Gobernanza de contrato y pruebas (Priority: P3)

**Goal**: Sincronizar contrato OpenAPI, quickstart y smoke CI para trazabilidad completa.

**Independent Test**: Contrato actualizado, quickstart ejecutable y smoke empleado pasando en entorno real.

### Tests for User Story 3

- [X] T033 [P] [US3] Crear validacion contractual de ejemplos 200/401 en backend/src/test/java/com/dsw02/empleado/integration/SecurityIntegrationTest.java
- [X] T034 [P] [US3] Crear escenario E2E de fallo generico (password incorrecta) en frontend/cypress/e2e/empleado-login-smoke.cy.ts
- [X] T035 [P] [US3] Crear escenario E2E de cuenta inactiva con mismo error generico en frontend/cypress/e2e/empleado-login-smoke.cy.ts

### Implementation for User Story 3

- [X] T036 [P] [US3] Completar contrato OpenAPI final con ejemplos actor/admin y 401 generico en specs/008-login-empleado-correo/contracts/empleados-login-openapi.yaml
- [X] T037 [P] [US3] Completar contrato de frontend y estados de UX en specs/008-login-empleado-correo/contracts/frontend-empleado-login-contract.md
- [X] T038 [US3] Actualizar quickstart con comandos y evidencia de validacion en specs/008-login-empleado-correo/quickstart.md
- [X] T039 [US3] Integrar smoke empleado en pipeline frontend en .github/workflows/frontend-ci.yml
- [X] T040 [US3] Actualizar script de humo operativo para empleado en scripts/smoke/empleados-smoke.sh

**Checkpoint**: US3 completa con gobernanza y pruebas trazables.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre integral de calidad, performance y documentacion final.

- [ ] T041 [P] Ejecutar validacion completa backend + frontend tests para feature 008 desde backend/pom.xml y frontend/package.json
- [ ] T042 [P] Ejecutar prueba de rendimiento auth/me a 50 rps y registrar p95 en specs/008-login-empleado-correo/quickstart.md
- [ ] T043 [P] Documentar decisiones y restricciones finales del feature en specs/008-login-empleado-correo/research.md
- [ ] T044 Limpiar codigo temporal y ajustes de refactor en frontend/src/app/features/empleado/ y backend/src/main/java/com/dsw02/empleado/
- [ ] T045 Ejecutar validacion final de quickstart y registrar evidencia de cierre en specs/008-login-empleado-correo/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- Setup (Phase 1): inicia de inmediato.
- Foundational (Phase 2): depende de Setup y bloquea historias.
- User Stories (Phases 3-5): dependen de Foundational.
- Polish (Phase 6): depende de historias completadas.

### User Story Dependencies

- US1 (P1): inicia tras Foundational; define MVP funcional.
- US2 (P2): inicia tras US1 para reforzar separacion de actores sobre flujo ya funcional.
- US3 (P3): inicia tras US1 y US2 para cerrar contratos y pruebas de gobernanza.

### Within Each User Story

- Primero pruebas de historia (unit/integration/e2e).
- Luego implementacion de componentes/servicios.
- Finalmente integracion contractual y validacion independiente.

### Parallel Opportunities

- Setup: T003, T004, T005 en paralelo.
- Foundational: T007, T008, T009, T010, T011, T012 en paralelo.
- US1: T015, T016, T017 en paralelo; T018, T019, T020 en paralelo.
- US2: T025, T026, T027 en paralelo; T028 y T030 en paralelo.
- US3: T033, T034, T035 en paralelo; T036 y T037 en paralelo.
- Polish: T041, T042, T043 en paralelo.

---

## Parallel Example: User Story 1

```bash
Task: T015 [US1] frontend/src/app/features/empleado/empleado-login.container.spec.ts
Task: T016 [US1] backend/src/test/java/com/dsw02/empleado/integration/SecurityIntegrationTest.java
Task: T017 [US1] frontend/cypress/e2e/empleado-login-smoke.cy.ts
```

## Parallel Example: User Story 2

```bash
Task: T025 [US2] backend/src/test/java/com/dsw02/empleado/integration/SecurityIntegrationTest.java
Task: T027 [US2] frontend/src/app/features/empleado/empleado-login.container.spec.ts
```

## Parallel Example: User Story 3

```bash
Task: T034 [US3] frontend/cypress/e2e/empleado-login-smoke.cy.ts
Task: T036 [US3] specs/008-login-empleado-correo/contracts/empleados-login-openapi.yaml
Task: T037 [US3] specs/008-login-empleado-correo/contracts/frontend-empleado-login-contract.md
```

---

## Implementation Strategy

### MVP First (US1)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar login empleado en ruta dedicada y smoke real.

### Incremental Delivery

1. Entregar MVP con US1.
2. Agregar US2 para robustez de separacion de actores.
3. Agregar US3 para trazabilidad de contrato + pruebas.
4. Cerrar con Phase 6 (performance, evidencia, limpieza).

### Suggested MVP Scope

- MVP sugerido: T001-T024 (Setup + Foundational + US1).
