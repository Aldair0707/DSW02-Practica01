# Feature Specification: Login Frontend con Admin

**Feature Branch**: `004-login-frontend-admin`  
**Created**: 2026-03-18  
**Status**: Draft  
**Input**: User description: "quiero hacer correctamente el login desde el front con admin/admin123 para entrar al sistema"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Login exitoso de administrador (Priority: P1)

Como usuario en entorno local, quiero iniciar sesion desde el frontend con `admin` y `admin123` para poder entrar al sistema y usar las vistas protegidas.

**Why this priority**: Sin login funcional no hay acceso a ninguna operacion de negocio desde UI.

**Independent Test**: Se valida de forma independiente levantando backend + frontend, autenticando con `admin/admin123` y comprobando acceso a listados.

**Acceptance Scenarios**:

1. **Given** el backend esta disponible y acepta Basic Auth, **When** ingreso `admin` y `admin123` en el formulario y envio, **Then** veo el panel autenticado y se cargan empleados/departamentos.
2. **Given** una sesion autenticada como admin, **When** navego entre secciones de empleados y departamentos, **Then** las llamadas usan credenciales validas y responden `200`.

---

### User Story 2 - Manejo de credenciales invalidas (Priority: P2)

Como usuario, quiero recibir un error claro cuando las credenciales son invalidas para corregirlas sin quedar en un estado inconsistente.

**Why this priority**: Reduce friccion de uso y evita falsa percepcion de caida del sistema.

**Independent Test**: Se prueba enviando usuario/password incorrectos y verificando que no se habilita el panel autenticado.

**Acceptance Scenarios**:

1. **Given** credenciales incorrectas, **When** envio el formulario de login, **Then** el frontend muestra mensaje de error y no carga recursos protegidos.

---

### User Story 3 - Cierre de sesion en frontend (Priority: P3)

Como usuario autenticado, quiero cerrar sesion desde el frontend para volver a estado no autenticado.

**Why this priority**: Completa el ciclo de uso y permite cambio de usuario en pruebas locales.

**Independent Test**: Se valida iniciando sesion, ejecutando logout y comprobando retorno al formulario de login.

**Acceptance Scenarios**:

1. **Given** un usuario autenticado, **When** presiono "Cerrar sesion", **Then** la UI vuelve al estado de login y limpia datos en memoria.

### Edge Cases

- Backend caido o no accesible: el frontend debe informar error de conectividad sin romper la UI.
- Usuario vacio o password vacio: el submit no debe ejecutarse y debe mostrarse feedback de validacion.
- Usuario autenticado pero deshabilitado: debe mostrarse mensaje de error (401/403) y no entrar.
- Usuario o password con uno o mas espacios: no se debe permitir el login.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El frontend DEBE mostrar un formulario de login con campos de usuario y contrasena.
- **FR-002**: El frontend DEBE autenticar contra `GET /api/v1/empleados/auth/me` usando encabezado `Authorization: Basic <base64(user:password)>`.
- **FR-003**: El frontend DEBE permitir login exitoso con credenciales de desarrollo por defecto `admin` / `admin123`.
- **FR-004**: El frontend DEBE bloquear el acceso a vistas operativas cuando no exista sesion autenticada en memoria.
- **FR-005**: El frontend DEBE mostrar feedback claro ante `401/403` o errores de red durante login.
- **FR-006**: El frontend DEBE proveer accion de logout que limpie estado de sesion y datos cargados.
- **FR-007**: El frontend DEBE reutilizar las credenciales autenticadas para llamadas protegidas posteriores en la sesion actual.
- **FR-008**: El feature NO DEBE introducir cambios de contrato backend ni nuevos endpoints.
- **FR-009**: El frontend DEBE bloquear intento de login cuando `username` o `password` contengan espacios.
- **FR-010**: El frontend DEBE bloquear intento de login cuando `username` o `password` esten vacios o solo contengan espacios.

*Additional API governance requirements (when REST applies):*

- **FR-API-VER**: Se consumen rutas versionadas existentes (`/api/v1/...`) sin excepciones.
- **FR-API-PAG**: Los listados consumidos en frontend respetan paginacion backend y default `size=5`.
- **FR-API-AUTH-DEF**: Se valida en entorno local que `admin` / `admin123` permite acceso, manteniendo soporte de override por variables de entorno backend.

### Key Entities *(include if feature involves data)*

- **CredencialesLogin**: `username`, `password`; datos de entrada para construir Basic Auth.
- **SesionFrontend**: estado en memoria que representa usuario autenticado y habilita vistas protegidas.
- **PerfilAutenticado**: respuesta de `auth/me` con datos del usuario autenticado (`clave`, `correo`, `nombre`, `activo`, etc.).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de intentos con `admin/admin123` en entorno local acceden al panel en menos de 2 segundos (backend disponible).
- **SC-002**: El 100% de intentos con credenciales invalidas permanece en pantalla de login con mensaje de error visible.
- **SC-003**: Tras logout, el 100% de vistas protegidas vuelven a requerir autenticacion.
- **SC-004**: No se agregan endpoints backend nuevos ni se rompe el contrato OpenAPI existente.
- **SC-005**: El 100% de intentos de login con espacios o campos vacios deben quedar bloqueados en cliente antes de invocar `auth/me`.
