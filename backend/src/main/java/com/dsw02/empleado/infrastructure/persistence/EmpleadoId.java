package com.dsw02.empleado.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmpleadoId implements Serializable {

    @Column(name = "prefijo", nullable = false, length = 4)
    private String prefijo;

    @Column(name = "consecutivo", nullable = false)
    private Long consecutivo;

    public EmpleadoId() {
    }

    public EmpleadoId(String prefijo, Long consecutivo) {
        this.prefijo = prefijo;
        this.consecutivo = consecutivo;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public Long getConsecutivo() {
        return consecutivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpleadoId that)) {
            return false;
        }
        return Objects.equals(prefijo, that.prefijo) && Objects.equals(consecutivo, that.consecutivo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefijo, consecutivo);
    }
}
