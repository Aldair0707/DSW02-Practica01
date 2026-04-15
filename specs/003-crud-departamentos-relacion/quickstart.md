# Quickstart - CRUD de Departamentos y Relacion con Empleados

## Prerrequisitos

- Java 17
- Docker y Docker Compose
- Maven 3.9+

## Variables de entorno

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `APP_BASIC_USER` (default dev: `admin`)
- `APP_BASIC_PASSWORD` (default dev: `admin123`)

Ejemplo:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=empleados_db
export DB_USER=empleados_user
export DB_PASSWORD=empleados_pass
export APP_BASIC_USER=admin
export APP_BASIC_PASSWORD=admin123
```

## Levantar stack

```bash
docker compose up --build
```

## Ejecucion local backend

```bash
docker compose up -d postgres
cd backend
mvn spring-boot:run
```

## Flujo de validacion funcional

### 1) Crear departamento

```bash
curl -i -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  -H "Content-Type: application/json" \
  -X POST http://localhost:8080/api/v1/departamentos \
  -d '{"nombre":"Sistemas"}'
```

Esperado: `201 Created` con `clave` tipo `DEP-XXXX`.

### 2) Listar departamentos (paginacion default)

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  "http://localhost:8080/api/v1/departamentos?page=0"
```

Esperado: `200 OK` y `size=5` por defecto si se omite.

### 3) Obtener departamento por clave

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  http://localhost:8080/api/v1/departamentos/DEP-1001
```

Esperado: `200 OK` con datos de departamento (sin empleados embebidos).

### 4) Actualizar nombre de departamento

```bash
curl -i -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  -H "Content-Type: application/json" \
  -X PUT http://localhost:8080/api/v1/departamentos/DEP-1001 \
  -d '{"nombre":"Tecnologia"}'
```

Esperado: `200 OK` y `clave` sin cambios.

### 5) Asignar departamento a empleado

```bash
curl -i -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  -H "Content-Type: application/json" \
  -X PATCH http://localhost:8080/api/v1/empleados/EMP-1001/departamento \
  -d '{"departamentoClave":"DEP-1001"}'
```

Esperado: `200 OK`.

### 6) Asignar departamento inexistente

```bash
curl -i -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  -H "Content-Type: application/json" \
  -X PATCH http://localhost:8080/api/v1/empleados/EMP-1001/departamento \
  -d '{"departamentoClave":"DEP-9999"}'
```

Esperado: `404 Not Found`.

### 7) Listar empleados por departamento

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  "http://localhost:8080/api/v1/departamentos/DEP-1001/empleados?page=0"
```

Esperado: `200 OK` con lista paginada y `size=5` por defecto.

### 8) Intentar eliminar departamento con empleados

```bash
curl -i -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  -X DELETE http://localhost:8080/api/v1/departamentos/DEP-1001
```

Esperado: `409 Conflict` cuando existen empleados asociados.

### 9) Eliminar departamento sin empleados

```bash
curl -i -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  -X DELETE http://localhost:8080/api/v1/departamentos/DEP-1002
```

Esperado: `204 No Content`.

## Pruebas automatizadas

```bash
cd backend
mvn test
```

## Swagger/OpenAPI

- UI: `http://localhost:8080/swagger-ui.html`
- Contrato de la feature: `specs/003-crud-departamentos-relacion/contracts/departamentos-openapi.yaml`

## Alcance explicito

- `departamentoClave` no se setea manualmente en alta inicial de empleado.
- Los nombres de departamento pueden repetirse.
- La clave de departamento es unica e inmutable.
- Existe migracion de historicos sin departamento hacia `DEP-0000` (`Sin asignar`).
