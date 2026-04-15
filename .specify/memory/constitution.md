<!--
Sync Impact Report
- Version change: 1.4.0 -> 1.5.0
- Modified principles:
	- II. Seguridad por Defecto (Basic Auth) -> II. Seguridad por Defecto (Basic Auth + Login de Empleado)
	- V. Contrato API Versionado, Paginación, Perfil de Acceso y Documentación Viva -> V. Contrato API Versionado, Paginación, Identidad de Actor, Alcance de Permisos y Documentación Viva
- Added sections: Ninguna (se amplía contenido en secciones existentes)
- Removed sections: Ninguna
- Templates requiring updates:
	- ✅ .specify/templates/plan-template.md
	- ✅ .specify/templates/spec-template.md
	- ✅ .specify/templates/tasks-template.md
	- ✅ .specify/templates/commands/*.md (no aplica: ruta no existe en este workspace)
- Runtime guidance updates:
	- ✅ .github/agents/copilot-instructions.md
	- ✅ frontend/README.md
- Deferred TODOs: Ninguno
-->

# DSW02-Practica01 Constitution

## Core Principles

### I. Stack Tecnológico Obligatorio
El backend se implementa exclusivamente con Spring Boot 3 y Java 17. No se permite introducir frameworks alternativos para el núcleo de API, seguridad o persistencia sin una enmienda explícita a esta constitución.

### II. Seguridad por Defecto (Basic Auth + Login de Empleado)
Todas las rutas de negocio deben estar protegidas mediante autenticación HTTP Basic usando Spring Security. Solo podrán existir endpoints públicos cuando estén justificados (por ejemplo, health checks) y documentados de forma explícita.
En entornos local/desarrollo, las credenciales por defecto DEBEN ser `admin` (usuario) y `admin123` (contraseña), con posibilidad de sobreescritura por variables de entorno.
El sistema DEBE soportar además inicio de sesión del actor empleado mediante correo y contraseña de empleado, con validación explícita de credenciales y sin degradar la protección de rutas administrativas.
El actor autenticado como empleado DEBE operar con permisos de solo lectura sobre recursos de empleados y departamentos. No se permiten operaciones de creación, actualización ni eliminación para ese actor.

### III. Persistencia en PostgreSQL
La base de datos oficial del proyecto es PostgreSQL. Toda funcionalidad persistente debe modelarse para PostgreSQL y ejecutarse mediante configuración parametrizable por variables de entorno.

### IV. Entorno Reproducible con Docker
El proyecto debe poder levantarse en local y en integración mediante Docker y Docker Compose, incluyendo al menos el servicio de aplicación y PostgreSQL. Ningún flujo crítico debe depender de instalaciones manuales fuera de contenedores.

### V. Contrato API Versionado, Paginación, Identidad de Actor, Alcance de Permisos y Documentación Viva
Toda API REST DEBE estar versionada en ruta (`/api/v{major}/...`) y reflejar esa versión en OpenAPI.
Todo endpoint de listado DEBE soportar paginación y DEBE usar por defecto `size=5` cuando no se especifique.
Los endpoints de introspección de sesión/autenticación (por ejemplo, `/auth/me`) DEBEN distinguir explícitamente actor administrativo y actor empleado en el contrato, evitando representar al admin como empleado.
Los contratos de autenticación DEBEN contemplar de forma explícita el flujo de login de empleado por correo y contraseña, incluyendo ejemplos de request/response y códigos de error esperados.
Los contratos y reglas de autorización DEBEN establecer explícitamente que el actor empleado autenticado solo puede consultar listados de empleados y departamentos; cualquier intento de CRUD DEBE rechazarse con código de autorización apropiado (por ejemplo, 403).
No se aceptan endpoints nuevos o cambios de contrato sin actualización de Swagger/OpenAPI y ejemplos de uso.

## Restricciones Técnicas y de Arquitectura

- Java: versión 17 (LTS), con compilación y ejecución alineadas a esta versión.
- Framework base: Spring Boot 3.x.
- Seguridad: Spring Security con HTTP Basic Auth.
- Credenciales por defecto (local/dev): `APP_BASIC_USER=admin` y `APP_BASIC_PASSWORD=admin123`.
- Base de datos: PostgreSQL, con configuración por variables (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`).
- Contenerización: `Dockerfile` para la aplicación y `docker-compose.yml` para la orquestación local.
- API: Swagger/OpenAPI habilitado en entorno de desarrollo y accesible para validación funcional.
- Versionado API: prefijo obligatorio por versión mayor en rutas públicas (`/api/v1/...`).
- Paginación: listados públicos con parámetros de paginación (`page`, `size`) y `size=5` por defecto.
- Perfil autenticado: los contratos de perfil de sesión DEBEN declarar tipo de actor (`ADMIN`/`EMPLEADO` o equivalente) para decisiones de autorización en frontend y backend.
- Login de empleado: el contrato de autenticación DEBE permitir credenciales de empleado por correo+contraseña y documentar claramente su alcance de permisos.
- Autorización por actor: el actor `EMPLEADO` solo puede ejecutar operaciones de lectura sobre listados de empleados y departamentos; operaciones de escritura (create/update/delete) están prohibidas.
- Configuración: no hardcodear secretos; usar variables de entorno y/o archivos `.env` fuera de control de versiones.
- En no-producción se permite valor por defecto de Basic Auth (`admin`/`admin123`) únicamente como baseline de arranque; en producción DEBE sobreescribirse.

## Flujo de Desarrollo y Quality Gates

- Cada nueva historia backend debe incluir:
	- Endpoint(s) implementados.
	- Endpoint(s) versionados (`/api/v{major}/...`).
	- Paginación en listados con valor por defecto `size=5`.
	- Contrato de perfil autenticado actualizado cuando cambien reglas de acceso admin/empleado.
	- Contrato de login de empleado por correo+contraseña actualizado cuando cambien reglas de autenticación de empleado.
	- Contrato y validación de autorización para asegurar que el actor empleado vea listados de empleados/departamentos en modo solo lectura y sin CRUD.
	- Seguridad aplicada con Basic Auth.
	- Credenciales default de desarrollo configuradas (`admin`/`admin123`) o justificadas si difieren.
	- Persistencia en PostgreSQL (si corresponde).
	- Documentación Swagger actualizada.
- Antes de integrar cambios, se debe verificar:
	- Compilación correcta con Java 17.
	- Arranque del stack con Docker Compose.
	- Pruebas unitarias e integración relevantes en verde.
	- Validación manual mínima de endpoints en Swagger UI.
	- Validación explícita de contrato de `/auth/me` para distinguir actor admin y empleado cuando aplique.
	- Validación explícita del flujo de login de empleado por correo+contraseña cuando aplique.
	- Validación explícita de permisos de actor empleado: listados permitidos y CRUD denegado en empleados/departamentos.
	- Validación de acceso con credenciales Basic Auth por defecto en entorno local.
	- Validación explícita de versionado de ruta y comportamiento de paginación por defecto.
- Cualquier desviación del stack definido debe registrarse como excepción técnica con motivo, alcance y plan de reversión.

## Governance

Esta constitución prevalece sobre decisiones ad-hoc de implementación. Toda revisión de código debe validar cumplimiento explícito de estos principios.

Las enmiendas requieren:
- Justificación técnica por escrito.
- Evaluación del impacto en seguridad, despliegue y documentación.
- Plan de migración cuando haya cambios de stack, autenticación o base de datos.

Política de versionado de esta constitución:
- **MAJOR**: redefinición o eliminación de principios/gobernanza con impacto incompatible.
- **MINOR**: adición o expansión material de principios o reglas obligatorias.
- **PATCH**: aclaraciones editoriales sin cambio normativo.

Expectativa de cumplimiento:
- Toda PR DEBE declarar cumplimiento constitucional en el `plan.md` y en revisión técnica.
- Cualquier excepción DEBE registrarse con alcance temporal y plan de remediación.

**Version**: 1.5.0 | **Ratified**: 2026-02-25 | **Last Amended**: 2026-03-26
