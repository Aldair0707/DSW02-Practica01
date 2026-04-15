# Phase 0 Research - CRUD de empleado

## 1) Modelado de `clave` como PK compuesta e inmutable

- Decision: Modelar identidad con PK compuesta (`prefijo`, `consecutivo`) en persistencia, donde `prefijo` es fijo `EMP-` y `consecutivo` es autonumérico; exponer `clave` al cliente como string `EMP-<consecutivo>`.
- Rationale: Cumple el nuevo contrato funcional, mantiene unicidad robusta en base de datos y simplifica búsqueda por clave externa.
- Alternatives considered: 
  - Mantener PK simple string `clave`: rechazado por menor control sobre secuencia autonumérica.
  - Mantener clave numérica capturada por usuario: rechazado por no cumplir el requerimiento de generación automática.

## 2) Restricción de longitud para `nombre`, `dirección`, `telefono`

- Decision: Aplicar validaciones Bean Validation (`@Size(max = 100)`) en DTOs de entrada para create/update y reflejarlas también en esquema SQL (`VARCHAR(100)`).
- Rationale: Defensa en profundidad (API + DB) y mensajes de error predecibles desde la API.
- Alternatives considered:
  - Validar solo en base de datos: rechazado por mala experiencia de error y menor trazabilidad.
  - Validar solo en API: rechazado por riesgo ante inserciones fuera de API.

## 3) Manejo de errores de negocio y contrato de respuesta

- Decision: Usar un formato uniforme de error JSON con campos `code`, `message`, `details`, mapeando casos: `VALIDACION`, `NO_ENCONTRADO`, `FORMATO_CLAVE_INVALIDO`, `CLAVE_NO_EDITABLE`.
- Rationale: Alinea FR-010 y permite a clientes tratar errores de forma consistente.
- Alternatives considered:
  - Mensajes libres por excepción: rechazado por inconsistencia.
  - RFC 7807 puro sin códigos de negocio: rechazado para mantener códigos semánticos solicitados en la feature.

## 4) Estrategia de seguridad (Constitución II)

- Decision: Proteger endpoints `/api/v1/empleados/**` con HTTP Basic; permitir público solo `/actuator/health` y documentación OpenAPI en entorno de desarrollo.
- Rationale: Cumple la constitución y preserva usabilidad de validación funcional por Swagger.
- Alternatives considered:
  - JWT/OAuth2: rechazado por complejidad fuera de alcance MVP.
  - Todo público: rechazado por violación constitucional.

## 5) Persistencia en PostgreSQL (Constitución III)

- Decision: Persistir con Spring Data JPA sobre PostgreSQL usando variable de entorno (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`).
- Rationale: Cumple constitución y simplifica despliegue por entorno.
- Alternatives considered:
  - H2 en runtime principal: rechazado por incumplimiento de base oficial.
  - SQL manual sin ORM: rechazado por mayor fricción para MVP CRUD.

## 6) Pruebas recomendadas para esta feature

- Decision: Combinar pruebas unitarias (validaciones y servicio), integración (repositorio + PostgreSQL con Testcontainers) y contract tests de endpoints con MockMvc.
- Rationale: Cobertura de reglas de dominio + comportamiento HTTP + persistencia real.
- Alternatives considered:
  - Solo unitarias: rechazado por riesgo en mapeo JPA/SQL.
  - Solo integración end-to-end: rechazado por feedback más lento.

## 7) Contenerización y ejecución reproducible (Constitución IV)

- Decision: Definir `Dockerfile` para backend y `docker-compose.yml` con servicios `app` y `postgres`, variables compartidas y red interna.
- Rationale: Entorno reproducible local/integración sin pasos manuales críticos.
- Alternatives considered:
  - Ejecutar DB fuera de contenedores: rechazado por incumplimiento constitucional.
  - Script local sin compose: rechazado por menor estandarización.

## Resolución de NEEDS CLARIFICATION

No quedan `NEEDS CLARIFICATION` abiertos para esta feature. Las decisiones anteriores cubren validación, persistencia, seguridad, contrato y pruebas.