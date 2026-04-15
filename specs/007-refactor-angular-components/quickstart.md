# Quickstart - Feature 007 Refactor Angular Con Componentes

## Prerequisitos

- Node.js 20+
- npm 10+
- Backend disponible en `http://localhost:8080`

## 1) Instalar dependencias frontend

```bash
cd /Users/anderluuna/Documents/DespliegueSoftware/DSW02-Practica01/frontend
npm install
```

## 2) Ejecutar frontend en modo desarrollo

```bash
npm start
```

Esto levanta Angular con `proxy.conf.json` para enrutar llamadas al backend.

## 3) Verificar flujos principales

- Login administrativo funcional.
- Navegación entre features (empleados/departamentos) en contenedores separados.
- Reutilización visible de:
  - Formulario compartido.
  - Tabla/listado compartido.
  - Estados de feedback compartidos (`loading`, `error`, `empty`).

## 4) Ejecutar pruebas frontend

```bash
npm test -- --watch=false
```

## 5) Ejecutar smoke E2E reales (obligatorios por PR)

Levantar frontend y backend antes de ejecutar Cypress:

```bash
# backend (desde raiz)
docker compose up -d

# frontend (desde frontend/)
npm start
```

En otra terminal (desde `frontend/`):

```bash
npm run e2e -- --spec cypress/e2e/admin-mvp.cy.ts --browser electron --headless --config baseUrl=http://localhost:4200
```

El segundo smoke real (crear+listar Empleados) debe ejecutarse tambien en PR con su spec dedicada cuando este implementada.

## 6) Ejecutar regresión E2E con stubs

```bash
npm run e2e -- --browser electron --headless
```

Las specs de regresión no-smoke deben usar stubs para mantener estabilidad y velocidad.

## 7) Validar build de producción

```bash
npm run build
```

## Criterios de salida

- No hay regresión funcional en login ni CRUD existente.
- Cobertura mínima de pruebas para componentes reutilizables y contenedores clave.
- Smoke real de login+navegación y smoke real de crear+listar Empleados en verde para PR.
- App compila y ejecuta sin errores de TypeScript o plantilla.
