# Quickstart - Vista de Empleado Solo Lectura

## Prerrequisitos

- Docker + Docker Compose
- Java 17
- Node.js 20+
- npm

## Levantar entorno

```bash
docker compose up -d
```

Verificacion basica:

```bash
curl -fsS http://localhost:8080/actuator/health
curl -fsS http://localhost:4200
```

## Flujo funcional esperado (manual)

1. Abrir `http://localhost:4200/empleado/login`.
2. Autenticarse con un empleado activo.
3. Confirmar que se muestran:
   - listado paginado de empleados (solo campos `id,nombre,correo,departamento`)
   - listado paginado de departamentos (solo campos `id,nombre`)
4. Confirmar que no aparecen acciones de crear/editar/eliminar en la UI.

## Validacion API por permisos

### 1) Lectura permitida para empleado

```bash
curl -i -u "empleado@empresa.com:MiPassword123" \
  "http://localhost:8080/api/v1/empleados?page=0&size=5"
```

```bash
curl -i -u "empleado@empresa.com:MiPassword123" \
  "http://localhost:8080/api/v1/departamentos?page=0&size=5"
```

Esperado: `200 OK` en ambos listados.

### 2) Escritura denegada para empleado

```bash
curl -i -u "empleado@empresa.com:MiPassword123" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"X","correo":"x@empresa.com","contrasena":"Password123"}' \
  "http://localhost:8080/api/v1/empleados"
```

```bash
curl -i -u "empleado@empresa.com:MiPassword123" \
  -X DELETE "http://localhost:8080/api/v1/departamentos/DEP-1001"
```

Esperado: `403 Forbidden` en operaciones de escritura.

### 3) Defaults de paginacion

```bash
curl -i -u "empleado@empresa.com:MiPassword123" \
  "http://localhost:8080/api/v1/empleados"
```

Esperado: respuesta paginada con `size=5` por defecto.

## Pruebas automatizadas sugeridas

### Backend

```bash
cd backend
./mvnw test
```

### Frontend unit

```bash
cd frontend
npm run test -- --watch=false
```

### Frontend E2E (empleado)

```bash
cd frontend
npm run e2e -- --spec cypress/e2e/empleado-login-smoke.cy.ts --browser electron --headless --config baseUrl=http://localhost:4200
```

## Criterios de salida

- Empleado autenticado visualiza ambos listados en modo solo lectura.
- CRUD bloqueado para EMPLEADO con `403`.
- Paginacion constitucional (`size=5` default) preservada.
- UI y backend alineados en control de permisos.
