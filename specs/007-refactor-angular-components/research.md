# Phase 0 Research - Refactor Angular Con Componentes

## 1) Arquitectura de estado y composición por feature

- Decision: Usar contenedores por feature (smart components) para orquestación de estado y componentes presentacionales puros para render/UI.
- Rationale: Cumple la clarificación aprobada y reduce acoplamiento entre lógica de negocio de vista y presentación.
- Alternatives considered:
  - Estado totalmente distribuido en cada componente: descartado por dificultad para coordinar flujos asíncronos y errores compartidos.
  - Store global único en esta fase: descartado por complejidad innecesaria para el alcance actual.

## 2) Estrategia de modularización del app actual

- Decision: Dividir `app.ts`/`app.html` en un contenedor administrativo y subcomponentes por dominio (`auth`, `empleados`, `departamentos`) más componentes de feedback reutilizables.
- Rationale: La pantalla actual concentra demasiadas responsabilidades y dificulta mantenimiento y pruebas.
- Alternatives considered:
  - Mantener monolito y solo mover estilos: descartado porque no resuelve deuda estructural.
  - Reescribir todo desde cero: descartado para evitar regresión funcional y sobrecosto.

## 3) Reutilización mínima obligatoria

- Decision: Cubrir reutilización mínima en formularios, listados/tablas y feedback (`loading`, `error`, `empty`) como baseline medible de la feature.
- Rationale: Alinea SC-002 con una implementación verificable y de alto impacto.
- Alternatives considered:
  - Solo formularios o solo listados: descartado por beneficio parcial y riesgo de duplicación residual.

## 4) Contrato de integración con backend existente

- Decision: No cambiar endpoints ni semántica backend; mantener consumo de `/api/v1/empleados/auth/me`, listados paginados y códigos `AUTH_INVALIDA`/`NO_AUTORIZADO`.
- Rationale: El objetivo es refactor frontend sin alterar comportamiento de negocio.
- Alternatives considered:
  - Ajustar payloads en backend para simplificar frontend: descartado por ampliar alcance y romper estabilidad contractual.

## 5) Estrategia de pruebas para refactor seguro

- Decision: Adoptar estrategia E2E híbrida con 2 smoke reales obligatorios por PR (`login+navegación`, `crear+listar Empleados`) y escenarios adicionales con stubs para regresión estable.
- Rationale: Balancea fidelidad de integración (proxy, auth, backend real) con estabilidad y tiempo de ejecución en CI.
- Alternatives considered:
  - Todo real backend: descartado por flakiness/costo en pipeline.
  - Todo con stubs: descartado por perder cobertura crítica de integración.
  - Validación manual exclusiva: descartado por baja repetibilidad y menor confianza.

## Resolución de NEEDS CLARIFICATION

No quedan NEEDS CLARIFICATION abiertas en esta feature. Las decisiones de arquitectura de estado y alcance de reutilización fueron resueltas en la sesión de clarificación del 2026-03-25.
