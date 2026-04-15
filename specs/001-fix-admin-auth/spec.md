# Feature Specification: Recuperar Autenticacion Admin Bootstrap

**Feature Branch**: `001-fix-admin-auth`  
**Created**: 2026-03-19  
**Status**: Draft  
**Input**: User description: "tengo problemas con mi usuario admin, al parecer no está como autenticado, porque cuando lo uso en postman el endpoint api/v1/empleados/auth/me, sale como usuario no encontrado pero admin está como auth basic si recuerdas que así lo he pedido, haremos la modificación en el feature 005"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Acceso De Admin A Perfil Autenticado (Priority: P1)

Como administrador bootstrap, quiero autenticarme correctamente y consultar mi perfil autenticado para poder operar el sistema sin bloqueos.

**Why this priority**: Si el usuario admin no es reconocido, se rompe el flujo de acceso principal de administracion y bloquea pruebas manuales y operacion local.

**Independent Test**: Puede validarse ejecutando autenticacion Basic con credenciales admin de entorno local y comprobando que el perfil autenticado se obtiene sin error de usuario no encontrado.

**Acceptance Scenarios**:

1. **Given** credenciales admin validas en entorno local, **When** se solicita el perfil autenticado, **Then** el sistema confirma autenticacion y devuelve el perfil esperado del actor administrativo.
2. **Given** credenciales admin invalidas, **When** se solicita el perfil autenticado, **Then** el sistema rechaza acceso con error de autenticacion consistente.

---

### User Story 2 - Coherencia Entre Actores Bootstrap Y Empleados (Priority: P2)

Como responsable del sistema, quiero que la autenticacion distinga correctamente entre actor administrativo bootstrap y empleados para evitar falsos "usuario no encontrado".

**Why this priority**: El problema reportado surge por falta de coherencia al resolver identidades autenticadas en distintos tipos de actor.

**Independent Test**: Puede probarse autenticando un admin bootstrap y un empleado activo, verificando que ambos son resueltos segun sus reglas y sin confundir identidades.

**Acceptance Scenarios**:

1. **Given** un admin bootstrap con credenciales validas, **When** se resuelve el usuario autenticado, **Then** se identifica como actor administrativo valido.
2. **Given** un empleado activo con credenciales validas, **When** se resuelve el usuario autenticado, **Then** se identifica como empleado valido y no como actor bootstrap.

---

### User Story 3 - Compatibilidad Con Flujo Frontend Admin (Priority: P3)

Como administrador de frontend, quiero que el flujo de login y acceso CRUD siga funcionando con admin para poder gestionar empleados desde la interfaz.

**Why this priority**: La feature 005 depende de que admin pueda autenticarse de forma estable para habilitar el panel CRUD.

**Independent Test**: Puede validarse iniciando sesion en frontend con admin y confirmando acceso a pantalla de gestion sin errores de identidad.

**Acceptance Scenarios**:

1. **Given** el frontend en flujo administrativo, **When** se inicia sesion con admin valido, **Then** se habilita la pantalla CRUD y se permiten operaciones protegidas.
2. **Given** la sesion admin invalida o no autorizada, **When** el frontend intenta acceder a recursos protegidos, **Then** se fuerza reautenticacion sin estados inconsistentes.

### Edge Cases

- Credenciales admin correctas con diferencias de mayusculas/minusculas o espacios no deseados en entrada.
- Entorno local con credenciales admin sobreescritas por variables de entorno.
- Error intermitente de red durante consulta de perfil autenticado que no debe confundirse con "usuario no encontrado".
- Intento de autenticacion con usuario existente pero no autorizado para flujo administrativo.
- Convivencia de autenticacion de admin bootstrap y autenticacion por correo de empleado en el mismo entorno.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST reconocer al usuario administrativo bootstrap como identidad autenticable valida en entorno local y de desarrollo.
- **FR-002**: El sistema MUST permitir que una autenticacion valida de admin obtenga perfil autenticado sin devolver error de "usuario no encontrado".
- **FR-003**: El sistema MUST mantener respuestas de error coherentes cuando las credenciales sean invalidas o el actor no tenga autorizacion.
- **FR-004**: El sistema MUST resolver correctamente el tipo de actor autenticado (admin bootstrap vs empleado) sin mezclar reglas de identidad.
- **FR-005**: El sistema MUST conservar compatibilidad con el flujo administrativo de frontend que depende de login admin para habilitar CRUD.
- **FR-006**: El sistema MUST mantener configurables por entorno las credenciales de desarrollo, preservando `admin` y `admin123` como referencia operativa local.
- **FR-007**: El sistema MUST registrar y comunicar errores de autenticacion de manera que permita diferenciar credencial invalida, actor no autorizado y falla de conectividad.
- **FR-008**: El sistema MUST evitar regresiones en autenticacion de empleados activos mientras corrige la autenticacion del admin bootstrap.

*Additional API governance requirements (when REST applies):*

- **FR-API-VER**: Public REST endpoints MUST include explicit version in path (`/api/v{major}/...`).
- **FR-API-PAG**: Collection/list endpoints MUST support pagination and default to `size=5` when omitted.
- **FR-API-AUTH-DEF**: Local/dev Basic Auth defaults MUST be `admin` (user) and `admin123` (password), with env override support.

### Key Entities *(include if feature involves data)*

- **ActorAdministrativoBootstrap**: Identidad administrativa preconfigurada para acceso operativo local, con credenciales de referencia y permisos de administracion.
- **PerfilAutenticado**: Representacion del actor autenticado utilizada por clientes para habilitar funcionalidades protegidas.
- **SesionAutenticada**: Estado resultante de una autenticacion valida que habilita operaciones de negocio protegidas.
- **ResultadoAutenticacion**: Resultado observable de cada intento de autenticacion, incluyendo exito o causa de rechazo.

## Assumptions

- La correccion se implementa como ajuste del flujo de autenticacion existente, sin introducir un mecanismo nuevo de login.
- El problema principal es de resolucion de identidad admin bootstrap y no de disponibilidad general del endpoint de autenticacion.
- El flujo CRUD del frontend del feature 005 se mantiene como consumidor de autenticacion administrativa existente.
- Las credenciales locales pueden ser sobreescritas por entorno, pero el comportamiento esperado sigue siendo consistente.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de intentos con credenciales admin validas en entorno local obtiene perfil autenticado sin error de "usuario no encontrado".
- **SC-002**: El 100% de intentos con credenciales admin invalidas es rechazado con respuesta de autenticacion consistente.
- **SC-003**: Al menos el 95% de inicios de sesion administrativos en frontend completa acceso a pantalla CRUD en menos de 30 segundos.
- **SC-004**: El 100% de pruebas de autenticacion de empleados activos continua funcionando tras la correccion de admin bootstrap.
