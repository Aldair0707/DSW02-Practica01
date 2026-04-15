# Data Model - CRUD de empleado

## Entidad: Empleado

### Descripción
Representa un empleado identificado de forma única por una PK compuesta (`prefijo`, `consecutivo`) y expuesto externamente como `clave` en formato `EMP-<autonumérico>`.

### Campos

- `prefijo` (parte PK)
  - Tipo lógico: `String`
  - Tipo persistencia: `VARCHAR(4)`
  - Reglas:
    - Obligatorio
    - Valor fijo `EMP-`

- `consecutivo` (parte PK)
  - Tipo lógico: entero largo (`Long`)
  - Tipo persistencia: `BIGINT`
  - Reglas:
    - Obligatorio
    - Autonumérico
    - Único dentro del prefijo `EMP-`

- `clave` (representación externa)
  - Tipo lógico: `String`
  - Formato: `EMP-<autonumérico>`
  - Reglas:
    - No se captura en alta
    - Inmutable después de creación
    - Válida para consulta/update/delete cuando cumple patrón `^EMP-[0-9]+$`

- `nombre`
  - Tipo lógico: `String`
  - Tipo persistencia: `VARCHAR(100)`
  - Reglas:
    - Obligatorio
    - Longitud máxima 100

- `direccion`
  - Tipo lógico: `String`
  - Tipo persistencia: `VARCHAR(100)`
  - Reglas:
    - Obligatorio
    - Longitud máxima 100

- `telefono`
  - Tipo lógico: `String`
  - Tipo persistencia: `VARCHAR(100)`
  - Reglas:
    - Obligatorio
    - Longitud máxima 100

## Relaciones

No hay relaciones con otras entidades en el alcance de esta feature.

## Reglas de validación derivadas de requerimientos

- `clave` enviada manualmente en alta -> error `VALIDACION` o ignorada por contrato.
- `clave` vacía/nula/malformada en consulta/update/delete -> error `FORMATO_CLAVE_INVALIDO`.
- `nombre`, `direccion`, `telefono` con longitud > 100 -> error `VALIDACION`.
- Intento de modificar `clave` en update -> error `CLAVE_NO_EDITABLE`.
- Consulta/update/delete sobre `clave` inexistente -> `NO_ENCONTRADO`.

## Estados y transiciones

- Estado implícito: `ACTIVO` (registro existente).
- Transiciones:
  - `CREAR` -> registro ACTIVO
  - `ACTUALIZAR` -> permanece ACTIVO (solo cambia nombre/direccion/telefono)
  - `ELIMINAR` -> registro inexistente

## Índices y constraints de base de datos

- Primary Key compuesta: `empleado.prefijo` + `empleado.consecutivo`
- Unique: implícito por PK compuesta
- Check recomendado: longitudes máximas de columnas (garantizadas por `VARCHAR(100)`)

## Consideraciones de serialización/API

- En requests/responses, exponer `clave`, `nombre`, `direccion`, `telefono`.
- En creación, no aceptar `clave` en payload; se devuelve en respuesta.
- Mantener compatibilidad con el texto de requisitos: internamente se usa `direccion` sin acento; en documentación se aclara correspondencia con “dirección”.