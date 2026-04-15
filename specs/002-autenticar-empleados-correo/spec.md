# Feature Specification: Autenticacion de empleados por correo y contrasena

**Feature Branch**: `[002-autenticar-empleados-correo]`  
**Created**: 2026-03-11  
**Status**: Draft  
**Input**: User description: "ahora quiero autenticar los empleados con correo y contrasena"

## Clarifications

### Session 2026-03-12

- Q: Como se trata el estado "bloqueada" en esta spec? -> A: Fuera de alcance; esta spec solo usa activa/inactiva.
- Q: El cambio de contrasena se implementa en este feature? -> A: No; queda fuera de alcance y se abordara en un feature futuro.
- Q: La unicidad del correo es global o solo para cuentas activas? -> A: Unicidad global para todas las cuentas.
- Q: En que responses se expone el correo del empleado? -> A: Solo en el endpoint de perfil autenticado (`GET /api/v1/empleados/auth/me`).
- Q: Se modela sesion de aplicacion o solo vigencia de credenciales? -> A: No hay sesion de aplicacion; el acceso depende de credenciales validas y cuenta activa.
- Q: Quien es el actor autorizado para `PATCH /api/v1/empleados/{clave}/estado` en este feature? -> A: Solo el usuario administrativo bootstrap `admin`.
- Q: Donde se debe reflejar esta regla de autorizacion? -> A: Tambien en OpenAPI para evitar ambiguedad tecnica.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Iniciar sesion como empleado (Priority: P1)

Como empleado, quiero autenticarme con mi correo y contrasena para poder acceder a funcionalidades que requieren identidad de empleado.

**Why this priority**: Sin autenticacion no existe control de acceso por empleado ni trazabilidad de acciones.

**Independent Test**: Se valida de forma independiente cuando un empleado con credenciales validas puede iniciar sesion y acceder a un recurso protegido de su perfil.

**Acceptance Scenarios**:

1. **Given** un empleado activo con correo y contrasena validos, **When** envia sus credenciales, **Then** el sistema confirma autenticacion exitosa y habilita acceso autenticado.
2. **Given** un empleado con correo correcto y contrasena incorrecta, **When** intenta iniciar sesion, **Then** el sistema rechaza el acceso con un mensaje de error generico.

---

### User Story 2 - Gestionar errores de autenticacion (Priority: P2)

Como empleado, quiero recibir respuestas claras cuando falle la autenticacion para corregir mi intento sin exponer informacion sensible.

**Why this priority**: Reduce frustracion del usuario y evita filtrar datos que faciliten ataques por enumeracion de cuentas.

**Independent Test**: Se prueba enviando credenciales invalidas en distintos casos y verificando que la respuesta sea consistente, segura y comprensible.

**Acceptance Scenarios**:

1. **Given** un correo no registrado, **When** se intenta iniciar sesion, **Then** el sistema responde con el mismo tipo de error que para credenciales invalidas.
2. **Given** un payload de alta de empleado con correo invalido o sin contrasena inicial, **When** se envia la solicitud, **Then** el sistema rechaza la solicitud con error de validacion.

---

### User Story 3 - Mantener acceso estable con credenciales vigentes (Priority: P3)

Como empleado, quiero acceder a recursos protegidos en cada solicitud cuando mis credenciales sean validas y mi cuenta permanezca activa.

**Why this priority**: Mejora la continuidad de uso y disminuye friccion operativa una vez completado el inicio de sesion.

**Independent Test**: Se valida al consumir multiples recursos protegidos con correo y contrasena validos y cuenta activa, y confirmar denegacion cuando las credenciales dejan de ser validas o la cuenta se inactiva.

**Acceptance Scenarios**:

1. **Given** un empleado con correo y contrasena validos y cuenta activa, **When** consume un recurso protegido, **Then** el sistema permite el acceso.
2. **Given** un empleado con correo o contrasena invalidos, o con cuenta inactiva, **When** intenta consumir un recurso protegido, **Then** el sistema deniega el acceso.

---

### Edge Cases

- Intentos repetidos con credenciales incorrectas desde una misma cuenta o contexto de acceso.
- Correo con mayusculas/minusculas mezcladas y espacios accidentales al inicio o final.
- Cuenta de empleado inactiva intentando iniciar sesion.
- Solicitudes de autenticacion concurrentes para la misma cuenta desde distintos dispositivos.
- Intento de alta de empleado sin contrasena inicial.
- Intento de alta de contrasena con longitud menor a 8 o mayor a 72 caracteres.
- Intento de cambio de estado de cuenta por actor no autorizado.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST permitir autenticacion de empleados usando correo y contrasena.
- **FR-002a**: El sistema MUST validar formato de correo y presencia de contrasena en payloads de alta de empleado y rechazar entradas invalidas con error de validacion.
- **FR-002b**: El sistema MUST evaluar credenciales de autenticacion HTTP Basic y rechazar credenciales invalidas con error de autenticacion.
- **FR-003**: El sistema MUST rechazar autenticaciones con credenciales invalidas mediante respuesta generica que no revele si el correo existe.
- **FR-004**: El sistema MUST permitir acceso a recursos protegidos unicamente cuando el empleado se autentique con correo y contrasena validos y su cuenta este activa.
- **FR-005**: El sistema MUST denegar acceso a recursos protegidos cuando el correo o la contrasena sean invalidos, o cuando la cuenta del empleado este inactiva.
- **FR-006**: El sistema MUST registrar eventos de autenticacion exitosos y fallidos con campos minimos de auditoria: timestamp, resultado, identificador de empleado o correo normalizado, origen de solicitud y codigo de motivo.
- **FR-007**: El sistema MUST requerir que solo empleados activos puedan autenticarse.
- **FR-008**: El sistema MUST aplicar autenticacion HTTP Basic en rutas de negocio, conforme a la constitucion del proyecto.
- **FR-009**: El sistema MUST mantener configurables por entorno las credenciales de autenticacion por defecto para desarrollo, incluyendo valores iniciales `admin` y `admin123`.
- **FR-010**: Los endpoints publicos REST de esta funcionalidad MUST usar versionado explicito en ruta (`/api/v{major}/...`).
- **FR-011**: Todo endpoint de listado publico relacionado con esta funcionalidad MUST soportar paginacion y usar `size=5` por defecto cuando no se provea.
- **FR-012**: El alta de empleado MUST requerir contrasena inicial como dato obligatorio de entrada.
- **FR-013**: Este feature MUST excluir funcionalidad de cambio de contrasena; dicha capacidad se definira en un feature posterior.
- **FR-014**: La contrasena de empleado MUST cumplir una politica minima de longitud entre 8 y 72 caracteres.
- **FR-015**: El sistema MUST almacenar solo hash de contrasena y nunca devolver contrasena en responses.
- **FR-016**: El sistema MUST garantizar unicidad global del correo de empleado en todas las cuentas (activas e inactivas).
- **FR-017**: El sistema MUST exponer el correo de empleado solo en el endpoint de perfil autenticado (`GET /api/v1/empleados/auth/me`) y no en responses generales de empleado.
- **FR-018**: El sistema MUST permitir la gestion de estado de cuenta (activar/inactivar) solo por el usuario administrativo bootstrap `admin`, mediante endpoint versionado `PATCH /api/v1/empleados/{clave}/estado`, aplicando reglas: validar formato de `clave`, rechazar cambios sobre empleado inexistente, rechazar solicitudes no autenticadas/no autorizadas y mantener idempotencia cuando el estado solicitado coincide con el estado actual. Cuando el estado solicitado coincida con el actual, la respuesta MUST ser `200` con payload consistente y sin cambios de estado.

### Key Entities

- **Empleado**: Persona registrada para operar en el sistema. Atributos relevantes para esta funcionalidad: identificador, correo, estado (activo/inactivo) y credencial de acceso.
	El correo de empleado es unico de forma global.

- **Credencial de Empleado**: Conjunto de datos de autenticacion asociado a un empleado (correo y contrasena), con reglas de validacion y vigencia.
	Regla minima para contrasena: entre 8 y 72 caracteres.
- **Evento de Autenticacion**: Registro auditable de intentos de inicio de sesion exitosos o fallidos, incluyendo marca temporal y resultado.

## Assumptions

- El alcance de este feature cubre autenticacion (inicio de sesion) y control de acceso asociado, no la recuperacion de contrasena.
- El correo de empleado es unico de forma global, sin reutilizacion entre cuentas activas e inactivas.
- El sistema mantendra mensajes de error orientados al usuario final sin exponer detalles de seguridad.
- No se modela sesion de aplicacion en este feature; el acceso depende de credenciales vigentes y estado activo de cuenta.
- El cambio de contrasena queda fuera de alcance de este feature y se definira en una especificacion futura.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El percentil 95 de autenticacion y consulta de perfil autenticado MUST ser menor o igual a 200 ms bajo carga baja/moderada (hasta 50 rps) en ambiente de integracion.
- **SC-002**: Al menos 99% de intentos con credenciales invalidas son rechazados con respuesta de error consistente y sin revelar existencia de cuenta.
- **SC-003**: El 100% de accesos a recursos protegidos sin autenticacion valida son bloqueados durante las pruebas de aceptacion.
- **SC-004**: Al menos 90% de empleados que inician sesion exitosamente pueden completar una accion protegida en el primer intento.
