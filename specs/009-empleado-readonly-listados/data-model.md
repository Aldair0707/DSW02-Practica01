# Data Model - Vista de Empleado Solo Lectura

## Entidades y Vistas de Contrato

### 1) Empleado (dominio existente)

Descripcion: entidad principal de personal registrada en backend.

Campos persistentes relevantes (existentes):
- clave: string
- nombre: string
- correo: string
- direccion: string
- telefono: string
- departamentoClave: string | null
- activo: boolean

Reglas relevantes para esta feature:
- Actor EMPLEADO no modifica esta entidad.
- Listado para EMPLEADO se expone mediante proyeccion de solo lectura.

### 2) Departamento (dominio existente)

Descripcion: unidad organizativa asociada a empleados.

Campos persistentes relevantes (existentes):
- clave: string
- nombre: string

Reglas relevantes para esta feature:
- Actor EMPLEADO solo consulta listados.
- CRUD permanece en alcance ADMIN.

### 3) EmpleadoReadOnlyItem (proyeccion contractual)

Descripcion: representacion minima para listados visibles por actor EMPLEADO.

Campos contractuales:
- id: string
- nombre: string
- correo: string
- departamento: string | null

Reglas:
- `id` se mapea desde identificador existente de dominio (`clave`) para no forzar migracion de esquema.
- No incluir `direccion`, `telefono`, `activo`, `passwordHash` ni campos internos.

### 4) DepartamentoReadOnlyItem (proyeccion contractual)

Descripcion: representacion minima de departamento para actor EMPLEADO.

Campos contractuales:
- id: string
- nombre: string

Reglas:
- `id` se mapea desde `clave` en capa de presentacion/DTO.
- No incluir metadatos adicionales fuera del alcance de consulta.

### 5) PerfilAutenticado (existente)

Descripcion: payload de `/api/v1/empleados/auth/me` usado para distinguir actor ADMIN/EMPLEADO.

Campos relevantes:
- actorType: `ADMIN` | `EMPLEADO`
- username: string
- permissions: string[]

Reglas:
- Solo actorType `EMPLEADO` habilita vista readonly objetivo de esta feature.
- Si actor no es EMPLEADO, frontend aplica flujo correspondiente (admin u otro rechazo).

## Relaciones

- Departamento 1..n Empleado (por `departamentoClave` en dominio, proyectado a `departamento` en lectura empleado).
- PerfilAutenticado determina politicas de autorizacion para consumir listados.
- EmpleadoReadOnlyItem y DepartamentoReadOnlyItem son vistas derivadas; no son tablas nuevas.

## Validaciones

- Listados conservan paginacion con `page` y `size`, default `size=5`.
- Intentos de `POST/PUT/PATCH/DELETE` sobre empleados/departamentos por actor EMPLEADO resultan en `403`.
- Estados vacios (`content=[]`) son validos y no se consideran error funcional.

## Transiciones de estado (feature)

1. No autenticado -> autenticado como EMPLEADO (via login existente).
2. EMPLEADO autenticado -> consulta listados readonly (empleados/departamentos).
3. EMPLEADO autenticado -> intenta escritura -> backend deniega `403` + UI muestra mensaje.
4. Sesion expirada/invalida -> denegacion de consulta y retorno a flujo de autenticacion.
