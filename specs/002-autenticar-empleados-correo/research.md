# Phase 0 Research - Autenticacion de empleados por correo y contrasena

## 1) Estrategia de autenticacion por correo en HTTP Basic

- Decision: Usar `correo` como `username` y `contrasena` como `password` en HTTP Basic para empleados.
- Rationale: Cumple autenticacion por correo/contrasena sin romper la regla constitucional de Basic Auth.
- Alternatives considered:
  - Endpoint de login con JWT: rechazado por salir del mecanismo obligatorio en este proyecto.
  - Usuario tecnico unico para todo: rechazado por no autenticar identidad de empleado.

## 2) Alcance de seguridad y estado de cuenta

- Decision: No modelar sesion de aplicacion; el acceso depende de credenciales vigentes y cuenta activa.
- Rationale: Basic Auth es stateless y esta definicion evita ambiguedad funcional.
- Alternatives considered:
  - Manejo de sesion con expiracion propia: rechazado por complejidad fuera de alcance y redundancia con Basic Auth.

## 3) Ciclo de vida de contrasena en este feature

- Decision: Exigir contrasena inicial en alta; excluir cambio de contrasena de este feature.
- Rationale: Mantiene MVP enfocado en autenticacion y control de acceso.
- Alternatives considered:
  - Incluir cambio de contrasena opcional en update: rechazado para reducir alcance y riesgo.

## 4) Politica minima y almacenamiento de contrasena

- Decision: Aplicar longitud minima/maxima 8-72 y persistir solo hash (sin retorno en responses).
- Rationale: 72 es limite practico compatible con BCrypt y evita truncado silencioso de entrada.
- Alternatives considered:
  - Sin limite superior: rechazado por comportamiento impredecible de hashing.
  - Persistir contrasena en claro para MVP: rechazado por riesgo de seguridad.

## 5) Unicidad de correo

- Decision: Unicidad global de correo para cuentas activas e inactivas.
- Rationale: Evita colisiones al reactivar cuentas y simplifica identificacion del principal autenticado.
- Alternatives considered:
  - Unicidad solo en activas: rechazada por ambiguedad operativa en historico.

## 6) Visibilidad de correo en contrato

- Decision: Exponer correo solo en `GET /api/v1/empleados/auth/me`; no en responses generales de empleado.
- Rationale: Minimiza exposicion de datos y mantiene el contrato de CRUD general mas acotado.
- Alternatives considered:
  - Exponer correo en listados y respuestas generales: rechazado por mayor superficie de dato sensible.

## 7) Manejo de errores y auditoria

- Decision: Mantener respuesta generica ante credenciales invalidas y registrar eventos de autenticacion (exito/fallo).
- Rationale: Reduce riesgo de enumeracion de cuentas y proporciona trazabilidad operativa.
- Alternatives considered:
  - Mensajes distintos por causa: rechazado por filtrar informacion.
  - Solo logs de aplicacion sin entidad dedicada: rechazado por menor capacidad de auditoria.

## 8) Idempotencia para cambio de estado de cuenta

- Decision: En `PATCH /api/v1/empleados/{clave}/estado`, responder `200` con payload consistente cuando el estado solicitado coincida con el estado actual.
- Rationale: Hace explicita la semantica idempotente solicitada y simplifica validacion funcional/contractual.
- Alternatives considered:
  - Responder `204` sin payload en no-cambio: rechazada por menor trazabilidad para clientes.
  - Responder error por no-cambio: rechazada por romper idempotencia.

## Resolucion de NEEDS CLARIFICATION

No quedan NEEDS CLARIFICATION para esta feature. Alcance, seguridad, contrato, unicidad, auditoria minima e idempotencia quedaron cerrados.