# Data Model - Login de Empleado por Correo y Contrasena

## Entidades

### 1) Empleado

Descripcion: identidad de negocio autenticable y administrable.

Campos relevantes:
- clave: string (formato EMP-<numero>)
- correo: string (email, unico global, normalizado)
- passwordHash: string (hash bcrypt, nunca expuesto)
- activo: boolean
- nombre: string
- direccion: string
- telefono: string
- departamentoClave: string | null

Validaciones:
- correo obligatorio, formato email, max 150.
- contrasena de entrada entre 8 y 72 caracteres (en alta/actualizacion permitida).
- activo controla acceso de autenticacion.

### 2) PerfilAutenticado

Descripcion: respuesta contractual de perfil para actor autenticado.

Campos:
- actorType: enum (ADMIN, EMPLEADO)
- username: string
- displayName: string
- permissions: string[]
- empleado: EmpleadoResumen | null

Reglas:
- Si actorType=ADMIN, empleado=null y permisos administrativos.
- Si actorType=EMPLEADO, permisos=[SELF] y empleado incluye correo/estado activo.

### 3) SesionUIEmpleado (ephemeral)

Descripcion: estado de autenticacion en frontend para actor empleado.

Campos:
- credentials: { username, password } en memoria
- profile: PerfilAutenticado | null
- loading: boolean
- errorMessage: string

Reglas:
- no persistencia en localStorage/sessionStorage.
- limpieza completa al logout o invalidacion backend (401/403).

### 4) EventoAutenticacion

Descripcion: auditoria de intentos de autenticacion.

Campos minimos:
- timestamp
- resultado (EXITO/FALLO)
- identificador (correo normalizado o null)
- origenSolicitud
- motivo (interno: AUTENTICACION_OK, CREDENCIALES_INVALIDAS, CUENTA_INACTIVA)

Reglas:
- motivo interno no se expone al cliente.
- cliente recibe 401 generico en fallo.

## Relaciones

- Empleado 1..n EventoAutenticacion (logico por identificador/correo).
- PerfilAutenticado referencia 0..1 EmpleadoResumen segun actor.
- SesionUIEmpleado referencia PerfilAutenticado cuando login exitoso.

## Transiciones de estado

### Flujo de login empleado
1. No autenticado -> enviando credenciales.
2. Credenciales validas + empleado activo -> autenticado (SELF).
3. Credenciales invalidas o empleado inactivo -> no autenticado con error generico 401.

### Flujo de sesion UI
1. Estado inicial sin perfil.
2. login() exitoso: setCredentials + setProfile.
3. logout()/invalidateFromBackend: clear() total en memoria.
