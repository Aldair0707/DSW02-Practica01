# Feature Specification: CRUD de Departamentos y Relacion con Empleados

**Feature Branch**: `003-crud-departamentos-relacion`  
**Created**: 2026-03-15  
**Status**: Draft  
**Input**: User description: "Implementar CRUD completo para Departamentos y vincular Empleado con Departamento mediante clave de departamento"

## Clarifications

### Session 2026-03-15

- Q: Como se debe realizar la asignacion/cambio de departamento de un empleado? -> A: Endpoint dedicado para asignar/cambiar departamento del empleado.
- Q: Como tratar empleados existentes al introducir la regla de departamento obligatorio? -> A: Migrarlos a un departamento por defecto DEP-0000 "Sin asignar" y exigir departamento valido en adelante.
- Q: Como debe comportarse la consulta de detalle de departamento respecto a empleados asociados? -> A: El detalle devuelve solo datos del departamento y los empleados se consultan en endpoint separado.
- Q: El nombre de departamento debe ser unico? -> A: No, puede repetirse; la unicidad obligatoria aplica solo a la clave.

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

### User Story 1 - Crear y mantener departamentos (Priority: P1)

Como administrador, quiero crear, consultar, actualizar y eliminar departamentos para mantener una estructura organizativa vigente y usable por el resto del sistema.

**Why this priority**: Sin la gestion de departamentos no existe la entidad base sobre la que se construye la relacion con empleados.

**Independent Test**: Puede probarse de forma independiente ejecutando operaciones CRUD sobre departamentos y validando reglas de negocio de clave y nombre.

**Acceptance Scenarios**:

1. **Given** que no existe un departamento con nombre "Recursos Humanos", **When** el administrador crea el departamento con ese nombre, **Then** el sistema registra el departamento con una clave unica en formato `DEP-XXXX`.
2. **Given** que existe un departamento, **When** el administrador actualiza su nombre a un valor valido, **Then** el sistema persiste el nuevo nombre sin cambiar la clave del departamento.
3. **Given** un departamento sin empleados asociados, **When** el administrador solicita su eliminacion, **Then** el sistema elimina el departamento y no lo devuelve en consultas posteriores.

---

### User Story 2 - Asignar empleados a departamentos (Priority: P2)

Como administrador, quiero asignar cada empleado a un departamento para que la informacion laboral quede clasificada y consistente.

**Why this priority**: Aporta valor directo al objetivo de relacionar empleados con departamentos y evita datos aislados.

**Independent Test**: Puede probarse editando empleados existentes para asignarles una clave de departamento valida y comprobando que la referencia queda guardada.

**Acceptance Scenarios**:

1. **Given** un empleado existente y un departamento existente, **When** el administrador asigna la clave de ese departamento al empleado, **Then** el empleado queda vinculado a dicho departamento.
2. **Given** un empleado existente, **When** el administrador intenta asignar una clave de departamento inexistente, **Then** el sistema rechaza la operacion con error 404.
3. **Given** un proceso de creacion de empleado, **When** se intenta informar manualmente `departamentoClave` en el alta inicial, **Then** el sistema no permite establecer ese valor en la creacion.

---

### User Story 3 - Consultar relacion departamento-empleados (Priority: P3)

Como administrador, quiero consultar departamentos junto con sus empleados para tomar decisiones operativas y verificar la distribucion del personal.

**Why this priority**: Es una capacidad de consulta que completa el valor del modelo relacional una vez existen datos y asignaciones.

**Independent Test**: Puede probarse creando al menos un departamento con empleados asignados y verificando que las consultas reflejan correctamente la relacion 1:N.

**Acceptance Scenarios**:

1. **Given** un departamento con empleados asignados, **When** el administrador consulta el detalle del departamento, **Then** puede identificar que empleados pertenecen a ese departamento.
2. **Given** un departamento sin empleados, **When** el administrador consulta su detalle, **Then** el sistema muestra el departamento sin elementos asociados y sin errores.

---

### Edge Cases

- Intento de crear un departamento con nombre de 1 caracter o mayor a 100 caracteres.
- Intento de crear o actualizar un departamento con nombre vacio o nulo.
- Intento de registrar dos departamentos con la misma clave de negocio.
- Intento de vincular un empleado a una clave de departamento inexistente.
- Intento de eliminar un departamento que tiene empleados asociados.
- Operaciones concurrentes de alta de departamento que podrian generar colision de clave.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema DEBE permitir crear departamentos con `nombre` obligatorio de 2 a 100 caracteres.
- **FR-002**: El sistema DEBE generar automaticamente una `clave` de negocio unica para cada departamento en formato `DEP-XXXX` (exactamente 4 digitos numericos).
- **FR-003**: El sistema DEBE impedir la creacion o actualizacion de departamentos cuando el `nombre` sea nulo, vacio, menor a 2 caracteres o mayor a 100.
- **FR-004**: El sistema DEBE garantizar la unicidad de `clave` de departamento y rechazar colisiones.
- **FR-004A**: El sistema DEBE permitir nombres de departamento repetidos; la unicidad de negocio aplica exclusivamente a `clave`.
- **FR-005**: El sistema DEBE permitir consultar el detalle de un departamento por su identificador funcional.
- **FR-006**: El sistema DEBE permitir listar departamentos con paginacion, usando `size=5` por defecto cuando no se especifique.
- **FR-007**: El sistema DEBE permitir actualizar el `nombre` de un departamento manteniendo inmutable su `clave` de negocio.
- **FR-008**: El sistema DEBE rechazar la eliminacion de un departamento cuando tenga empleados asociados.
- **FR-009**: El sistema DEBE impedir que un empleado quede vinculado a mas de un departamento a la vez.
- **FR-010**: El sistema DEBE permitir asignar a cada empleado una unica clave de departamento valida.
- **FR-011**: El sistema DEBE rechazar asignaciones de empleado hacia claves de departamento inexistentes.
- **FR-011A**: El sistema DEBE responder con error 404 cuando se intente asignar a un empleado una clave de departamento inexistente.
- **FR-012**: El sistema DEBE reflejar la relacion 1:N mostrando que un departamento puede tener cero o muchos empleados y cada empleado pertenece a un solo departamento.
- **FR-013**: Las rutas REST publicas afectadas por esta funcionalidad DEBEN estar versionadas por ruta mayor.
- **FR-014**: La funcionalidad DEBE permanecer protegida con autenticacion Basic en linea con la politica vigente del sistema.
- **FR-015**: El sistema DEBE incluir el campo `departamentoClave` en la representacion de empleado.
- **FR-016**: El sistema NO DEBE permitir editar manualmente `departamentoClave` durante la creacion inicial del empleado; la asignacion debe realizarse mediante endpoint dedicado posterior.
- **FR-017**: Cuando se rechace la eliminacion de un departamento con empleados asociados, el sistema DEBE responder con error de conflicto (409).
- **FR-018**: El sistema DEBE gestionar la asignacion y cambio de `departamentoClave` mediante un endpoint dedicado de empleado y no a traves del alta inicial.
- **FR-019**: El sistema DEBE migrar empleados existentes sin departamento al departamento por defecto `DEP-0000` con nombre `Sin asignar` durante la adopcion de la funcionalidad.
- **FR-020**: Tras la migracion inicial, todo empleado DEBE quedar con `departamentoClave` valido al finalizar su flujo de alta/asignacion.
- **FR-021**: El endpoint de detalle de departamento DEBE devolver solo datos del departamento, sin lista embebida de empleados.
- **FR-022**: El sistema DEBE exponer un endpoint versionado dedicado para listar empleados por departamento.

### Endpoint Scope (Versionado Obligatorio)

- Todas las rutas de esta funcionalidad DEBEN iniciar con `/api/v1/`.
- Operaciones CRUD de departamento esperadas bajo `/api/v1/departamentos`.
- Operaciones de consulta por clave de departamento esperadas bajo `/api/v1/departamentos/{clave}`.
- Operaciones de asignacion o cambio de departamento para empleado DEBEN realizarse en endpoint dedicado versionado de empleados (`PATCH /api/v1/empleados/{clave}/departamento`).
- No se aceptan rutas nuevas de esta funcionalidad sin version en path.

### Endpoints Requeridos

1. Crear departamento
   - Metodo y ruta: `POST /api/v1/departamentos`
   - Body de ejemplo:

```json
{
  "nombre": "Sistemas"
}
```

   - Respuesta esperada: `201 Created`

```json
{
  "clave": "DEP-1001",
  "nombre": "Sistemas"
}
```

2. Listar departamentos
   - Metodo y ruta: `GET /api/v1/departamentos`
   - Respuesta esperada: `200 OK`

```json
{
  "content": [
    {
      "clave": "DEP-1001",
      "nombre": "Sistemas"
    }
  ]
}
```

3. Obtener departamento por clave
   - Metodo y ruta: `GET /api/v1/departamentos/{clave}`
   - Respuesta esperada: `200 OK`

```json
{
  "clave": "DEP-1001",
  "nombre": "Sistemas"
}
```

4. Actualizar departamento
   - Metodo y ruta: `PUT /api/v1/departamentos/{clave}`
   - Body de ejemplo:

```json
{
  "nombre": "Tecnologia"
}
```

5. Eliminar departamento
   - Metodo y ruta: `DELETE /api/v1/departamentos/{clave}`
   - Respuesta esperada: `204 No Content`

6. Asignar o cambiar departamento de empleado
  - Metodo y ruta: `PATCH /api/v1/empleados/{clave}/departamento`
  - Body de ejemplo:

```json
{
  "departamentoClave": "DEP-1001"
}
```

  - Respuesta esperada: `200 OK`
  - Error esperado cuando departamento no existe: `404 Not Found`

7. Listar empleados de un departamento
   - Metodo y ruta: `GET /api/v1/departamentos/{clave}/empleados`
   - Respuesta esperada: `200 OK`

```json
{
  "content": [
    {
      "clave": "EMP-1001",
      "nombre": "Juan Perez",
      "departamentoClave": "DEP-1001"
    }
  ]
}
```

### Key Entities *(include if feature involves data)*

- **Departamento**: Unidad organizativa a la que se asignan empleados. Atributos clave: `clave` (negocio, unica, autogenerada con formato `DEP-XXXX`) y `nombre` (obligatorio, 2-100 caracteres).
- **Empleado**: Persona registrada en el sistema con una referencia de pertenencia organizativa mediante `departamentoClave`. Cada empleado pertenece a un unico departamento y su referencia de departamento no se define manualmente durante la creacion.

### Ejemplo de Respuesta de Empleado

```json
{
  "clave": "EMP-1001",
  "nombre": "Juan Perez",
  "departamentoClave": "DEP-1001"
}
```

### Ejemplo Conceptual de Relacion

- **Departamento**:
  - `DEP-1001` -> Sistemas
  - `DEP-1002` -> Recursos Humanos
- **Empleado**:
  - `EMP-1001` -> pertenece a `DEP-1001`
  - `EMP-1002` -> pertenece a `DEP-1001`
  - `EMP-1003` -> pertenece a `DEP-1002`

### Assumptions

- La eliminacion de departamentos con empleados asociados se bloquea para evitar empleados huerfanos y retorna conflicto.
- Las consultas de detalle de departamento incluyen la capacidad de identificar empleados asociados, directamente o mediante consulta relacionada.
- Las reglas de seguridad, versionado y paginacion existentes en el sistema se mantienen para los nuevos endpoints.
- Se permite un departamento tecnico por defecto `DEP-0000` (`Sin asignar`) para normalizar datos historicos al activar la regla de departamento obligatorio.
- El formato de clave de departamento usa exactamente 4 digitos en la parte numerica (`DEP-0001`, `DEP-1001`).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de departamentos creados en pruebas funcionales cumplen formato de clave `DEP-XXXX` y unicidad.
- **SC-002**: Al menos el 95% de operaciones CRUD de departamentos se completan exitosamente en el primer intento cuando los datos son validos.
- **SC-003**: El 100% de intentos con datos invalidos (nombre nulo, fuera de rango o clave inexistente al asignar empleado) son rechazados con mensajes de validacion comprensibles.
- **SC-004**: El 100% de empleados activos quedan asociados a exactamente un departamento valido tras la migracion/actualizacion de datos de la funcionalidad.
- **SC-005**: En validacion de usuarios internos, al menos el 90% confirma que puede localizar y verificar la pertenencia de un empleado a su departamento en menos de 30 segundos.
