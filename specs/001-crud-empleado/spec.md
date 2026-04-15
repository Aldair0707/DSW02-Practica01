# Feature Specification: CRUD de empleado

**Feature Branch**: `001-crud-empleado`  
**Created**: 2026-02-25  
**Status**: Draft  
**Input**: User description: "Crea un CRUD de empleado con los campos clave, nombre, dirección y telefono. Donde clave sea el pk y nombre, dirección y telefono sea de 100 caracteres."

## Clarifications

### Session 2026-02-25

- Q: ¿Cómo debe definirse el campo `clave` como PK? → A: **(SUPERSEDED por sesión 2026-02-26)**.

### Session 2026-02-26

- Q: ¿Cómo debe definirse ahora `clave`? → A: `clave` se compone de un prefijo fijo `EMP-` y un consecutivo autonumérico; la PK es compuesta por (`prefijo`, `consecutivo`) y se expone como `clave` en formato `EMP-<número>`.

### Session 2026-03-08

- Q: ¿Qué hacer en alta si el cliente envía `clave` manual? → A: Opción B: ignorar la `clave` enviada y crear de todos modos con `clave` autogenerada `EMP-<autonumérico>`.
- Q: ¿Cómo debe comportarse el listado de empleados? → A: Opción A: el listado debe ser paginado con parámetros `page` y `size`, usando `size=5` por defecto.

## User Scenarios & Testing *(mandatory)*

<!--
  IMPORTANT: User stories should be PRIORITIZED as user journeys ordered by importance.
  Each user story/journey must be INDEPENDENTLY TESTABLE - meaning if you implement just ONE of them,
  you should still have a viable MVP (Minimum Viable Product) that delivers value.
  
  Assign priorities (P1, P2, P3, etc.) to each story, where P1 is the most critical.
  Think of each story as a standalone slice of functionality that can be:
  - Developed independently
  - Tested independently
  - Deployed independently
  - Demonstrated to users independently
-->

### User Story 1 - Registrar empleado (Priority: P1)

Como usuario del sistema, quiero registrar un empleado con nombre, dirección y teléfono para que el sistema genere una clave única con formato `EMP-<autonumérico>`.

**Why this priority**: Sin altas no existe información base para operar el resto del flujo.

**Independent Test**: Puede probarse creando un empleado válido y verificando que queda disponible para consulta posterior.

**Acceptance Scenarios**:

1. **Given** que se registra un empleado con nombre, dirección y teléfono válidos, **When** el usuario confirma el alta, **Then** el sistema crea el empleado y devuelve una `clave` generada con formato `EMP-<autonumérico>`.
2. **Given** que el usuario intenta incluir manualmente una `clave` en el alta, **When** envía la solicitud, **Then** el sistema ignora ese valor y mantiene la generación automática de la clave.
3. **Given** que alguno de los campos nombre, dirección o teléfono supera 100 caracteres, **When** el usuario intenta registrar el empleado, **Then** el sistema rechaza la solicitud indicando el límite permitido.

---

### User Story 2 - Consultar empleados (Priority: P2)

Como usuario del sistema, quiero consultar un empleado por clave y listar empleados para revisar la información registrada.

**Why this priority**: Permite validar y explotar los datos capturados en el alta.

**Independent Test**: Puede probarse consultando por clave existente/no existente y ejecutando el listado paginado validando `size=5` por defecto.

**Acceptance Scenarios**:

1. **Given** que existe un empleado registrado, **When** el usuario consulta por su clave en formato `EMP-<autonumérico>`, **Then** el sistema devuelve sus datos completos.
2. **Given** que no existe un empleado para una clave, **When** el usuario consulta por esa clave, **Then** el sistema devuelve un resultado de no encontrado.
3. **Given** que hay empleados registrados, **When** el usuario solicita el listado, **Then** el sistema devuelve una colección paginada de empleados.
4. **Given** que el usuario solicita listado sin enviar `size`, **When** el sistema procesa la petición, **Then** aplica `size=5` por defecto.

---

### User Story 3 - Actualizar y eliminar empleado (Priority: P3)

Como usuario del sistema, quiero actualizar nombre, dirección y teléfono de un empleado, y eliminarlo cuando ya no aplique.

**Why this priority**: Completa el ciclo de mantenimiento de datos (CRUD) una vez que alta y consulta están operativas.

**Independent Test**: Puede probarse modificando un empleado existente y luego eliminándolo, verificando que deja de estar disponible.

**Acceptance Scenarios**:

1. **Given** que existe un empleado, **When** el usuario actualiza nombre, dirección y teléfono con valores válidos, **Then** el sistema guarda los cambios.
2. **Given** que existe un empleado, **When** el usuario solicita su eliminación, **Then** el sistema elimina el registro y confirma la operación.
3. **Given** que el empleado no existe, **When** el usuario intenta actualizar o eliminar, **Then** el sistema informa que el recurso no fue encontrado.

---

### Edge Cases

- ¿Qué ocurre si `nombre`, `dirección` o `telefono` tienen exactamente 100 caracteres? Debe aceptarse.
- ¿Qué ocurre si `nombre`, `dirección` o `telefono` tienen 101 caracteres o más? Debe rechazarse.
- ¿Qué ocurre si en el alta se envía una `clave` manual? Debe ignorarse, conservando generación automática.
- ¿Qué ocurre si en consulta/update/delete la `clave` no sigue el formato `EMP-<autonumérico>`? Debe rechazarse por formato inválido.
- ¿Qué ocurre si el consecutivo autonumérico genera un valor ya existente para el mismo prefijo? Debe evitarse garantizando unicidad de la PK compuesta (`prefijo`, `consecutivo`).
- ¿Qué ocurre si se intenta cambiar la `clave` de un empleado existente? Debe rechazarse; la clave es identificador primario.
- ¿Qué ocurre al eliminar un empleado y volver a consultarlo por clave? Debe retornar no encontrado.
- ¿Qué ocurre si el cliente omite `size` al listar? Debe aplicarse `size=5` por defecto.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema DEBE permitir crear un empleado con los campos `nombre`, `dirección` y `telefono`.
- **FR-002**: El sistema DEBE generar automáticamente la `clave` del empleado en formato `EMP-<autonumérico>`.
- **FR-003**: El sistema DEBE modelar la identidad del empleado con una PK compuesta por `prefijo` y `consecutivo`, donde el `prefijo` es fijo `EMP-`.
- **FR-004**: El sistema DEBE exigir que `nombre`, `dirección` y `telefono` no superen 100 caracteres cada uno.
- **FR-005**: El sistema DEBE permitir consultar un empleado por su `clave`.
- **FR-006**: El sistema DEBE permitir listar empleados registrados mediante paginación.
- **FR-007**: El sistema DEBE permitir actualizar `nombre`, `dirección` y `telefono` de un empleado existente.
- **FR-008**: El sistema NO DEBE permitir modificar la `clave` de un empleado existente.
- **FR-009**: El sistema DEBE permitir eliminar un empleado por su `clave`.
- **FR-010**: El sistema DEBE devolver errores en formato JSON con estructura `{code, message, details}` y usar códigos de negocio del catálogo (`VALIDACION`, `FORMATO_CLAVE_INVALIDO`, `CLAVE_NO_EDITABLE`, `NO_ENCONTRADO`) según corresponda.
- **FR-011**: El sistema DEBE exigir que la `clave` de entrada para consulta/actualización/eliminación cumpla el patrón `EMP-<autonumérico>`.
- **FR-012**: El sistema NO DEBE permitir que el cliente defina manualmente la `clave` durante el alta; si el cliente envía `clave`, el sistema DEBE ignorarla y generar la `clave` oficial con formato `EMP-<autonumérico>`.
- **FR-014**: El endpoint de listado DEBE aceptar `page` y `size` como parámetros de paginación.
- **FR-015**: Si el cliente omite `size` en el listado, el sistema DEBE usar `size=5` por defecto.

### Key Entities *(include if feature involves data)*

- **Empleado**: Representa a una persona registrada en el sistema con identificador `clave` expuesto como `EMP-<autonumérico>`, derivado de una PK compuesta (`prefijo` = `EMP-`, `consecutivo` autonumérico), y atributos `nombre`, `dirección` y `telefono` (cada uno con máximo 100 caracteres).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de los intentos de alta con datos válidos finalizan con creación exitosa y con una `clave` generada en formato `EMP-<autonumérico>`.
- **SC-002**: El 100% de los intentos de alta o actualización con `nombre`, `dirección` o `telefono` mayores a 100 caracteres son rechazados con mensaje de validación.
- **SC-003**: El 100% de las consultas por `clave` existente en formato `EMP-<autonumérico>` retornan exactamente un empleado.
- **SC-004**: El 100% de las operaciones de eliminación exitosas hacen que una consulta posterior por esa `clave` retorne no encontrado.
- **SC-005**: El 100% de los intentos de consulta/actualización/eliminación con `clave` fuera del patrón `EMP-<autonumérico>` son rechazados con mensaje de formato inválido.
- **SC-006**: El 100% de las solicitudes de listado sin parámetro `size` aplican `size=5` por defecto.
