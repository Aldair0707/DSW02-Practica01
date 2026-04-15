# Phase 0 Research - Corregir Rol Admin En Login

## 1) Resolucion de perfil para principal `admin`

- Decision: Resolver `auth/me` por tipo de actor; cuando el principal coincide con el usuario bootstrap, devolver actor `ADMIN` sin requerir lookup en tabla de empleados.
- Rationale: El problema raiz es tratar `admin` como empleado. Separar actor administrativo evita errores de identidad y mantiene semantica del dominio.
- Alternatives considered:
  - Crear empleado tecnico `admin` en base de datos: descartado por mezclar actor de plataforma con actor de negocio.
  - Exponer endpoint nuevo para admin: descartado por incrementar superficie API sin necesidad.

## 2) Contrato de errores de acceso (401/403)

- Decision: Estandarizar respuestas con HTTP + codigo funcional fijo: `AUTH_INVALIDA` para `401` y `NO_AUTORIZADO` para `403`, con mensaje generico seguro.
- Rationale: Frontend necesita distinguir autenticacion invalida vs falta de permisos de forma deterministica y testeable.
- Alternatives considered:
  - Distinguir solo por status HTTP: descartado por menor robustez contractual.
  - Mensaje libre sin codigo funcional: descartado por fragilidad en integracion frontend.

## 3) Gobernanza de listados y versionado

- Decision: Aplicar la regla a todos los listados actuales y futuros del dominio admin; minimo obligatorio en esta feature: `/api/v1/empleados`, `/api/v1/departamentos`, `/api/v1/departamentos/{clave}/empleados` con `page`, `size` y default `size=5`.
- Rationale: Cumple constitucion y evita regresiones cuando aparezcan nuevos listados.
- Alternatives considered:
  - Limitar solo a listados actuales: descartado por deuda de gobernanza futura.
  - Permitir rutas sin versionado: descartado por incumplimiento constitucional.

## 4) Alcance de trazabilidad de rechazos

- Decision: Dejar fuera de alcance de esta feature la persistencia/logging adicional de rechazos administrativos (FR-009 diferido).
- Rationale: Prioridad actual es correccion funcional de login/autorizar admin y gobernanza de contrato/listados; auditoria se aborda en feature posterior.
- Alternatives considered:
  - Implementar auditoria completa ahora: descartado por ampliar alcance y riesgo de desviar la entrega principal.

## 5) Integracion frontend con fuente de verdad backend

- Decision: Mantener decision de acceso admin basada en `actorType` entregado por backend, no por heuristica local de username.
- Rationale: Centraliza autorizacion efectiva y evita inconsistencias entre cliente y servidor.
- Alternatives considered:
  - Basar acceso solo en nombre de usuario: descartado por riesgo de bypass logico e inconsistencias.

## Resolucion de NEEDS CLARIFICATION

Todas las aclaraciones criticas quedaron resueltas en la spec (sesion 2026-03-25). No quedan `NEEDS CLARIFICATION` pendientes para pasar a diseno y tareas.
