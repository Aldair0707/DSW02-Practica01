package com.dsw02.empleado.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartamentoUpdateRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String nombre;

    public String getNombre() {
        return nombre;
    }
}
