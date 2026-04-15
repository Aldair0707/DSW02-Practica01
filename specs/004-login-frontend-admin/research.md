# Phase 0 Research - Login Frontend con Admin

## 1) Version Angular objetivo en este entorno

- Decision: Implementar el feature sobre Angular 21.2.x porque Angular 22 no esta publicado en npm al momento.
- Rationale: Permite entregar login funcional ahora, manteniendo compatibilidad con el ecosistema actual del proyecto.
- Alternatives considered:
  - Bloquear desarrollo hasta disponibilidad de Angular 22: rechazado por retrasar entrega de valor.
  - Downgrade a versiones anteriores: rechazado por no aportar ventajas funcionales.

## 2) Mecanismo de autenticacion en frontend

- Decision: Usar HTTP Basic Auth construyendo `Authorization: Basic <base64(username:password)>` desde el formulario de login.
- Rationale: Es el mecanismo exigido por backend Spring Security para este proyecto.
- Alternatives considered:
  - JWT/OAuth: rechazado porque no existe soporte backend para esta feature y cambiaria alcance.

## 3) Estrategia de sesion en UI

- Decision: Mantener sesion autenticada en memoria de componente/servicio durante la vida de la SPA, sin persistir credenciales en localStorage/sessionStorage.
- Rationale: Reduce riesgo de exposicion de credenciales y cumple alcance minimo para entrar al sistema en local.
- Alternatives considered:
  - Persistir credenciales en almacenamiento web: rechazado por riesgo de seguridad y no requerido para MVP.

## 4) Endpoint de validacion de login

- Decision: Validar login llamando `GET /api/v1/empleados/auth/me`.
- Rationale: Endpoint existente que confirma credenciales y retorna perfil autenticado.
- Alternatives considered:
  - Usar otro endpoint de negocio como validacion indirecta: rechazado por menor claridad semantica.

## 5) Manejo de errores de autenticacion y conectividad

- Decision: Normalizar errores `401/403` como "credenciales invalidas/no autorizado" y errores de red como "backend no disponible".
- Rationale: Mejora UX y evita estados ambiguos para el usuario.
- Alternatives considered:
  - Mensaje unico para todo error: rechazado por baja capacidad de diagnostico.

## 6) Integracion con vistas protegidas existentes

- Decision: Cargar empleados y departamentos solo despues de login exitoso y limpiar datos en logout.
- Rationale: Mantiene consistencia de estado y evita consultas no autenticadas.
- Alternatives considered:
  - Intentar cargar datos antes de autenticar: rechazado por generar errores innecesarios.

## 7) Gobernanza API y constitucion

- Decision: No agregar ni modificar endpoints backend; consumir solamente rutas versionadas `/api/v1/...` con comportamiento paginado existente.
- Rationale: Cumple constitucion vigente y minimiza impacto cruzado.
- Alternatives considered:
  - Crear endpoint nuevo de login: rechazado por fuera de alcance y sin necesidad tecnica.

## Resolucion de NEEDS CLARIFICATION

No quedan NEEDS CLARIFICATION para esta feature.
