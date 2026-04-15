# Tasks: Corregir Rol Admin En Login

**Input**: Design documents from `/specs/006-fix-admin-auth-role/`
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/

**Tests**: No se crean tareas de TDD porque no fue solicitado explícitamente; se incluyen tareas de validación de runtime, suites relevantes y quality gates constitucionales.

**Organization**: Tareas agrupadas por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Alinear artefactos de diseño y contrato base de la feature.

- [x] T001 Registrar clarificaciones aprobadas y estado final de la spec en specs/006-fix-admin-auth-role/spec.md
- [x] T002 Registrar constitution check final (pre/post diseño) en specs/006-fix-admin-auth-role/plan.md
- [x] T003 [P] Consolidar decisiones de diseño cerradas en specs/006-fix-admin-auth-role/research.md
- [x] T004 [P] Consolidar entidades/reglas finales en specs/006-fix-admin-auth-role/data-model.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Base compartida de autenticación/autorización y gobernanza API.

**⚠️ CRITICAL**: Ninguna historia puede ejecutarse antes de completar esta fase.

- [x] T005 Consolidar DTO de perfil por actor ADMIN/EMPLEADO en backend/src/main/java/com/dsw02/empleado/api/dto/EmpleadoAuthProfileResponse.java
- [x] T006 Implementar resolución de principal autenticado por actor en backend/src/main/java/com/dsw02/empleado/application/ObtenerPerfilAutenticadoService.java
- [x] T007 Implementar handlers de seguridad para 401/403 con códigos AUTH_INVALIDA/NO_AUTORIZADO en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java
- [x] T008 [P] Mantener defaults Basic Auth con override por entorno en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java
- [x] T009 [P] Consolidar contrato de errores de frontend en frontend/src/app/core/http/api-error.util.ts
- [x] T010 [P] Consolidar contrato de tipos auth frontend en frontend/src/app/core/models/auth.models.ts
- [x] T011 Aplicar gobernanza de listados (`page`, `size`, `size=5`) en backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java
- [x] T012 [P] Aplicar gobernanza de listados (`page`, `size`, `size=5`) en backend/src/main/java/com/dsw02/empleado/api/DepartamentoController.java
- [x] T013 [P] Actualizar contrato funcional de la feature en specs/006-fix-admin-auth-role/contracts/auth-profile-admin-role-contract.md
- [x] T014 Actualizar OpenAPI versionado/paginación/auth profile en specs/002-autenticar-empleados-correo/contracts/empleados-auth-openapi.yaml

**Checkpoint**: Base técnica lista para desarrollar historias por prioridad.

---

## Phase 3: User Story 1 - Login Correcto De Administrador (Priority: P1) 🎯 MVP

**Goal**: Permitir login de admin y acceso al frontend administrativo sin clasificarlo como empleado.

**Independent Test**: Login con `admin/admin123` y verificación de `actorType=ADMIN` en `auth/me` con panel administrativo habilitado.

- [x] T015 [US1] Ajustar endpoint de perfil autenticado para actor ADMIN en backend/src/main/java/com/dsw02/empleado/api/EmpleadoController.java
- [x] T016 [US1] Adaptar consumo de `auth/me` al contrato por actor en frontend/src/app/core/http/empleados-api.service.ts
- [x] T017 [US1] Ajustar flujo de login por `actorType=ADMIN` en frontend/src/app/app.ts
- [x] T018 [P] [US1] Actualizar presentación de identidad autenticada en frontend/src/app/app.html
- [x] T019 [P] [US1] Ajustar persistencia de sesión auth por actor en frontend/src/app/core/auth/auth-session.service.ts

**Checkpoint**: US1 funcional e independiente (MVP).

---

## Phase 4: User Story 2 - Autorizacion Coherente Para CRUD Admin (Priority: P2)

**Goal**: Permitir CRUD solo a ADMIN y denegar acceso a EMPLEADO en recursos administrativos.

**Independent Test**: ADMIN opera listados y CRUD; EMPLEADO autenticado recibe 403 en recursos administrativos.

- [x] T020 [US2] Endurecer autorización de CRUD de empleados por rol ADMIN en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java
- [x] T021 [P] [US2] Endurecer autorización de CRUD de departamentos por rol ADMIN en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java
- [x] T022 [US2] Mantener alta de empleado con departamento en un solo submit en backend/src/main/java/com/dsw02/empleado/application/CrearEmpleadoService.java
- [x] T023 [P] [US2] Alinear payload de alta con departamento opcional en frontend/src/app/core/models/empleado.models.ts
- [x] T024 [US2] Mantener flujo de alta unificado desde UI admin en frontend/src/app/app.ts
- [x] T025 [US2] Alinear reglas de autorización y listados mínimos en specs/006-fix-admin-auth-role/contracts/auth-profile-admin-role-contract.md

**Checkpoint**: US2 completa sin regresión de US1.

---

## Phase 5: User Story 3 - Mensajes Claros Ante Credenciales O Rol Invalidos (Priority: P3)

**Goal**: Diferenciar claramente errores de autenticación inválida y falta de permisos con contrato estable.

**Independent Test**: Forzar 401 y 403 y verificar mapeo consistente a `AUTH_INVALIDA` y `NO_AUTORIZADO` en backend/frontend.

- [ ] T026 [US3] Verificar por integración el mapeo 401/403 con códigos funcionales en backend/src/test/java/com/dsw02/empleado/integration/SecurityIntegrationTest.java
- [x] T027 [P] [US3] Alinear payload de error con contrato funcional en backend/src/main/java/com/dsw02/empleado/api/dto/ErrorResponse.java
- [x] T028 [US3] Traducir errores API a mensajes UX en frontend/src/app/core/http/api-error.util.ts
- [x] T029 [P] [US3] Ajustar mensajes de login/carga y control de sesión en frontend/src/app/app.ts
- [x] T030 [US3] Reflejar mensajes contextuales de acceso en frontend/src/app/app.html

**Checkpoint**: US3 completa y validable de forma independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre integral de documentación, validación y calidad.

- [x] T031 [P] Actualizar guía de verificación manual y runtime en specs/006-fix-admin-auth-role/quickstart.md
- [x] T032 Actualizar resumen final de cumplimiento constitucional en specs/006-fix-admin-auth-role/plan.md
- [x] T033 Ejecutar smoke end-to-end de la feature en scripts/smoke/empleados-smoke.sh
- [ ] T034 Ejecutar pruebas de integración backend relevantes en backend/src/test/java/com/dsw02/empleado/integration/SecurityIntegrationTest.java y backend/src/test/java/com/dsw02/empleado/integration/EmpleadoCrudIntegrationTest.java
- [x] T035 [P] Validar manualmente en Swagger UI rutas versionadas, parámetros page/size y default size=5 para listados
- [x] T036 [P] Ejecutar pruebas/build frontend relevantes en frontend/src/app/app.spec.ts y frontend/package.json
- [x] T037 [P] Documentar defer explícito de FR-009 y referencia a feature futura en specs/006-fix-admin-auth-role/plan.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia inmediatamente.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias.
- **Phase 3 (US1)**: depende de Phase 2.
- **Phase 4 (US2)**: depende de Phase 2; integra sobre base de US1.
- **Phase 5 (US3)**: depende de Phase 2 y de respuestas de seguridad activas.
- **Phase 6 (Polish)**: depende de las historias objetivo completadas.

### User Story Dependencies

- **US1 (P1)**: sin dependencia de otras historias tras fundación.
- **US2 (P2)**: depende de base de roles/seguridad; se valida sobre US1.
- **US3 (P3)**: depende del contrato de errores activo en backend y frontend.

### Story Completion Order (Dependency Graph)

- **US1 -> US2 -> US3**

### Parallel Opportunities

- Setup: T003 y T004 en paralelo.
- Foundational: T008, T009, T010, T012 y T013 en paralelo tras T005-T007.
- US1: T018 y T019 en paralelo después de T015-T017.
- US2: T021 y T023 en paralelo después de T020-T022.
- US3: T027 y T029 en paralelo después de T026.
- Polish: T031, T035 y T037 en paralelo con T032-T033-T034-T036.

---

## Parallel Example: User Story 1

```bash
Task: "T018 [US1] Actualizar presentación de identidad autenticada en frontend/src/app/app.html"
Task: "T019 [US1] Ajustar persistencia de sesión auth por actor en frontend/src/app/core/auth/auth-session.service.ts"
```

---

## Parallel Example: User Story 2

```bash
Task: "T021 [US2] Endurecer autorización de CRUD de departamentos por rol ADMIN en backend/src/main/java/com/dsw02/empleado/infrastructure/security/SecurityConfig.java"
Task: "T023 [US2] Alinear payload de alta con departamento opcional en frontend/src/app/core/models/empleado.models.ts"
```

---

## Parallel Example: User Story 3

```bash
Task: "T027 [US3] Alinear payload de error con contrato funcional en backend/src/main/java/com/dsw02/empleado/api/dto/ErrorResponse.java"
Task: "T029 [US3] Ajustar mensajes de login/carga y control de sesión en frontend/src/app/app.ts"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1.
2. Completar Phase 2.
3. Completar Phase 3 (US1).
4. Validar login admin y acceso administrativo.

### Incremental Delivery

1. Setup + Foundational.
2. Entregar US1 y validar de forma independiente.
3. Entregar US2 con autorización CRUD completa.
4. Entregar US3 con mensajes y códigos funcionales consistentes.
5. Cerrar con quickstart, smoke y quality gate.

### Parallel Team Strategy

1. Equipo completo en fases 1-2.
2. Luego distribución por historia:
   - Dev A: US1.
   - Dev B: US2.
   - Dev C: US3.
3. Integración y validación final en Phase 6.

---

## Notes

- Todas las tareas siguen formato checklist: `- [ ] Txxx [P] [USx] Descripción con ruta`.
- Las etiquetas `[USx]` se usan solo en fases de historias.
- FR-009 se mantiene fuera de alcance en esta feature según clarificación aprobada.
- T026 y T034 quedan pendientes de cierre definitivo en este entorno porque Testcontainers no logra resolver un Docker environment valido en el runner de pruebas.
