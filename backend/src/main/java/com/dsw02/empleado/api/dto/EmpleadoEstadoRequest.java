package com.dsw02.empleado.api.dto;

import jakarta.validation.constraints.NotNull;

public class EmpleadoEstadoRequest {

    @NotNull
    private Boolean activo;

    public Boolean getActivo() {
        return activo;
    }
}
