# Data Model - Corregir Rol Admin En Login

## Entidad: AuthPrincipal

### Descripcion
Representa el principal autenticado resultante de HTTP Basic antes de mapearlo a un perfil funcional.

### Campos

- `username` (`string`, obligatorio, sin espacios)
- `roles` (`string[]`, al menos un rol)
- `authenticated` (`boolean`)
- `active` (`boolean`)

## Entidad: PerfilAutenticado

### Descripcion
Contrato funcional de identidad que consume frontend para habilitar o bloquear el area administrativa.

### Campos

- `actorType` (`"ADMIN" | "EMPLEADO"`, obligatorio)
- `username` (`string`, obligatorio)
- `displayName` (`string`, obligatorio)
- `permissions` (`string[]`, obligatorio)
- `empleado` (`EmpleadoResumen | null`, requerido cuando `actorType="EMPLEADO"`)

## Entidad: EmpleadoResumen

### Descripcion
Subconjunto de datos de empleado expuesto al consultar perfil autenticado cuando el actor es empleado.

### Campos

- `clave` (`string`, patron `EMP-[0-9]+`)
- `correo` (`string`, formato email)
- `nombre` (`string`)
- `direccion` (`string`)
- `telefono` (`string`)
- `departamentoClave` (`string | null`)
- `activo` (`boolean`)

## Entidad: DecisionAutorizacion

### Descripcion
Resultado de politica de acceso para endpoints CRUD administrativos.

### Campos

- `resource` (`string`)
- `action` (`string`)
- `allowed` (`boolean`)
- `reason` (`"AUTH_INVALIDA" | "NO_AUTORIZADO" | "OK"`)

## Entidad: SolicitudListadoAdmin

### Descripcion
Representa el contrato de consulta para endpoints de listado del dominio admin.

### Campos

- `page` (`integer`, opcional, default `0`, minimo `0`)
- `size` (`integer`, opcional, default `5`, minimo `1`)
- `versionPrefix` (`string`, obligatorio, valor esperado `/api/v1`)

## Relaciones

- `AuthPrincipal` se transforma en `PerfilAutenticado` mediante resolucion por rol/identidad.
- Si `PerfilAutenticado.actorType="ADMIN"`, `empleado` es `null` y se habilitan permisos de gestion.
- Si `PerfilAutenticado.actorType="EMPLEADO"`, `empleado` contiene resumen del registro real.
- Toda denegacion en endpoints administrativos genera `DecisionAutorizacion` con `AUTH_INVALIDA` o `NO_AUTORIZADO`.
- `SolicitudListadoAdmin` aplica a `/api/v1/empleados`, `/api/v1/departamentos` y `/api/v1/departamentos/{clave}/empleados` como minimo.

## Reglas de validacion

- `admin/admin123` debe autenticarse en local/dev salvo override por entorno.
- El principal bootstrap admin no debe requerir existencia en repositorio de empleados.
- Un empleado autenticado no puede acceder a acciones administrativas protegidas.
- `401` se usa para credenciales invalidas/ausentes; `403` para autenticado sin permisos.
- Los endpoints de listado de administracion usan `page` y `size`, con `size=5` por defecto.
- Las rutas publicas del dominio admin se mantienen versionadas en `/api/v1/...`.

## Transiciones de estado

- `No autenticado` -> `Autenticando` -> `Autenticado(ADMIN|EMPLEADO)`
- `Autenticando` -> `No autenticado` (credenciales invalidas)
- `Autenticado(EMPLEADO)` -> `No autorizado` al intentar recurso admin
- `Autenticado(ADMIN)` -> `Autorizado` para CRUD administrativo
- `Autenticado(*)` -> `No autenticado` por expiracion/rechazo posterior (`401`)
