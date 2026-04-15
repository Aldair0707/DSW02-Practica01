# Feature Specification: Refactor Angular Con Componentes

**Feature Branch**: `007-refactor-angular-components`  
**Created**: 2026-03-25  
**Status**: Draft  
**Input**: User description: "ahora quiero que implementes correctamente el proyecto de angular, veo que no se utilizaron los componentes, quiero que el proyecto tenga buenas prácticas"

## Clarifications

### Session 2026-03-25

- Q: ¿Qué arquitectura de estado debe usar el frontend en esta refactorización? → A: B - Estado por feature en contenedores (smart components) y componentes presentacionales puros para UI reutilizable.
- Q: ¿Qué alcance mínimo de reutilización debe cubrir esta feature? → A: C - Reutilizar formularios y listados/tablas, más componentes compartidos de feedback (loading/error/empty).
- Q: ¿Qué herramienta base de calidad frontend se usará para U1? → A: Cypress para pruebas E2E de flujos críticos del MVP.
- Q: ¿Qué estrategia E2E conviene para este proyecto? → A: B - Estrategia híbrida: al menos una prueba smoke con backend real y el resto de escenarios con stubs para estabilidad y velocidad en CI.
- Q: ¿Cuántos escenarios smoke reales deben mantenerse en la estrategia híbrida? → A: C - 2 escenarios smoke reales (login+navegación y un CRUD feliz).
- Q: ¿Cuándo deben ejecutarse los smoke reales? → A: A - En cada PR.
- Q: ¿Qué módulo debe cubrir el smoke CRUD real obligatorio? → A: A - Empleados.
- Q: ¿Qué alcance debe tener el smoke CRUD real de Empleados? → A: B - Crear y listar Empleados.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Modularizar La Pantalla Principal (Priority: P1)

Como administrador quiero que la interfaz de gestión esté separada en componentes claros para que el mantenimiento y evolución del frontend no dependa de un único archivo grande y difícil de entender.

**Why this priority**: Es el mayor cuello de botella actual; sin modularización, cualquier cambio funcional aumenta riesgo de errores y retrabajo.

**Independent Test**: Puede validarse comprobando que el flujo actual (login, navegación entre vistas, creación/edición/listado) funciona igual después de mover secciones de la UI a componentes reutilizables.

**Acceptance Scenarios**:

1. **Given** que el frontend está dividido en componentes por responsabilidad, **When** un desarrollador modifica solo el componente de empleados, **Then** no necesita cambiar componentes no relacionados para completar esa modificación.
2. **Given** que la pantalla principal usa múltiples responsabilidades, **When** se ejecuta el flujo completo de autenticación y gestión, **Then** el comportamiento visible para usuario se mantiene consistente con el existente.

---

### User Story 2 - Reutilizar Componentes De Formularios Y Listados (Priority: P2)

Como equipo de frontend quiero componentes reutilizables para formularios y tablas para evitar duplicación de lógica y diseño entre empleados y departamentos.

**Why this priority**: Reduce deuda técnica y facilita agregar nuevos módulos administrativos con menor esfuerzo.

**Independent Test**: Puede verificarse creando y editando entidades en ambos módulos (empleados y departamentos) y confirmando consistencia visual y funcional en validaciones, acciones y mensajes.

**Acceptance Scenarios**:

1. **Given** que existen componentes compartidos para secciones comunes, **When** se ajusta una regla de presentación compartida, **Then** el cambio se refleja en los módulos que reutilizan ese componente sin duplicar implementación.
2. **Given** que un formulario falla por validación, **When** el usuario corrige los datos, **Then** el componente muestra feedback uniforme y recupera estado operativo.

---

### User Story 3 - Estandarizar Estado, Errores Y Convenciones (Priority: P3)

Como desarrollador quiero convenciones consistentes de manejo de estado, errores y comunicación entre componentes para que el proyecto sea predecible y sencillo de extender.

**Why this priority**: Mejora calidad a medio plazo y reduce defectos por patrones inconsistentes.

**Independent Test**: Puede validarse forzando respuestas de éxito y error en login/listados y confirmando que el frontend muestra mensajes y estados de carga de forma consistente en todos los módulos.

**Acceptance Scenarios**:

1. **Given** que se producen errores de autenticación o permisos, **When** el frontend recibe la respuesta, **Then** los componentes muestran mensajes consistentes y no dejan estados intermedios inválidos.
2. **Given** que una operación asíncrona está en curso, **When** el usuario interactúa con acciones relacionadas, **Then** los componentes respetan estados de carga y evitan acciones duplicadas.

### Edge Cases

- Navegación entre módulos mientras hay una solicitud en curso no debe dejar datos obsoletos en pantalla.
- Reintentos de login tras error no deben mezclar mensajes anteriores con resultados nuevos.
- Cancelar edición de una entidad debe restaurar el formulario a un estado válido y limpio.
- Cambios en componentes compartidos no deben romper flujos existentes de creación, edición y eliminación.
- Cuando la sesión expira, los componentes deben invalidar estado sensible y redirigir al flujo de autenticación.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El frontend DEBE dividir la pantalla administrativa en componentes con responsabilidad única para autenticación, navegación, formularios y listados.
- **FR-002**: El frontend DEBE reutilizar componentes compartidos para patrones repetidos de UI y evitar duplicación de lógica entre módulos administrativos.
- **FR-003**: El frontend DEBE mantener el comportamiento funcional actual de login, visualización y operaciones CRUD administrativas durante la refactorización.
- **FR-004**: El frontend DEBE establecer una convención consistente para manejo de estados de carga, éxito y error por feature, concentrando la orquestación en componentes contenedores (smart components).
- **FR-005**: El frontend DEBE estandarizar la comunicación entre componentes usando componentes presentacionales puros para UI reutilizable y minimizando acoplamiento directo.
- **FR-006**: El frontend DEBE mostrar mensajes de error coherentes para autenticación inválida y falta de permisos en todo el flujo administrativo.
- **FR-007**: La refactorización DEBE incluir estructura de archivos y nombres de componentes comprensibles para facilitar mantenibilidad.
- **FR-008**: El frontend DEBE preservar compatibilidad con los contratos backend existentes de esta feature sin introducir cambios de comportamiento no solicitados.
- **FR-009**: La solución DEBE actualizar o añadir pruebas del frontend para cubrir los flujos críticos después de la refactorización por componentes.
- **FR-010**: Para U1 (MVP), la solución DEBE incluir pruebas E2E con Cypress para login administrativo y navegación principal entre módulos.
- **FR-011**: La estrategia E2E DEBE ser híbrida: al menos dos escenarios smoke ejecutando contra backend real (login+navegación y crear+listar Empleados), y los escenarios adicionales orientados a regresión ejecutando con stubs de red.
- **FR-012**: Los escenarios smoke E2E contra backend real DEBEN ejecutarse en cada PR como gate de integración.

### Key Entities *(include if feature involves data)*

- **Contenedor Administrativo**: Componente orquestador que coordina sesión, navegación interna y carga inicial de datos.
- **Componente De Autenticación**: Componente dedicado al ingreso de credenciales y feedback del proceso de login.
- **Componente De Formulario Reutilizable**: Componente parametrizable para alta/edición de entidades administrativas.
- **Componente De Listado Reutilizable**: Componente para renderizar colecciones con acciones, paginación y estados vacíos/carga.
- **Estado De Vista**: Modelo de estado de UI que representa carga, error, éxito y datos activos por módulo.

## Assumptions & Dependencies

- El backend y sus contratos para autenticación, errores y listados ya están definidos y deben mantenerse como fuente de verdad.
- Esta feature no requiere cambios de negocio en endpoints; su foco es arquitectura y mantenibilidad del frontend.
- El flujo administrativo actual es funcional y se toma como baseline para evitar regresiones.
- El equipo seguirá la convención de estructura por dominio ya usada en el proyecto.
- Cypress se adopta como herramienta de calidad E2E para validar U1 en local y en pipeline.
- En CI se prioriza enfoque híbrido (smoke real + regresión con stubs) para balancear cobertura de integración y estabilidad.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de los flujos críticos de administración (login, listar, crear, editar, eliminar) sigue funcionando tras la refactorización.
- **SC-002**: Al menos el 80% de secciones repetidas de UI administrativa queda implementado mediante componentes reutilizables, cubriendo como mínimo formularios, listados/tablas y componentes de feedback (loading/error/empty).
- **SC-003**: El tiempo promedio para ubicar el punto de cambio de una modificación de UI administrativa se reduce en al menos 40% según validación del equipo.
- **SC-004**: El frontend mantiene ejecución exitosa de pruebas y build en el flujo de calidad del proyecto después de la refactorización.
- **SC-005**: El 100% de los escenarios críticos definidos para U1 (login y navegación principal entre módulos) ejecuta en verde en la suite E2E de Cypress.
- **SC-006**: La suite E2E mantiene al menos 2 escenarios smoke contra backend real (login+navegación y crear+listar Empleados) y el resto de escenarios críticos en modo stub, con ejecución estable en pipeline.
- **SC-007**: El gate de PR ejecuta en verde ambos smoke reales obligatorios antes de permitir integración de cambios.
