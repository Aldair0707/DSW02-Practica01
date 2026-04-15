# Data Model - Autenticacion de empleados por correo y contrasena

## Entidad: Empleado

### Descripcion
Representa al empleado de negocio y su identidad de acceso por correo/contrasena.

### Campos relevantes

- `prefijo` (PK compuesta)
  - Tipo: `String`
  - Persistencia: `VARCHAR(4)`
  - Regla: valor fijo `EMP-`

- `consecutivo` (PK compuesta)
  - Tipo: `Long`
  - Persistencia: `BIGINT`
  - Regla: autonumerico por secuencia

- `correo`
  - Tipo: `String`
  - Persistencia: `VARCHAR(150)`
  - Reglas:
    - Obligatorio
    - Formato de correo valido
    - Unico de forma global (incluye cuentas activas e inactivas)
    - Se normaliza a lowercase + trim

- `passwordHash`
  - Tipo: `String`
  - Persistencia: `VARCHAR(255)`
  - Reglas:
    - Obligatorio
    - Contrasena de entrada entre 8 y 72 caracteres antes de aplicar hash
    - Se almacena solo hash (BCrypt u otro hash no reversible)
    - Nunca se expone en respuestas API
    - En este feature no se incluye cambio de contrasena

- `activo`
  - Tipo: `Boolean`
  - Persistencia: `BOOLEAN`
  - Reglas:
    - Valor por defecto `true`
    - Solo `true` permite autenticacion

- `nombre`, `direccion`, `telefono`
  - Reglas existentes conservadas
  - Persistencia: `VARCHAR(100)` no nulos

## Entidad: EventoAutenticacion

### Descripcion
Registro auditable de cada intento de autenticacion de empleado.

### Campos

- `id`
  - Tipo: `Long`
  - Persistencia: `BIGSERIAL`
  - Regla: PK autonumerica

- `correoIntentado`
  - Tipo: `String`
  - Persistencia: `VARCHAR(150)`
  - Regla: almacenar correo normalizado del intento

- `resultado`
  - Tipo: `Enum` (`EXITO`, `FALLO`)
  - Persistencia: `VARCHAR(20)`

- `motivo`
  - Tipo: `String`
  - Persistencia: `VARCHAR(80)`
  - Regla: valores controlados (ej. `CREDENCIALES_INVALIDAS`, `CUENTA_INACTIVA`, `AUTENTICACION_OK`)

- `origenSolicitud`
  - Tipo: `String`
  - Persistencia: `VARCHAR(120)`
  - Regla: registrar origen de solicitud (IP o identificador de origen disponible)

- `fechaHora`
  - Tipo: `Instant`
  - Persistencia: `TIMESTAMP WITH TIME ZONE`
  - Regla: obligatorio

## Vista/API: PerfilAutenticado

### Descripcion
Representacion segura del empleado autenticado para `GET /api/v1/empleados/auth/me`.

### Campos expuestos

- `clave`
- `correo`
- `nombre`
- `direccion`
- `telefono`
- `activo`

## Vista/API: EmpleadoResponse General

### Reglas de exposicion

- Incluye `clave`, `nombre`, `direccion`, `telefono`.
- No incluye `correo` ni `passwordHash`.

## Vista/API: EmpleadoEstadoResponse

### Descripcion
Representacion devuelta por `PATCH /api/v1/empleados/{clave}/estado` para cambios efectivos e idempotentes.

### Campos expuestos

- `clave`
- `activo`

## Relaciones

- `EventoAutenticacion` se relaciona logicamente con `Empleado` por `correoIntentado`; no requiere FK estricta para conservar auditoria de intentos sobre correos inexistentes.

## Reglas de validacion

- Correo con formato invalido -> error de validacion.
- Correo inexistente o contrasena incorrecta -> error generico de autenticacion.
- Empleado inactivo -> autenticacion denegada.
- Password en texto plano nunca persiste ni se retorna.
- Cambio de contrasena fuera de alcance para esta especificacion.

## Transiciones de estado

- Empleado activo + credenciales validas -> autenticado.
- Empleado activo + credenciales invalidas -> no autenticado.
- Empleado inactivo + cualquier credencial -> no autenticado.

## Indices y constraints sugeridos

- Indice unico funcional para correo normalizado.
- Indice por `fechaHora` en eventos para consultas temporales.
- Indice compuesto (`correoIntentado`, `fechaHora`) para auditorias por usuario.