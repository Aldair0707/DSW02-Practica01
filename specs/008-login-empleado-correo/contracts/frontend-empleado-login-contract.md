# Frontend Contract - Login Empleado

## Objetivo

Definir contrato de UI para flujo dedicado de actor EMPLEADO en ruta /empleado/login.

## Ruta

- Path: /empleado/login
- Acceso: publico (sin sesion)
- Resultado esperado: formulario de login por correo y contrasena

## Campos de formulario

- correo (string, requerido, email)
- contrasena (string, requerido, sin espacios extremos)

## Reglas de comportamiento

- En submit valido, frontend invoca autenticacion real via Basic Auth y consume GET /api/v1/empleados/auth/me.
- Si actorType != EMPLEADO, mostrar error de permisos para flujo empleado.
- Si backend retorna 401, mostrar mensaje generico de credenciales invalidas.
- No persistir credenciales en localStorage/sessionStorage.

## Estado UI

- idle: formulario visible
- loading: boton deshabilitado y texto de validacion
- success: estado autenticado con perfil empleado
- error: feedback generico no revelador

## Aceptacion minima E2E

- Caso feliz: empleado activo autentica y obtiene perfil EMPLEADO.
- Caso fallo: password incorrecta retorna error generico y no autentica.
- Caso inactivo: retorna mismo error generico que credenciales invalidas.
