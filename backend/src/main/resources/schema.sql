CREATE SEQUENCE IF NOT EXISTS empleado_consecutivo_seq START WITH 1001 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS departamento_consecutivo_seq START WITH 1001 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS departamento (
	prefijo VARCHAR(4) NOT NULL,
	consecutivo BIGINT NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	PRIMARY KEY (prefijo, consecutivo)
);

ALTER TABLE IF EXISTS departamento
	ADD COLUMN IF NOT EXISTS clave VARCHAR(8);

UPDATE departamento
SET clave = prefijo || LPAD(consecutivo::text, 4, '0')
WHERE clave IS NULL;

ALTER TABLE IF EXISTS departamento
	ALTER COLUMN clave SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS ux_departamento_clave
	ON departamento (clave);

CREATE INDEX IF NOT EXISTS ix_departamento_nombre
	ON departamento (nombre);

INSERT INTO departamento (prefijo, consecutivo, clave, nombre)
VALUES ('DEP-', 0, 'DEP-0000', 'Sin asignar')
ON CONFLICT (prefijo, consecutivo) DO NOTHING;

CREATE TABLE IF NOT EXISTS empleado (
	prefijo VARCHAR(4) NOT NULL,
	consecutivo BIGINT NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	direccion VARCHAR(100) NOT NULL,
	telefono VARCHAR(100) NOT NULL,
	PRIMARY KEY (prefijo, consecutivo)
);

ALTER TABLE IF EXISTS empleado
	ADD COLUMN IF NOT EXISTS correo VARCHAR(150),
	ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255),
	ADD COLUMN IF NOT EXISTS activo BOOLEAN,
	ADD COLUMN IF NOT EXISTS departamento_clave VARCHAR(8);

UPDATE empleado
SET correo = CONCAT('legacy+', consecutivo, '@invalid.local')
WHERE correo IS NULL;

UPDATE empleado
SET password_hash = '$2a$10$Qu8qVQjQ5bkA90xj4x4m4uA1b8upfKQ7QSh6h6vV8Eap8rjLjjmIu'
WHERE password_hash IS NULL;

UPDATE empleado
SET activo = TRUE
WHERE activo IS NULL;

UPDATE empleado
SET departamento_clave = 'DEP-0000'
WHERE departamento_clave IS NULL;

ALTER TABLE IF EXISTS empleado
	ALTER COLUMN correo SET NOT NULL,
	ALTER COLUMN password_hash SET NOT NULL,
	ALTER COLUMN activo SET NOT NULL,
	ALTER COLUMN departamento_clave SET NOT NULL;

ALTER TABLE IF EXISTS empleado
	DROP CONSTRAINT IF EXISTS fk_empleado_departamento_clave;

ALTER TABLE IF EXISTS empleado
	ADD CONSTRAINT fk_empleado_departamento_clave
	FOREIGN KEY (departamento_clave)
	REFERENCES departamento(clave);

CREATE INDEX IF NOT EXISTS ix_empleado_departamento_clave
	ON empleado (departamento_clave);

CREATE UNIQUE INDEX IF NOT EXISTS ux_empleado_correo_lower
	ON empleado (LOWER(correo));

CREATE TABLE IF NOT EXISTS evento_autenticacion (
	id BIGSERIAL PRIMARY KEY,
	correo_intentado VARCHAR(150),
	resultado VARCHAR(20) NOT NULL,
	motivo VARCHAR(80) NOT NULL,
	origen_solicitud VARCHAR(120),
	fecha_hora TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS ix_evento_auth_fecha_hora
	ON evento_autenticacion (fecha_hora);

CREATE INDEX IF NOT EXISTS ix_evento_auth_correo_fecha
	ON evento_autenticacion (correo_intentado, fecha_hora);
