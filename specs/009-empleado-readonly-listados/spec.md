# Feature Specification: Vista de Empleado Solo Lectura

**Feature Branch**: `009-empleado-readonly-listados`  
**Created**: 2026-03-26  
**Status**: Draft  
**Input**: User description: "Implement the feature specification based on the updated constitution. I want to build..."

## Clarifications

### Session 2026-03-26

- Q: Como expondremos la consulta para empleado: mismos endpoints o endpoints exclusivos? → A: Usar los mismos endpoints versionados y aplicar autorizacion por actor para permitir solo GET a EMPLEADO y denegar escrituras.
- Q: Que codigo HTTP usaremos para escritura denegada en empleado autenticado? → A: 403 Forbidden.
- Q: Como aplicaremos el modo solo lectura entre UI y backend? → A: Ocultar o deshabilitar acciones de escritura en UI para EMPLEADO y mantener denegacion obligatoria en backend.
- Q: Que nivel de campos visibles tendra EMPLEADO en listados? → A: Mostrar solo un subconjunto de campos no sensibles en listados de empleados y departamentos.
- Q: Cual sera el set minimo de campos no sensibles para EMPLEADO? → A: Empleado = id,nombre,correo,departamento; Departamento = id,nombre.

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

### User Story 1 - Ver listados al iniciar sesion (Priority: P1)

Como empleado autenticado, quiero ver el listado de empleados registrados y el listado de departamentos existentes para consultar informacion operativa sin editar datos.

**Why this priority**: Es el objetivo principal del requerimiento y entrega valor inmediato al empleado autenticado.

**Independent Test**: Puede validarse iniciando sesion como empleado y comprobando que ambos listados son visibles y navegables sin depender de funciones administrativas.

**Acceptance Scenarios**:

1. **Given** un empleado con credenciales validas, **When** inicia sesion correctamente, **Then** visualiza el listado de empleados registrados.
2. **Given** un empleado con sesion activa, **When** accede a la vista de consulta, **Then** visualiza el listado de departamentos existentes.

---

### User Story 2 - Operar en modo solo lectura (Priority: P2)

Como empleado autenticado, quiero que el sistema me limite a consulta para evitar cambios no autorizados en empleados y departamentos.

**Why this priority**: Garantiza cumplimiento de autorizacion y evita riesgos de integridad de datos.

**Independent Test**: Puede probarse con un empleado autenticado intentando crear, actualizar o eliminar y verificando denegacion en todos los casos.

**Acceptance Scenarios**:

1. **Given** un empleado autenticado, **When** intenta crear, editar o eliminar empleados o departamentos, **Then** el sistema rechaza la accion por permisos insuficientes.

---

### User Story 3 - Mensajes claros ante acciones no permitidas (Priority: P3)

Como empleado autenticado, quiero recibir una respuesta clara cuando una accion no esta permitida para entender por que no puedo modificar informacion.

**Why this priority**: Reduce confusion, evita reintentos innecesarios y mejora la experiencia de uso.

**Independent Test**: Puede validarse forzando una accion restringida y verificando que se muestra un mensaje comprensible para el usuario.

**Acceptance Scenarios**:

1. **Given** un empleado autenticado, **When** intenta una accion de escritura no permitida, **Then** recibe una respuesta de autorizacion denegada con mensaje entendible.

---

### Edge Cases

- Empleado autenticado pero sin registros de empleados disponibles: se muestra estado vacio sin error.
- Empleado autenticado pero sin departamentos disponibles: se muestra estado vacio sin error.
- Sesion de empleado expirada al consultar listados: se rechaza acceso y se solicita nueva autenticacion.
- Empleado intenta acceso directo a operaciones de escritura por URL o solicitud manual: acceso denegado.

## Requirements *(mandatory)*

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right functional requirements.
-->

### Functional Requirements

- **FR-001**: El sistema DEBE permitir que un empleado autenticado acceda a una vista de consulta con listado de empleados registrados.
- **FR-002**: El sistema DEBE permitir que un empleado autenticado acceda a una vista de consulta con listado de departamentos existentes.
- **FR-003**: El sistema DEBE mantener el acceso de empleado en modo solo lectura para empleados y departamentos.
- **FR-004**: El sistema DEBE denegar para actor empleado cualquier operacion de creacion, actualizacion o eliminacion sobre empleados y departamentos.
- **FR-005**: El sistema DEBE mostrar una respuesta clara cuando una accion es denegada por permisos insuficientes.
- **FR-006**: El sistema DEBE conservar la distincion de actor autenticado (ADMIN vs EMPLEADO) para aplicar reglas de acceso correspondientes.
- **FR-007**: El sistema DEBE permitir estados vacios en listados sin mostrar errores tecnicos al usuario.
- **FR-008**: El sistema DEBE exigir sesion valida para consultar listados y bloquear acceso cuando la sesion no sea valida.
- **FR-009**: El sistema DEBE reutilizar los endpoints versionados existentes de empleados y departamentos, aplicando autorizacion por actor para habilitar solo operaciones GET a EMPLEADO.
- **FR-010**: La UI DEBE ocultar o deshabilitar acciones de creacion, actualizacion y eliminacion cuando el actor autenticado sea EMPLEADO.
- **FR-011**: El backend DEBE mantener la validacion de autorizacion para escrituras aunque la UI o un cliente externo intente invocar esas operaciones.
- **FR-012**: Para el actor EMPLEADO, los listados DEBEN exponer solo campos no sensibles definidos: Empleado (`id`, `nombre`, `correo`, `departamento`) y Departamento (`id`, `nombre`).

- **FR-API-VER**: Los endpoints REST publicos DEBEN incluir version explicita en ruta (`/api/v{major}/...`).
- **FR-API-PAG**: Los endpoints de coleccion/listado DEBEN soportar paginacion y usar `size=5` por defecto cuando no se indique.
- **FR-API-AUTH-DEF**: En local/dev, las credenciales Basic Auth por defecto DEBEN ser `admin` y `admin123`, con sobreescritura por entorno.
- **FR-API-AUTH-PROFILE**: Los endpoints de perfil autenticado (por ejemplo, `/auth/me`) DEBEN declarar tipo de actor (ADMIN/EMPLEADO) y no representar admin como empleado.
- **FR-API-EMP-LOGIN**: La autenticacion de empleado DEBE soportar correo+contrasena y documentar respuestas de exito y error.
- **FR-API-EMP-READONLY**: Tras login de empleado, el sistema DEBE permitir consulta de listados de empleados y departamentos y DEBE denegar create/update/delete para actor empleado.
- **FR-API-EMP-FORBIDDEN**: Cuando un empleado autenticado intente create/update/delete sobre empleados o departamentos, el sistema DEBE responder con `403 Forbidden`.

### Key Entities *(include if feature involves data)*

- **Empleado**: Persona registrada en el sistema con datos de identificacion y asociacion a departamento; para esta feature se consume en modo consulta.
- **Departamento**: Unidad organizativa registrada en el sistema; para esta feature se consume en modo consulta.
- **Sesion de Empleado**: Estado de autenticacion del actor empleado que habilita permisos de lectura y restringe escritura.

## Assumptions

- El login de empleado por correo+contrasena ya se encuentra implementado y operativo.
- Los listados de empleados y departamentos se consultan reutilizando endpoints versionados existentes, sin crear rutas exclusivas para empleado.
- La gestion CRUD completa para empleados y departamentos permanece reservada a perfiles con permisos administrativos.
- La denegacion de acciones no permitidas para empleado autenticado se comunica con `403 Forbidden` y mensaje legible.

## Success Criteria *(mandatory)*

<!--
  ACTION REQUIRED: Define measurable success criteria.
  These must be technology-agnostic and measurable.
-->

### Measurable Outcomes

- **SC-001**: Al menos 95% de empleados autenticados visualizan ambos listados (empleados y departamentos) en menos de 5 segundos desde el inicio de sesion exitoso.
- **SC-002**: El 100% de intentos de create/update/delete realizados por actor empleado sobre empleados o departamentos son denegados por autorizacion.
- **SC-003**: Al menos 90% de usuarios de prueba identifican correctamente que su perfil es de solo lectura sin soporte adicional.
- **SC-004**: El 100% de escenarios de aceptacion definidos para las historias P1 y P2 pasan en validacion funcional.
