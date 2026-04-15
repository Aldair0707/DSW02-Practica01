# Frontend UI Contract - Empleado Solo Lectura

## Objetivo

Definir el comportamiento esperado de la interfaz para actor `EMPLEADO` autenticado al consultar empleados y departamentos sin capacidades de CRUD.

## Entradas de estado

- Perfil autenticado desde `/api/v1/empleados/auth/me` con `actorType=EMPLEADO`.
- Listado paginado de empleados (`GET /api/v1/empleados`).
- Listado paginado de departamentos (`GET /api/v1/departamentos`).

## Campos visibles por actor EMPLEADO

### Empleados

- `id`
- `nombre`
- `correo`
- `departamento`

### Departamentos

- `id`
- `nombre`

## Reglas de UI

1. La UI debe ocultar o deshabilitar botones y acciones de:
   - crear empleado
   - editar empleado
   - eliminar empleado
   - crear departamento
   - editar departamento
   - eliminar departamento
2. La UI debe permitir navegacion de paginas (`page`, `size`) en ambos listados.
3. Si backend responde `403` en intento de escritura, mostrar mensaje legible de acceso denegado.
4. Si backend responde `401`, invalidar sesion de empleado y redirigir al flujo de login.

## Estados de pantalla

- `loading`: mientras se resuelven profile y listados.
- `authenticated-readonly`: listados visibles y controles de escritura ocultos/deshabilitados.
- `empty`: cuando no hay elementos en empleados o departamentos.
- `error`: para fallos no autorizados o de conectividad.

## Criterios de validacion de contrato UI

- Un empleado autenticado puede visualizar ambos listados sin exponer campos fuera del subconjunto definido.
- Ninguna accion de escritura debe quedar disponible en la UI para actor EMPLEADO.
- Cualquier escritura forzada por cliente externo sigue fallando con `403` en backend.
