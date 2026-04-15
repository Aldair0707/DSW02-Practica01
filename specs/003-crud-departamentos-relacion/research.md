# Phase 0 Research - CRUD de Departamentos y Relacion con Empleados

## 1) Generacion de clave de departamento DEP-XXXX

- Decision: Generar `clave` de departamento en backend con prefijo fijo `DEP-` y consecutivo numerico de exactamente 4 digitos (`DEP-0001`, `DEP-1001`).
- Rationale: Centraliza la regla de negocio, evita manipulacion del cliente y garantiza formato uniforme.
- Alternatives considered:
  - Permitir que cliente envie la clave: rechazado por riesgo de colisiones e inconsistencia de formato.
  - Usar UUID como clave de negocio: rechazado por no cumplir el formato requerido en especificacion.

## 2) Integridad entre Empleado y Departamento

- Decision: Mantener relacion 1:N por `departamentoClave` en empleado, validando existencia de departamento en asignacion/cambio.
- Rationale: Cumple el modelo funcional solicitado, mantiene trazabilidad de negocio y simplifica consultas por clave.
- Alternatives considered:
  - Relacion solo por id tecnico interno: rechazada por alejarse de la clave de negocio exigida en endpoints.

## 3) Cambio de departamento en endpoint dedicado

- Decision: Usar endpoint dedicado `PATCH /api/v1/empleados/{clave}/departamento`.
- Rationale: Separa responsabilidades del update general de empleado y reduce actualizaciones accidentales del vinculo.
- Alternatives considered:
  - Permitir cambio en `PUT /api/v1/empleados/{clave}`: rechazado por mezclar responsabilidades de mantenimiento de perfil y asignacion organizativa.

## 4) Politica de errores de negocio

- Decision: Devolver `404 Not Found` cuando el departamento objetivo no exista y `409 Conflict` cuando se intente eliminar un departamento con empleados asociados.
- Rationale: Codigos semanticos claros para cliente y alineados con la especificacion.
- Alternatives considered:
  - Devolver `400` para ambos casos: rechazado por menor expresividad del contrato.

## 5) Migracion de historicos sin departamento

- Decision: Crear/asegurar departamento tecnico `DEP-0000` con nombre `Sin asignar` y reasignar ahi empleados historicos sin departamento.
- Rationale: Evita nulos persistentes y permite hacer obligatoria la pertenencia de empleado a departamento tras migracion.
- Alternatives considered:
  - Permitir `departamentoClave` nullable de forma indefinida: rechazado por debilitar integridad del modelo.

## 6) Consulta de detalle vs consulta de empleados por departamento

- Decision: `GET /api/v1/departamentos/{clave}` devuelve solo datos de departamento; empleados se consultan en `GET /api/v1/departamentos/{clave}/empleados`.
- Rationale: Evita payloads sobredimensionados y mantiene endpoint de detalle estable.
- Alternatives considered:
  - Embebido siempre de empleados en detalle: rechazado por costo variable y acoplamiento.
  - Query param `includeEmpleados`: rechazado por aumentar variabilidad de contrato en esta iteracion.

## 7) Gobernanza API (versionado + paginacion + seguridad)

- Decision: Mantener todas las rutas nuevas bajo `/api/v1/...`, aplicar paginacion a listados con default `size=5`, y proteger endpoints con Basic Auth.
- Rationale: Cumplimiento directo de la constitucion vigente del repositorio.
- Alternatives considered:
  - Rutas sin version o paginacion opcional sin default: rechazadas por incumplimiento constitucional.

## Resolucion de NEEDS CLARIFICATION

No quedan NEEDS CLARIFICATION para esta feature. Reglas de relacion, errores, migracion, versionado y forma de consulta quedaron cerradas.
