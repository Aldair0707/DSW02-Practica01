# Data Model - CRUD Empleados Desde Admin

## Entidad: AdministradorSesion

### Descripcion
Estado de autenticacion del administrador que habilita operaciones CRUD de empleados en frontend.

### Campos

- `username`
  - Tipo: `string`
  - Regla: obligatorio, sin espacios no permitidos
- `authenticated`
  - Tipo: `boolean`
  - Regla: `true` solo tras validacion exitosa
- `perfil`
  - Tipo: `PerfilAutenticado | null`
  - Regla: presente cuando hay sesion valida
- `errorSesion`
  - Tipo: `string | null`

## Entidad: EmpleadoForm

### Descripcion
Representa los datos editables en UI para alta y edicion de empleado.

### Campos

- `nombre` (`string`, obligatorio, no vacio)
- `direccion` (`string`, obligatorio, no vacio)
- `telefono` (`string`, obligatorio, no vacio)
- `correo` (`string`, obligatorio, formato email valido, unico)
- `contrasena` (`string`, obligatoria en alta, opcional en edicion)
- `departamentoClave` (`string | null`, opcional)

## Entidad: EmpleadoListadoItem

### Descripcion
Representa cada fila visible del listado paginado de empleados.

### Campos

- `clave` (`string`)
- `nombre` (`string`)
- `telefono` (`string`)
- `correo` (`string`)
- `departamentoClave` (`string | null`)
- `activo` (`boolean`)

## Entidad: EmpleadoPageState

### Descripcion
Estado de paginacion y carga para la tabla de empleados.

### Campos

- `items` (`EmpleadoListadoItem[]`)
- `page` (`number`)
- `size` (`number`)
- `totalElements` (`number`)
- `totalPages` (`number`)
- `loading` (`boolean`)

## Entidad: ResultadoOperacionUI

### Descripcion
Resultado mostrado al usuario tras operaciones de alta, edicion, eliminacion o error.

### Campos

- `type` (`"success" | "error" | "warning"`)
- `message` (`string`)
- `source` (`"login" | "create" | "update" | "delete" | "list"`)

## Relaciones

- `AdministradorSesion.authenticated=true` habilita `EmpleadoPageState` y operaciones CRUD.
- `EmpleadoForm` se transforma en payload de creacion o actualizacion.
- Respuesta de API de empleados alimenta `EmpleadoListadoItem[]` y metadatos de `EmpleadoPageState`.
- Cada accion CRUD produce un `ResultadoOperacionUI`.

## Reglas de validacion

- No se envian operaciones CRUD si la sesion admin no esta autenticada.
- No se envia alta/edicion con campos obligatorios vacios o compuestos solo por espacios.
- `correo` debe cumplir formato valido y unicidad global; duplicados deben reflejar error de negocio.
- En edicion, si `contrasena` llega vacia, se conserva la contrasena actual.
- `departamentoClave` puede enviarse nulo/vacio para representar estado sin asignar.
- Eliminacion requiere confirmacion explicita antes del request.
- Tras alta, edicion o eliminacion exitosa, el listado se refresca manteniendo contexto de paginacion.

## Transiciones de estado

- `No autenticado` -> `Autenticando` -> `Autenticado`
- `Autenticado` -> `Guardando` -> `Autenticado` (alta/edicion exitosa)
- `Autenticado` -> `Eliminando` -> `Autenticado` (eliminacion exitosa)
- `Autenticado` -> `Error` (fallo de validacion, autorizacion o red)
- `Autenticado` -> `No autenticado` (respuesta backend `401/403`)
- `Autenticado` -> `No autenticado` (logout/expiracion)
