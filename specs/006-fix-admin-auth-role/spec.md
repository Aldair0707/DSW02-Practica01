# Feature Specification: Corregir Rol Admin En Login

**Feature Branch**: `006-fix-admin-auth-role`  
**Created**: 2026-03-22  
**Status**: Ready for Implementation  
**Input**: User description: "tengo problemas con mi backend, al parecer cuando quiero iniciar sesion desde el front con mi usuario administrador que es admin y su contrasena admin123, no me deja porque piensa mi api que admin es un empleado. admin es el administrador y es el que utilizara todos los endpoints para el CRUD, entonces primero estamos trabajando el frontend para aministrador."

## Clarifications

### Session 2026-03-25

- Q: ¿Cómo debe cubrirse FR-009 (trazabilidad de rechazos de acceso administrativo) en esta feature? → A: D - No registrar rechazos en esta feature (dejarlo para otra).
- Q: ¿Cuál es el alcance de FR-008 sobre paginación/versionado en endpoints de listado? → A: B - Aplicar a todos los listados actuales y futuros del dominio admin; los tres listados actuales son obligatorios mínimos.
- Q: ¿Cómo se diferencia de forma contractual autenticación inválida vs falta de autorización? → A: C - Diferenciar por HTTP y código funcional fijo: AUTH_INVALIDA para 401 y NO_AUTORIZADO para 403, con mensaje genérico seguro.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Login Correcto De Administrador (Priority: P1)

Como administrador quiero iniciar sesion con mis credenciales admin para poder acceder al frontend de gestion y operar el CRUD sin bloqueo por clasificacion incorrecta.

**Why this priority**: Si el login de administrador falla, el flujo completo de administracion queda inutilizable y bloquea el trabajo del equipo.

**Independent Test**: Puede probarse iniciando sesion con `admin/admin123` y verificando que el acceso administrativo se concede sin que el sistema lo trate como empleado.

**Acceptance Scenarios**:

1. **Given** que no hay sesion activa y existe una cuenta administrativa valida, **When** se envian credenciales correctas de administrador, **Then** el sistema autentica y concede acceso al area administrativa.
2. **Given** que no hay sesion activa, **When** se envian credenciales de un empleado no administrador, **Then** el sistema rechaza acceso al area administrativa y mantiene protegidos los endpoints de gestion.

---

### User Story 2 - Autorizacion Coherente Para CRUD Admin (Priority: P2)

Como administrador autenticado quiero consumir los endpoints CRUD habilitados para mi perfil para gestionar empleados sin errores de autorizacion por mezcla de roles.

**Why this priority**: Permite ejecutar el objetivo funcional del frontend admin y evita regresiones en operaciones de negocio clave.

**Independent Test**: Puede probarse autenticando como admin y ejecutando al menos una operacion de lectura y una de escritura de CRUD verificando autorizacion exitosa.

**Acceptance Scenarios**:

1. **Given** que el administrador esta autenticado correctamente, **When** solicita un endpoint CRUD permitido para administracion, **Then** la operacion se autoriza de acuerdo con su rol.
2. **Given** que un usuario sin rol administrador intenta consumir endpoints CRUD de administracion, **When** realiza la solicitud, **Then** el sistema deniega la operacion.

---

### User Story 3 - Mensajes Claros Ante Credenciales O Rol Invalidos (Priority: P3)

Como usuario del frontend quiero recibir retroalimentacion clara cuando falle login o autorizacion para entender si el problema es credencial invalida o falta de permisos.

**Why this priority**: Reduce confusion operativa y acelera diagnostico para soporte funcional.

**Independent Test**: Puede probarse ejecutando intentos fallidos con credenciales incorrectas y con perfil no autorizado, verificando mensajes diferenciados y accionables.

**Acceptance Scenarios**:

1. **Given** que se ingresan credenciales invalidas, **When** se intenta iniciar sesion, **Then** el sistema informa fallo de autenticacion sin exponer informacion sensible.
2. **Given** que el usuario esta autenticado pero no tiene permiso de administrador, **When** intenta entrar o consumir recursos administrativos, **Then** el sistema informa falta de autorizacion de forma explicita.

### Edge Cases

- El usuario `admin` existe, pero esta deshabilitado o bloqueado temporalmente.
- Existen credenciales validas de empleado con identificador similar a `admin` y el sistema debe evitar ambiguedad de rol.
- Una sesion cambia de estado durante una operacion CRUD (por ejemplo, expira o se invalida).
- El frontend intenta reusar credenciales almacenadas de un empleado en flujo exclusivo de administrador.
- Se reciben multiples intentos consecutivos de login fallido y el sistema debe responder de forma consistente.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema DEBE autenticar al usuario administrador `admin` con la contrasena `admin123` en entorno local por defecto, salvo configuracion explicita distinta del entorno.
- **FR-002**: El sistema DEBE clasificar al usuario autenticado segun su rol efectivo y NO debe tratar al administrador como empleado durante el login ni durante la autorizacion posterior.
- **FR-003**: El sistema DEBE permitir acceso al frontend de administracion solo a usuarios con rol administrador autenticado.
- **FR-004**: El sistema DEBE permitir a usuarios administradores consumir los endpoints CRUD administrativos definidos para la gestion de empleados.
- **FR-005**: El sistema DEBE denegar acceso a endpoints CRUD administrativos a usuarios autenticados sin rol administrador.
- **FR-006**: El sistema DEBE devolver respuesta consistente para distinguir autenticacion invalida de falta de permisos usando: HTTP 401 + codigo funcional `AUTH_INVALIDA` para credenciales invalidas, y HTTP 403 + codigo funcional `NO_AUTORIZADO` para accesos sin permisos, con mensajes genericos que no expongan informacion sensible.
- **FR-007**: El sistema DEBE mantener separadas las reglas de autenticacion y autorizacion de administrador respecto a las de empleado para evitar cruces de identidad.
- **FR-008**: El sistema DEBE conservar la gobernanza de API vigente en todos los endpoints de listado actuales y futuros del dominio admin: rutas publicas versionadas (`/api/v{major}/...`) y paginacion con valor por defecto `size=5` cuando aplique. Como minimo obligatorio en esta feature, DEBE cumplirse en `/api/v1/empleados`, `/api/v1/departamentos` y `/api/v1/departamentos/{clave}/empleados`.
- **FR-009**: Esta feature NO incluye persistencia ni logging adicional de rechazos de acceso administrativo; esa trazabilidad queda fuera de alcance y se abordara en una feature posterior.

### Key Entities *(include if feature involves data)*

- **Administrador**: Usuario con privilegios para iniciar sesion en el frontend administrativo y operar endpoints CRUD de gestion.
- **Empleado**: Usuario no administrador que puede autenticarse segun su flujo, pero no tiene permiso para operaciones CRUD administrativas.
- **Sesion De Acceso**: Estado de autenticacion asociado a un usuario que determina su capacidad de invocar operaciones protegidas.
- **Resultado De Autorizacion**: Decision funcional de permitir o denegar acceso a una accion administrativa segun rol y estado de sesion.

## Assumptions & Dependencies

- El frontend actual ya consume un flujo de login administrativo y solo requiere respuestas coherentes de backend para habilitar acceso.
- La cuenta administrativa por defecto en desarrollo/local es `admin` con `admin123`, con posibilidad de sobreescritura por configuracion de entorno.
- El alcance de esta feature se centra en autenticacion/autorizacion del perfil administrador y no redefine el CRUD de empleados.
- Existe al menos un mecanismo de trazabilidad operativa para eventos de acceso (auditoria o bitacora equivalente).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de los intentos de login con credenciales validas de administrador en entorno local permite acceso al area administrativa.
- **SC-002**: El 100% de intentos de usuarios no administradores para acceder a endpoints CRUD administrativos es rechazado correctamente.
- **SC-003**: Al menos el 95% de las pruebas funcionales del flujo "login admin + operacion CRUD" se completan sin errores de clasificacion de rol.
- **SC-004**: El 100% de respuestas de acceso fallido reporta una causa funcional interpretable por frontend (autenticacion invalida o autorizacion denegada).
