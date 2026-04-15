# Frontend Contract - CRUD Empleados Admin

## Scope

Contrato funcional de integracion frontend-backend para la gestion CRUD de empleados desde panel admin.

## Endpoints backend consumidos (sin cambios de contrato)

### 1) Validar sesion admin

- Method: `GET`
- Path: `/api/v1/empleados/auth/me`
- Auth: `Authorization: Basic <base64(username:password)>`
- Success: `200 OK`
- Error: `401 Unauthorized`, `403 Forbidden`

### 2) Listar empleados paginados

- Method: `GET`
- Path: `/api/v1/empleados?page={n}&size={n}`
- Auth: Basic Auth obligatorio
- Notes:
  - backend mantiene default `size=5`
  - sin orden explicito, frontend espera orden por clave ascendente

### 3) Crear empleado

- Method: `POST`
- Path: `/api/v1/empleados`
- Auth: Basic Auth obligatorio
- Success: `201 Created`
- Error: `400`, `401`, `403`

### 4) Actualizar empleado

- Method: `PUT`
- Path: `/api/v1/empleados/{clave}`
- Auth: Basic Auth obligatorio
- Success: `200 OK`
- Error: `400`, `401`, `403`, `404`

### 5) Eliminar empleado

- Method: `DELETE`
- Path: `/api/v1/empleados/{clave}`
- Auth: Basic Auth obligatorio
- Success: `204 No Content`
- Notes: comportamiento idempotente observable en frontend

## Frontend behavior contract

- El panel CRUD solo se habilita para `admin` autenticado.
- Si backend responde `401` o `403`, frontend invalida sesion y solicita login.
- La clave de empleado es solo lectura en UI.
- El listado se mantiene paginado y consistente tras crear, editar o eliminar.
- La eliminacion requiere confirmacion explicita del administrador.

## Non-goals

- No se agregan endpoints nuevos de backend.
- No se rediseña persistencia backend.
- No se modifica mecanismo de autenticacion (se mantiene Basic Auth).
