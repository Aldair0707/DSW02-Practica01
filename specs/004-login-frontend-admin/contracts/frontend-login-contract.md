# Frontend Contract - Login con Basic Auth

## Scope

Contrato de integracion frontend-backend para autenticacion y habilitacion de panel en el feature `004-login-frontend-admin`.

## Backend endpoints consumidos

### 1) Validar sesion y obtener perfil

- Method: `GET`
- Path: `/api/v1/empleados/auth/me`
- Auth: `Authorization: Basic <base64(username:password)>`
- Success response: `200 OK`
- Body:

```json
{
  "clave": "EMP-1001",
  "correo": "admin@empresa.com",
  "nombre": "Administrador",
  "direccion": "Direccion",
  "telefono": "555-000",
  "departamentoClave": "DEP-1001",
  "activo": true
}
```

- Error responses:
  - `401 Unauthorized`: credenciales invalidas o ausentes
  - `403 Forbidden`: usuario sin permisos o inactivo

### 2) Cargar empleados (post-login)

- Method: `GET`
- Path: `/api/v1/empleados?page={n}&size={n}`
- Auth: `Authorization: Basic <base64(username:password)>`
- Notes: backend aplica paginacion y default `size=5`.

### 3) Cargar departamentos (post-login)

- Method: `GET`
- Path: `/api/v1/departamentos?page={n}&size={n}`
- Auth: `Authorization: Basic <base64(username:password)>`
- Notes: backend aplica paginacion y default `size=5`.

## Frontend behavior contract

- Debe existir formulario con `username` y `password` obligatorios.
- Al enviar login valido, la UI cambia a estado autenticado y muestra panel.
- Al enviar login invalido, permanece en login y muestra mensaje de error.
- Al hacer logout, limpia sesion en memoria y regresa a pantalla de login.
- El frontend no crea ni modifica endpoints backend en este feature.

## Security notes

- Credenciales Basic se mantienen en memoria durante la sesion.
- No persistir username/password en `localStorage` ni `sessionStorage` dentro de este alcance.
