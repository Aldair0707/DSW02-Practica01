# Phase 0 Research - CRUD Empleados Desde Admin

## 1) Alcance de autenticacion para habilitar CRUD

- Decision: Mantener el acceso a la pantalla CRUD de empleados restringido a perfil administrador autenticado con Basic Auth.
- Rationale: El feature define control administrativo y debe evitar operaciones de gestion desde perfiles no autorizados.
- Alternatives considered:
  - Permitir cualquier usuario autenticado: rechazado por incumplir el objetivo de control administrativo.
  - Crear un flujo de roles nuevo en backend: rechazado por ampliar alcance y requerir cambios de contrato.

## 2) Estrategia de consumo API para CRUD de empleados

- Decision: Consumir exclusivamente endpoints existentes bajo `/api/v1/empleados` para listar, crear, editar y eliminar.
- Rationale: Cumple gobernanza constitucional de API y evita introducir deuda por cambios de backend innecesarios.
- Alternatives considered:
  - Exponer endpoints frontend-specific en backend: rechazado por fuera de alcance.

## 3) Paginacion y navegacion de listado

- Decision: Usar paginacion por `page` y `size`, respetando default backend `size=5` cuando no se indique.
- Rationale: Mantiene consistencia con contrato actual y escalabilidad para volumen de empleados.
- Alternatives considered:
  - Cargar todos los empleados en una sola consulta: rechazado por costo y falta de alineacion con el backend.

## 4) Validaciones de formulario en frontend

- Decision: Bloquear submit en campos obligatorios vacios, con solo espacios o formato invalido antes de invocar la API.
- Rationale: Mejora experiencia de usuario y reduce errores evitables en backend.
- Alternatives considered:
  - Delegar toda validacion al backend: rechazado por mala UX y retroalimentacion tardia.

## 5) Regla de unicidad de correo

- Decision: Tratar correo de empleado como unico global.
- Rationale: Evita colisiones de identidad y alinea validacion funcional con operacion administrativa.
- Alternatives considered:
  - Unicidad solo entre activos: rechazado por ambiguedad operativa.
  - Correo no unico: rechazado por riesgo de inconsistencia.

## 6) Comportamiento de contrasena en edicion

- Decision: En edicion, la contrasena es opcional; si queda vacia se conserva la actual.
- Rationale: Reduce friccion en actualizaciones administrativas sin cambios de credencial.
- Alternatives considered:
  - Exigir contrasena siempre en edicion: rechazado por carga operativa.
  - Bloquear cambio de contrasena en este flujo: rechazado por limitar administracion.

## 7) Departamento en alta/edicion

- Decision: El departamento es opcional y puede permanecer sin asignar en alta o edicion.
- Rationale: Permite registrar empleados aun cuando la asignacion organizacional no este definida al momento.
- Alternatives considered:
  - Departamento obligatorio en toda operacion: rechazado por bloquear casos reales.

## 8) Eliminacion segura

- Decision: Exigir confirmacion explicita antes de eliminar un empleado y refrescar listado al confirmar.
- Rationale: Reduce errores operativos y mantiene estado de pantalla coherente.
- Alternatives considered:
  - Eliminar con un solo clic: rechazado por riesgo de acciones accidentales.

## 9) Manejo de errores de red y autorizacion

- Decision: Diferenciar errores de autenticacion/autorizacion (`401/403`) de fallos de conectividad o negocio para mensajes claros.
- Rationale: Permite al administrador identificar si debe reautenticar, corregir datos o revisar disponibilidad del backend.
- Alternatives considered:
  - Mensaje de error unico: rechazado por baja capacidad de diagnostico.

## 10) Sesion administrativa y coherencia de estado

- Decision: Mantener sesion en memoria y terminarla por cierre manual o invalidacion backend (`401/403`), sin temporizador de inactividad en esta iteracion.
- Rationale: Mantiene seguridad y simplicidad operativa acorde al alcance definido.
- Alternatives considered:
  - Persistir credenciales en almacenamiento web: rechazado por riesgo de seguridad y no requerido para esta fase.
  - Definir timeout de inactividad ahora: rechazado por fuera de alcance de esta iteracion.

## Resolucion de NEEDS CLARIFICATION

No quedan NEEDS CLARIFICATION en el contexto tecnico de esta feature.
