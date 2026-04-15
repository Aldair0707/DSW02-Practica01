package com.dsw02.empleado.api.dto;

import com.dsw02.empleado.domain.Empleado;

public record EmpleadoResponse(
    String clave,
    String nombre,
    String direccion,
    String telefono,
    String departamentoClave
) {

    public static EmpleadoResponse fromDomain(Empleado empleado) {
        return new EmpleadoResponse(
            empleado.clave(),
            empleado.nombre(),
            empleado.direccion(),
            empleado.telefono(),
            empleado.departamentoClave()
        );
    }
}
