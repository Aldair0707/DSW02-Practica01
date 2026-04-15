package com.dsw02.empleado.api.dto;

import com.dsw02.empleado.domain.Departamento;

public record DepartamentoResponse(
    String clave,
    String nombre
) {

    public static DepartamentoResponse fromDomain(Departamento departamento) {
        return new DepartamentoResponse(departamento.clave(), departamento.nombre());
    }
}
