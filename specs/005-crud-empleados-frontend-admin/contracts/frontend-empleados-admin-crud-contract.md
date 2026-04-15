# Frontend Contract - CRUD Empleados Desde Admin

## Scope

Contrato funcional de integracion frontend-backend para acceso administrativo y operaciones CRUD de empleados en el feature 005.

## Endpoints backend consumidos

### 1) Validar sesion administrativa

- Method: `GET`
- Path: `/api/v1/empleados/auth/me`
- Auth: `Authorization: Basic <base64(username:password)>`
- Success: `200 OK` con perfil autenticado
- Errors:
  - `401 Unauthorized`
  - `403 Forbidden`

### 2) Listar empleados paginados

- Method: `GET`
- Path: `/api/v1/empleados?page={n}&size={n}`
- Auth: Basic Auth obligatorio
- Notes: backend soporta paginacion y default `size=5`.

### 3) Crear empleado

- Method: `POST`
- Path: `/api/v1/empleados`
- Auth: Basic Auth obligatorio
- Body: datos requeridos de empleado segun contrato backend actual
- Success: `201 Created` o equivalente definido en backend
- Errors: validacion de negocio y formato

### 4) Actualizar empleado

- Method: `PUT`
- Path: `/api/v1/empleados/{clave}`
- Auth: Basic Auth obligatorio
- Body: datos actualizados permitidos
- Success: `200 OK`
- Errors: recurso inexistente o validacion

### 5) Eliminar empleado

- Method: `DELETE`
- Path: `/api/v1/empleados/{clave}`
- Auth: Basic Auth obligatorio
- Success: `204 No Content` o equivalente definido en backend
- Errors: recurso inexistente o no autorizado

## Frontend behavior contract

- El acceso al CRUD requiere sesion admin valida.
- Las operaciones CRUD solo se habilitan cuando la sesion esta autenticada.
- Alta y edicion bloquean submit con campos obligatorios vacios, solo espacios o correo invalido.
- El correo se trata como unico global; si existe en otro empleado, la operacion falla con error de negocio.
- En edicion, si la contrasena se deja vacia, se conserva la contrasena actual.
- El departamento es opcional en alta y edicion, pudiendo permanecer sin asignar.
- Eliminacion requiere confirmacion explicita antes de ejecutar request.
- Despues de crear, editar o eliminar, el listado se refresca y muestra estado actualizado.
- La paginacion debe preservar contexto de pantalla y permitir navegacion secuencial.

## Non-goals

- No se introducen endpoints backend nuevos.
- No se modifica el contrato OpenAPI backend en este feature.
- No se incorpora un nuevo mecanismo de autenticacion distinto de Basic Auth.

## Security notes

- Credenciales Basic usadas solo durante sesion activa.
- No persistir usuario/contrasena en almacenamiento permanente del navegador.
