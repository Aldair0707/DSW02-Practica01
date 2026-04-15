# Data Model - Refactor Angular Con Componentes

## Entity: AdminShellState

### Description
Estado agregado del contenedor administrativo principal que orquesta sesión, módulo activo y cargas iniciales.

### Fields

- `isAuthenticated` (`boolean`)
- `activeView` (`'empleados' | 'departamentos'`)
- `authLoading` (`boolean`)
- `globalErrorMessage` (`string | null`)
- `globalSuccessMessage` (`string | null`)

## Entity: FeatureViewState<T>

### Description
Modelo genérico de estado por feature para operaciones asíncronas y datos renderizados.

### Fields

- `loading` (`boolean`)
- `errorMessage` (`string | null`)
- `items` (`T[]`)
- `page` (`number`)
- `size` (`number`, default `5`)
- `totalPages` (`number`)

## Entity: AuthViewModel

### Description
View model de autenticación para componente de login administrativo.

### Fields

- `username` (`string`)
- `password` (`string`)
- `submitDisabled` (`boolean`)
- `validationErrors` (`string[]`)

## Entity: ReusableFormConfig

### Description
Configuración declarativa para formularios reutilizables de alta/edición.

### Fields

- `mode` (`'create' | 'edit'`)
- `submitLabel` (`string`)
- `cancelVisible` (`boolean`)
- `fieldErrors` (`Record<string, string[]>`)
- `isSubmitting` (`boolean`)

## Entity: FeedbackState

### Description
Modelo de presentación unificada para componentes de feedback reutilizables.

### Fields

- `type` (`'loading' | 'error' | 'empty' | 'success'`)
- `message` (`string`)
- `visible` (`boolean`)

## Relationships

- `AdminShellState` coordina estados específicos `FeatureViewState<Empleado>` y `FeatureViewState<Departamento>`.
- `AuthViewModel` alimenta la autenticación y actualiza `AdminShellState.isAuthenticated`.
- `ReusableFormConfig` es consumido por componentes de formulario para empleados y departamentos.
- `FeedbackState` se proyecta de forma homogénea en cada feature y en el contenedor principal.

## Validation Rules

- Las acciones de mutación deben deshabilitar botones durante `isSubmitting=true`.
- Los listados deben mantener `size=5` por defecto cuando no se especifique.
- Los errores de autenticación/autorización se traducen de forma uniforme según código funcional backend.
- La vista activa no debe cambiar a estado inconsistente durante cargas en progreso.

## State Transitions

- `unauthenticated -> authenticating -> authenticated`
- `authenticating -> auth_error` (credenciales/permisos)
- `feature_idle -> feature_loading -> feature_loaded`
- `feature_loading -> feature_error`
- `feature_editing -> feature_submitting -> feature_loaded`
