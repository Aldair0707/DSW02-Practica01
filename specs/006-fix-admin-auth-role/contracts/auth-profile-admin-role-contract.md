# Contract - Auth Profile Admin Role

## Scope

Contrato funcional frontend-backend para:

- Resolver correctamente el perfil autenticado cuando el usuario es `admin` (bootstrap) o un empleado.
- Estandarizar errores de autenticacion/autorizacion (`AUTH_INVALIDA` y `NO_AUTORIZADO`).
- Garantizar gobernanza de listados admin con versionado `/api/v1` y paginacion `page/size` con default `size=5`.

## Endpoint

### GET `/api/v1/empleados/auth/me`

- Auth: `Authorization: Basic <base64(username:password)>`
- Versioning: se mantiene bajo `/api/v1/...`

### Response `200 OK`

```json
{
  "actorType": "ADMIN",
  "username": "admin",
  "displayName": "Administrador",
  "permissions": ["CRUD_EMPLEADOS", "CRUD_DEPARTAMENTOS"],
  "empleado": null
}
```

```json
{
  "actorType": "EMPLEADO",
  "username": "empleado@empresa.com",
  "displayName": "Ana Perez",
  "permissions": ["SELF"],
  "empleado": {
    "clave": "EMP-1001",
    "correo": "empleado@empresa.com",
    "nombre": "Ana Perez",
    "direccion": "Calle Centro 123",
    "telefono": "5551234567",
    "departamentoClave": "DEP-001",
    "activo": true
  }
}
```

### Response `401 Unauthorized`

- Credenciales ausentes o invalidas.
- Body de error consistente con `ErrorResponse` actual (`AUTH_INVALIDA`).

Ejemplo:

```json
{
  "code": "AUTH_INVALIDA",
  "message": "Credenciales invalidas",
  "details": []
}
```

### Response `403 Forbidden`

- Usuario autenticado sin permiso para recurso solicitado.
- Body de error consistente con `ErrorResponse` actual (`NO_AUTORIZADO`).

Ejemplo:

```json
{
  "code": "NO_AUTORIZADO",
  "message": "No autorizado",
  "details": []
}
```

## List Endpoints Governance (Admin Domain)

Todos los endpoints de listado del dominio admin deben:

- Estar versionados bajo `/api/v1/...`.
- Soportar parametros `page` y `size`.
- Aplicar `size=5` cuando `size` no se envie.

Minimo obligatorio en esta feature:

### GET `/api/v1/empleados`

- Query params: `page` (default `0`), `size` (default `5`)

### GET `/api/v1/departamentos`

- Query params: `page` (default `0`), `size` (default `5`)

### GET `/api/v1/departamentos/{clave}/empleados`

- Query params: `page` (default `0`), `size` (default `5`)

### Non-versioned routes

- `/empleados`, `/departamentos`, `/departamentos/{clave}/empleados` no forman parte del contrato publico y deben devolver `404`.

## Behavioral Rules

- El usuario bootstrap `admin` se reconoce como actor administrativo sin lookup obligatorio en tabla de empleados.
- Un empleado autenticado conserva su comportamiento de perfil actual.
- El frontend habilita pantalla administrativa solo cuando `actorType=ADMIN`.
- Errores de autenticacion y autorizacion son distinguibles por status y codigo funcional estable.
- La regla de versionado/paginacion aplica a listados actuales y futuros del dominio admin.

## Non-goals

- No se crean endpoints nuevos para auth.
- No se cambia mecanismo Basic Auth.
- No se implementa en esta feature logging/persistencia adicional de rechazos administrativos.
