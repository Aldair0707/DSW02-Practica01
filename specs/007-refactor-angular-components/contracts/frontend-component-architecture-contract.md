# Frontend Component Architecture Contract

## Scope

Este contrato define la arquitectura y reglas de integración frontend para la refactorización a componentes en la feature 007, sin alterar contratos HTTP backend existentes.

## Component Contract

## 1) Feature Containers (smart)

- Deben centralizar orquestación de estado, coordinación de servicios y side-effects.
- No deben contener marcado repetitivo de tablas/formularios/feedback.
- Deben exponer entradas/salidas claras a componentes presentacionales.

## 2) Presentational Components (dumb)

- Reciben `@Input()` con datos serializables y emiten `@Output()` para intenciones de usuario.
- No realizan llamadas HTTP directas.
- No gestionan estado de negocio global.

## 3) Reusable UI Components mínimos

- Formularios reutilizables:
  - Soportan modo alta/edición.
  - Exponen eventos `submit` y `cancel`.
- Listas/Tablas reutilizables:
  - Renderizan data tipada.
  - Exponen eventos de paginación/selección/acciones.
- Feedback reutilizable:
  - `loading`, `error`, `empty` con API consistente.

## API Consumption Contract

- Endpoints backend no cambian.
- Se mantiene compatibilidad con:
  - `GET /api/v1/empleados/auth/me`
  - Endpoints de listado paginados ya existentes.
- Manejo de errores funcionales:
  - `AUTH_INVALIDA` -> tratamiento UI para reintento/login.
  - `NO_AUTORIZADO` -> mensaje de permisos insuficientes.

## Routing and Composition Contract

- El shell principal compone vistas por feature.
- La navegación debe mantener separación clara entre composición de página y presentación de widgets.
- No se permite volver a concentrar lógica transversal en un único componente equivalente al `app.ts` monolítico actual.

## Testability Contract

- Cada componente reusable debe tener pruebas unitarias de entradas/salidas.
- Cada contenedor principal debe tener prueba de integración de flujo feliz y error principal.
- Build y test frontend deben pasar como gate de aceptación.
- La estrategia E2E debe ser híbrida.
- En cada PR deben ejecutarse y pasar 2 smoke reales contra backend:
  - Login + navegación principal.
  - Crear + listar Empleados.
- Las pruebas E2E adicionales de regresión deben priorizar stubs de red para estabilidad.

## Out of Scope

- Cambios de contratos backend, versionado API o estructura de seguridad.
- Incorporación de una librería de estado global (NGRX/NGXS) en esta iteración.
