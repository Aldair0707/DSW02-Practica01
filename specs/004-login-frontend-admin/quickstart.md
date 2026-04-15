# Quickstart - Login Frontend con Admin

## Prerrequisitos

- Node.js 20+ recomendado (LTS par)
- npm 10+
- Java 17
- Docker + Docker Compose

## 1) Levantar backend

Desde raiz del repo:

```bash
docker compose up -d postgres
cd backend
mvn spring-boot:run
```

Verificar que el backend este en `http://localhost:8080`.

## 2) Configurar credenciales default de desarrollo

Si no se sobreescriben variables, backend usa:

- `APP_BASIC_USER=admin`
- `APP_BASIC_PASSWORD=admin123`

Opcionalmente exportar de forma explicita:

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

Esto levanta Angular con proxy a backend (`proxy.conf.json`) en `http://localhost:4200`.

## 4) Validar login desde UI

1. Abrir `http://localhost:4200`
2. En login ingresar:
   - Usuario: `admin`
   - Contrasena: `admin123`
3. Presionar `Entrar`

Resultado esperado:

- Se muestra panel autenticado.
- Se cargan listados de empleados y departamentos.

## 5) Validar error de credenciales

1. Ingresar password incorrecta.
2. Presionar `Entrar`.

Resultado esperado:

- Permanece en pantalla de login.
- Se muestra error visible de autenticacion.

## 6) Validar bloqueo por campos vacios

1. Dejar `Usuario o correo` vacio.
2. Dejar `Contrasena` vacia.
3. Presionar `Entrar`.

Resultado esperado:

- No se ejecuta login contra backend.
- Se muestran mensajes de validacion por campo.

## 7) Validar bloqueo por espacios

1. Ingresar `admin test` en usuario o `admin 123` en contrasena.
2. Presionar `Entrar`.

Resultado esperado:

- No se ejecuta login contra backend.
- Se muestra mensaje: "Usuario y contrasena son obligatorios y no deben contener espacios."

## 8) Validar logout

1. Con sesion activa, presionar `Cerrar sesion`.

Resultado esperado:

- Vuelve a formulario de login.
- Se limpia estado de datos cargados.

## 9) Build rapido

```bash
cd frontend
npm run build
```

Resultado esperado: build completado sin errores.

## 10) Validar regla de no-endpoints/backend contratos

Desde raiz del repo:

```bash
./scripts/quality/verify-no-backend-endpoints-or-contract-changes.sh
```

Resultado esperado: `OK: No se detectaron cambios de endpoints backend ni contratos OpenAPI YAML para este feature.`
