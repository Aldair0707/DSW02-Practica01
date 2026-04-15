# Data Model - CRUD de Departamentos y Relacion con Empleados

## Entidad: Departamento

### Descripcion
Unidad organizativa de negocio a la que se asocian empleados.

### Campos

- `prefijo` (PK compuesta)
  - Tipo: `String`
  - Persistencia: `VARCHAR(4)`
  - Regla: valor fijo `DEP-`

- `consecutivo` (PK compuesta)
  - Tipo: `Long`
  - Persistencia: `BIGINT`
  - Regla: autonumerico por secuencia

- `clave`
  - Tipo: `String`
  - Persistencia: campo derivado de prefijo + consecutivo, expuesto como `DEP-XXXX` (4 digitos exactos)
  - Reglas:
    - Obligatoria
    - Unica de negocio
    - Inmutable despues de creada

- `nombre`
  - Tipo: `String`
  - Persistencia: `VARCHAR(100)`
  - Reglas:
    - Obligatorio
    - Longitud entre 2 y 100
    - Puede repetirse entre departamentos distintos

## Entidad: Empleado (impacto)

### Descripcion
Empleado existente con nueva referencia de pertenencia organizativa.

### Campo nuevo

- `departamentoClave`
  - Tipo: `String`
  - Persistencia: `VARCHAR(20)`
  - Reglas:
    - Obligatorio despues de migracion inicial
    - Debe referenciar una clave de departamento existente
    - No editable en alta inicial de empleado
    - Se actualiza mediante endpoint dedicado de asignacion/cambio

## Entidad tecnica: Departamento por defecto

### Descripcion
Departamento tecnico para normalizacion de historicos.

### Valores

- `clave`: `DEP-0000`
- `nombre`: `Sin asignar`

### Reglas

- Debe existir durante migracion de empleados historicos sin departamento.
- Puede quedar activo para trazabilidad, sin uso en nuevas altas si el flujo exige asignacion explicita posterior.

## Relaciones

- Departamento 1 --- N Empleado
- Cada empleado pertenece a un unico departamento por `departamentoClave`.

## Reglas de validacion

- Crear/actualizar departamento con `nombre` fuera de rango -> error de validacion.
- Asignar `departamentoClave` inexistente en empleado -> `404 Not Found`.
- Eliminar departamento con empleados asociados -> `409 Conflict`.
- Intento de modificar manualmente `departamentoClave` en alta de empleado -> rechazo de validacion.

## Transiciones de estado relevantes

- Empleado sin departamento historico -> migrado a `DEP-0000`.
- Empleado con `departamentoClave=DEP-0000` -> puede cambiar a departamento funcional por endpoint dedicado.
- Departamento sin empleados -> eliminable.
- Departamento con uno o mas empleados -> no eliminable.

## Indices y constraints sugeridos

- Constraint unico para clave de departamento.
- Indice por `departamentoClave` en empleado para listar empleados por departamento.
- FK logica o constraint referencial entre empleado y departamento (segun modelo fisico actual del proyecto).
