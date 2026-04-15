# Quickstart - CRUD de empleado

## Prerrequisitos

- Java 17
- Docker y Docker Compose
- Maven 3.9+

## Variables de entorno

Definir (en `.env` o entorno shell):

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `APP_BASIC_USER`
- `APP_BASIC_PASSWORD`

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

## Levantar con Docker Compose

```bash
docker compose up --build
```

Servicios esperados:

- `postgres` en `5432`
- `app` en `8080`

## Ejecución local (sin contenedor de app)

1. Levantar solo PostgreSQL:

```bash
docker compose up -d postgres
```

2. Arrancar backend:

```bash
cd backend
mvn spring-boot:run
```

## Verificación rápida

### Health check

```bash
curl http://localhost:8080/actuator/health
```

### Crear empleado

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  -H "Content-Type: application/json" \
  -X POST http://localhost:8080/api/v1/empleados \
  -d '{
    "nombre":"Juan Pérez",
    "direccion":"Calle Uno 123",
    "telefono":"5551234567"
  }'

# Nota: la respuesta debe incluir una clave generada con formato EMP-<autonumérico> (ej. EMP-1001)
```

### Obtener empleado

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  http://localhost:8080/api/v1/empleados/EMP-1001
```

### Listar empleados

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  "http://localhost:8080/api/v1/empleados?page=0&size=5"

# Nota: si se omite `size`, el endpoint debe usar `size=5` por defecto.
```

### Actualizar empleado

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  -H "Content-Type: application/json" \
  -X PUT http://localhost:8080/api/v1/empleados/EMP-1001 \
  -d '{
    "nombre":"Juan P. Actualizado",
    "direccion":"Av Principal 45",
    "telefono":"5557654321"
  }'
```

### Eliminar empleado

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" \
  -X DELETE http://localhost:8080/api/v1/empleados/EMP-1001
```

## Ejecutar pruebas

```bash
cd backend
mvn test
```

## Swagger/OpenAPI

- UI (desarrollo): `http://localhost:8080/swagger-ui.html`
- Contrato de referencia de esta feature: `specs/001-crud-empleado/contracts/empleados-openapi.yaml`