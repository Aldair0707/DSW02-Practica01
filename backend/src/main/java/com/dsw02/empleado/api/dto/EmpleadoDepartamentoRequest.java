package com.dsw02.empleado.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EmpleadoDepartamentoRequest {

    @NotBlank
    @Pattern(regexp = "^DEP-[0-9]{4}$")
    private String departamentoClave;

    public String getDepartamentoClave() {
        return departamentoClave;
    }
}
