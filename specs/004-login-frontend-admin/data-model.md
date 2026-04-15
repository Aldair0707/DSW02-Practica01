# Data Model - Login Frontend con Admin

## Entidad: CredencialesLogin

### Descripcion
Datos ingresados por usuario para autenticacion Basic en frontend.

### Campos

- `username`
  - Tipo: `string`
  - Regla: obligatorio, no vacio
  - Ejemplo: `admin`

- `password`
  - Tipo: `string`
  - Regla: obligatorio, no vacio
  - Ejemplo: `admin123`

## Entidad: PerfilAutenticado

### Descripcion
Representa la respuesta de `GET /api/v1/empleados/auth/me` tras login exitoso.

### Campos

- `clave` (`string`)
- `correo` (`string`)
- `nombre` (`string`)
- `direccion` (`string`)
- `telefono` (`string`)
- `departamentoClave` (`string | null`)
- `activo` (`boolean`)

## Entidad: SesionFrontend

### Descripcion
Estado en memoria de la SPA que controla acceso a vistas protegidas.

### Campos

- `autenticado` (`boolean`)
- `credenciales` (`CredencialesLogin` en memoria durante la sesion)
- `perfil` (`PerfilAutenticado | null`)
- `errorActual` (`string | null`)

## Entidad: EstadoPantallaLogin

### Descripcion
Estado de UI para experiencia de login.

### Campos

- `loadingAuth` (`boolean`)
- `mensaje` (`string`)
- `error` (`string`)

## Relaciones

- `CredencialesLogin` se transforma en header Basic para solicitar `PerfilAutenticado`.
- `PerfilAutenticado` alimenta `SesionFrontend`.
- `SesionFrontend.autenticado` habilita carga de recursos paginados de empleados y departamentos.

## Reglas de validacion

- Si `username` o `password` estan vacios, no se envia request.
- Si backend responde `401/403`, `SesionFrontend.autenticado=false`.
- Si backend responde `200`, `SesionFrontend.autenticado=true` y se habilitan vistas protegidas.
- Logout siempre limpia `perfil`, datos cargados y mensajes de estado.

## Transiciones de estado

- `No autenticado` -> `Autenticando` -> `Autenticado` (login valido)
- `No autenticado` -> `Autenticando` -> `Error` (credenciales invalidas o red)
- `Autenticado` -> `No autenticado` (logout)
