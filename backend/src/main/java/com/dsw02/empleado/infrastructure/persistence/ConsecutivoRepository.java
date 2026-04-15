package com.dsw02.empleado.infrastructure.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConsecutivoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ConsecutivoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long siguienteConsecutivo() {
        Long next = jdbcTemplate.queryForObject("SELECT nextval('empleado_consecutivo_seq')", Long.class);
        if (next == null) {
            throw new IllegalStateException("No fue posible obtener consecutivo para empleado");
        }
        return next;
    }

    public long siguienteConsecutivoDepartamento() {
        Long next = jdbcTemplate.queryForObject("SELECT nextval('departamento_consecutivo_seq')", Long.class);
        if (next == null) {
            throw new IllegalStateException("No fue posible obtener consecutivo para departamento");
        }
        return next;
    }
}
