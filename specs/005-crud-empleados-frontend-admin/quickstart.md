# Quickstart - CRUD Empleados Desde Admin

## Prerrequisitos

- Node.js 20+ recomendado
- npm 10+
- Java 17
- Docker + Docker Compose

## 1) Levantar backend

Desde la raiz del repositorio:

```bash
docker compose up -d postgres
cd backend
mvn spring-boot:run
```

Verificar backend en http://localhost:8080.

## 2) Configurar credenciales locales de referencia

Valores por defecto en desarrollo:

- `APP_BASIC_USER=admin`
- `APP_BASIC_PASSWORD=admin123`

Opcional:

```bash
export APP_BASIC_USER=admin
export APP_BASIC_PASSWORD=admin123
```

## 3) Levantar frontend

En otro terminal:

```bash
cd frontend
npm install
npm start
```

Frontend disponible en http://localhost:4200.

## 4) Validar acceso admin al CRUD

1. Abrir http://localhost:4200
2. Ingresar usuario admin y contrasena valida
3. Entrar al panel

Resultado esperado:

- Se habilita la vista CRUD de empleados.
- El listado de empleados se carga paginado.

## 5) Validar alta de empleado

1. Completar formulario de nuevo empleado con datos validos.
2. Confirmar alta.

Resultado esperado:

- Mensaje de exito visible.
- Empleado visible en listado actualizado.

## 6) Validar edicion de empleado

1. Seleccionar un empleado existente.
2. Editar datos permitidos.
3. Confirmar actualizacion.

Resultado esperado:

- Mensaje de exito visible.
- Cambios reflejados en la fila del empleado.
- Si la contrasena queda vacia en edicion, se conserva la contrasena previa.

## 7) Validar eliminacion con confirmacion

1. Seleccionar eliminar en un empleado.
2. Confirmar la accion.

Resultado esperado:

- Registro eliminado del listado.
- Estado de pagina consistente tras refresco.

## 8) Validar bloqueos de formulario

1. Intentar guardar con campos vacios o solo espacios.
2. Intentar guardar con correo invalido.

Resultado esperado:

- No se envia request al backend.
- Se muestran errores de validacion en UI.

## 9) Validar unicidad global de correo

1. Intentar crear o editar un empleado con un correo ya asignado a otro empleado.

Resultado esperado:

- El backend rechaza la operacion por regla de unicidad.
- La UI muestra mensaje de error de negocio.

## 10) Validar departamento opcional

1. Crear o editar empleado sin seleccionar departamento.

Resultado esperado:

- La operacion se permite.
- El empleado queda como `Sin asignar` en el listado.

## 11) Validar paginacion

1. Navegar con botones Anterior/Siguiente.
2. Verificar cambios de pagina.

Resultado esperado:

- Se muestra pagina correcta.
- No se pierde estado de autenticacion.

## 12) Verificar build y pruebas

```bash
cd frontend
npm test -- --watch=false
npm run build
```

Resultado esperado: pruebas y build sin errores.
