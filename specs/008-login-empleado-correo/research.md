# Research - Login de Empleado por Correo y Contrasena

## Decision 1: Reutilizar HTTP Basic existente para login de empleado

- Decision: El flujo de login de empleado usara HTTP Basic sobre endpoints existentes, sin introducir JWT ni endpoint de login separado en backend.
- Rationale: El backend ya soporta autenticacion de empleado por correo+contrasena en SecurityConfig y valida actor en /api/v1/empleados/auth/me.
- Alternatives considered:
  - Crear endpoint POST de login con sesion/token: descartado por ampliar alcance y modelo de seguridad.
  - Introducir JWT en este feature: descartado por no estar en requerimientos ni constitucion.

## Decision 2: Ruta dedicada de frontend para actor empleado

- Decision: Se agrega ruta dedicada /empleado/login en frontend para separar experiencia de empleado del shell administrativo.
- Rationale: Evita ambiguedad UX entre actores, facilita pruebas E2E y reduce riesgo de permisos mezclados.
- Alternatives considered:
  - Reusar formulario admin actual: descartado por mezclar flujos y mensajes de permisos.
  - Toggle de actor en misma pantalla: descartado por complejidad de estado y mayor riesgo de errores.

## Decision 3: Sesion en memoria de UI

- Decision: Credenciales de empleado solo se mantienen en memoria (AuthSessionService), sin localStorage/sessionStorage.
- Rationale: Mejora postura de seguridad y cumple aclaracion de spec.
- Alternatives considered:
  - sessionStorage para refrescos: descartado por persistencia de secretos en navegador.
  - localStorage para "recordarme": descartado por mayor superficie de exposicion.

## Decision 4: Respuesta 401 generica para credenciales invalidas e inactivo

- Decision: Cliente recibe siempre 401 generico en fallos de login de empleado, incluyendo cuenta inactiva.
- Rationale: Mitiga enumeracion de cuentas y mantiene comportamiento uniforme hacia cliente.
- Alternatives considered:
  - 403 especifico para inactivo: descartado por revelar estado de cuenta.
  - Mensaje distinto para inactivo en UI: descartado por misma razon de seguridad.

## Decision 5: Mantener alcance funcional SELF para actor EMPLEADO

- Decision: Actor empleado autenticado solo accede a /api/v1/empleados/auth/me en esta feature.
- Rationale: Cumple principio de menor privilegio y evita regresiones en endpoints administrativos.
- Alternatives considered:
  - Permitir listados para empleado: descartado por romper limite de alcance y permisos.
  - CRUD propio de empleado: descartado por requerir nuevo feature.

## Decision 6: Validacion contractual y smoke real obligatorios

- Decision: Se define contrato OpenAPI especifico de la feature y smoke E2E real de login empleado.
- Rationale: Alinea implementacion con constitucion y permite evidencia reproducible en CI/local.
- Alternatives considered:
  - Solo pruebas unitarias: descartado por baja cobertura end-to-end.
  - Solo documentar sin smoke: descartado por falta de verificacion operacional.
