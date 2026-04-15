# Quickstart - Corregir Rol Admin En Login

## Prerrequisitos

- Docker + Docker Compose
- Java 17
- Node.js 20+
- npm 10+

## 1) Levantar infraestructura y backend

```bash
docker compose up -d
cd backend
./mvnw spring-boot:run
```

Validar que backend responde en `http://localhost:8080/actuator/health`.

## 2) Verificar credenciales admin por defecto local/dev

Variables efectivas esperadas:

- `APP_BASIC_USER=admin`
- `APP_BASIC_PASSWORD=admin123`

Opcional para forzar entorno:

```bash
export APP_BASIC_USER=admin
export APP_BASIC_PASSWORD=admin123
```

## 3) Levantar frontend

```bash
cd frontend
npm install
npm start
```

Frontend en `http://localhost:4200`.

## 4) Caso principal: login admin habilita CRUD

1. Abrir `http://localhost:4200`.
2. Ingresar `admin` / `admin123`.
3. Ejecutar login.

Resultado esperado:

- `auth/me` responde perfil de tipo `ADMIN`.
- Se habilita panel administrativo.
- Se pueden consultar listados CRUD sin error de clasificacion como empleado.

## 5) Caso negativo: empleado no accede a area admin

1. Iniciar sesion con credenciales validas de empleado.
2. Intentar abrir/usar area administrativa.

Resultado esperado:

- El backend rechaza acceso administrativo con `403`.
- El frontend muestra mensaje de permisos insuficientes.

## 6) Caso negativo: credenciales invalidas

1. Probar usuario o contrasena invalidos.

Resultado esperado:

- Respuesta `401` con `code=AUTH_INVALIDA`.
- Mensaje de autenticacion invalida.
- No se crea sesion en frontend.

## 7) Caso negativo: usuario autenticado sin permisos admin

1. Iniciar sesion como empleado.
2. Intentar consumir un recurso administrativo de CRUD.

Resultado esperado:

- Respuesta `403` con `code=NO_AUTORIZADO`.
- Frontend informa falta de permisos.

## 8) Verificar contrato de `auth/me`

Con curl:

```bash
curl -i -u admin:admin123 http://localhost:8080/api/v1/empleados/auth/me
```

Resultado esperado:

- `HTTP/1.1 200 OK`
- Payload con `actorType=ADMIN` y `empleado=null`.

## 9) Verificar gobernanza de listados (`page`, `size`, versionado)

Listados minimos obligatorios:

```bash
curl -i -u admin:admin123 "http://localhost:8080/api/v1/empleados"
curl -i -u admin:admin123 "http://localhost:8080/api/v1/departamentos"
curl -i -u admin:admin123 "http://localhost:8080/api/v1/departamentos/DEP-0000/empleados"
```

Resultado esperado:

- Respuesta `200 OK`.
- En payload paginado se observa `size=5` cuando no se envia parametro `size`.

Verificacion de rutas no versionadas:

```bash
curl -i -u admin:admin123 "http://localhost:8080/empleados"
curl -i -u admin:admin123 "http://localhost:8080/departamentos"
curl -i -u admin:admin123 "http://localhost:8080/departamentos/DEP-0000/empleados"
```

Resultado esperado:

- `404 Not Found` en las tres rutas.

## 10) Verificacion de regresion minima

- Listado de empleados sigue usando `/api/v1/empleados?page={n}&size={n}`.
- Cuando no se envia `size`, backend conserva default `size=5`.
- Swagger/OpenAPI refleja contrato actualizado de `auth/me`.

## 11) Pruebas recomendadas

```bash
cd backend
./mvnw test

cd ../frontend
npm test -- --watch=false
npm run build
```

Resultado esperado: pruebas y build en verde.
