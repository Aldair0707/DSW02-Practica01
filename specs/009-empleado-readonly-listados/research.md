# Research - Vista de Empleado Solo Lectura

## Decision 1: Reutilizar endpoints versionados existentes para lectura de EMPLEADO

- Decision: Se reutilizan `GET /api/v1/empleados` y `GET /api/v1/departamentos` para actor EMPLEADO, sin crear rutas nuevas exclusivas.
- Rationale: La especificacion aclarada exige reutilizacion de endpoints existentes y evita duplicar contratos y logica de negocio.
- Alternatives considered:
  - Crear endpoints dedicados para EMPLEADO: descartado por aumentar mantenimiento y duplicar superficie API.
  - Exponer solo endpoint agregado/resumen: descartado por no cubrir requerimiento de ver ambos listados.

## Decision 2: Autorizacion granular por metodo y actor

- Decision: Ajustar `SecurityConfig` para permitir `GET` en empleados/departamentos a rol `EMPLEADO`, manteniendo escrituras (`POST`, `PUT`, `PATCH`, `DELETE`) restringidas a `ADMIN`.
- Rationale: Cumple principio de menor privilegio y la constitucion (solo lectura para empleado) sin romper CRUD admin existente.
- Alternatives considered:
  - Mantener bloqueo total para EMPLEADO: descartado por impedir el objetivo funcional principal.
  - Controlar solo desde frontend: descartado por inseguro ante clientes externos o llamadas manuales.

## Decision 3: Denegacion explicita de escrituras con 403

- Decision: Toda operacion de escritura intentada por EMPLEADO en empleados/departamentos responde `403 Forbidden` con error de autorizacion.
- Rationale: El usuario ya definio `403` y es el comportamiento semantico correcto para actor autenticado sin permiso.
- Alternatives considered:
  - 401 para escrituras: descartado por semantica incorrecta (no es fallo de autenticacion).
  - 404 para ocultar recurso: descartado por reducir transparencia y complicar observabilidad.

## Decision 4: Defensa en profundidad UI + backend

- Decision: La UI de empleado oculta o deshabilita acciones de escritura, y backend valida permisos de todos modos.
- Rationale: Mejora UX (evita intentos fallidos) y mantiene seguridad real en servidor.
- Alternatives considered:
  - Solo backend: descartado por UX pobre.
  - Solo frontend: descartado por inseguro.

## Decision 5: Proyeccion de campos no sensibles para EMPLEADO

- Decision: Para actor EMPLEADO, los listados exponen solo campos minimos definidos por aclaracion: 
  - Empleado: `id`, `nombre`, `correo`, `departamento`
  - Departamento: `id`, `nombre`
- Rationale: Cumple requerimiento de minimizacion de datos y reduce exposicion de atributos sensibles/no necesarios.
- Alternatives considered:
  - Reusar payload completo admin: descartado por sobreexposicion de datos.
  - Definir campos mas adelante: descartado por ambiguedad de contrato y pruebas.

## Decision 6: Compatibilidad de identificador con modelo existente

- Decision: Se modela `id` como identificador contractual de solo lectura para empleado, mapeado desde identificadores actuales del dominio (por ejemplo, clave persistente) sin forzar migracion de base de datos.
- Rationale: Permite cumplir especificacion sin introducir cambios de esquema.
- Alternatives considered:
  - Migrar todo el dominio de `clave` a `id` real: descartado por alcance alto y riesgo de regresion.
  - Mantener nombre `clave` en contrato de empleado: descartado por no respetar aclaracion cerrada de la especificacion.

## Decision 7: Mantener paginacion constitucional en listados

- Decision: Conservar paginacion en endpoints de listado con `page` y `size`, y `size=5` por defecto.
- Rationale: Es mandato constitucional y ya esta implementado en controladores.
- Alternatives considered:
  - Deshabilitar paginacion para empleado: descartado por incumplimiento constitucional.
  - Usar un default distinto para empleado: descartado por inconsistencia de contrato.

## Decision 8: Evidencia de validacion por capas

- Decision: Validar feature con pruebas backend (autorizacion GET vs CRUD), pruebas frontend (vista readonly) y smoke/API manual reproducible.
- Rationale: El cambio afecta seguridad y UX, por lo que requiere cobertura multiplataforma.
- Alternatives considered:
  - Solo pruebas unitarias: descartado por baja confianza en integracion de seguridad.
  - Solo validacion manual: descartado por baja repetibilidad.
