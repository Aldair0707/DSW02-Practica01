# Quickstart - Login Empleado por Correo y Contrasena

## Prerrequisitos

- Docker y Docker Compose
- Java 17
- Node.js 20+
- npm

## Levantar stack

1. Stack completo (postgres + backend + frontend NGINX):

```bash
docker compose up -d
```

2. Verificar servicios:

```bash
curl -fsS http://localhost:4200
curl -fsS http://localhost:8080/actuator/health
```

## Flujo manual de validacion

1. Abrir http://localhost:4200/empleado/login
2. Ingresar correo y contrasena de empleado activo.
3. Confirmar que frontend consulta GET /api/v1/empleados/auth/me y recibe actorType=EMPLEADO.
4. Confirmar que no hay credenciales guardadas en localStorage/sessionStorage.

## Casos de validacion clave

### Caso feliz

```bash
curl -u "empleado@empresa.com:MiPassword123" \
  http://localhost:8080/api/v1/empleados/auth/me
```

Esperado: 200 con actorType=EMPLEADO y permissions=[SELF].

### Credenciales invalidas

```bash
curl -i -u "empleado@empresa.com:incorrecta" \
  http://localhost:8080/api/v1/empleados/auth/me
```

Esperado: 401 con mensaje generico.

### Cuenta inactiva

```bash
curl -i -u "empleado.inactivo@empresa.com:MiPassword123" \
  http://localhost:8080/api/v1/empleados/auth/me
```

Esperado: 401 con el mismo mensaje generico que credenciales invalidas.

## Pruebas automatizadas sugeridas

- Backend:

```bash
cd backend
./mvnw test
```

- Frontend unit:

```bash
cd frontend
npm run test -- --watch=false
```

- Frontend smoke E2E empleado:

```bash
cd frontend
npm run e2e -- --spec cypress/e2e/empleado-login-smoke.cy.ts --browser electron --headless --config baseUrl=http://localhost:4200
```

- Script de humo operativo (incluye 401 generico por password incorrecta e inactivo):

```bash
BASE_URL=http://localhost:8080 APP_BASIC_USER=admin APP_BASIC_PASSWORD=admin123 \
  ./scripts/smoke/empleados-smoke.sh
```
