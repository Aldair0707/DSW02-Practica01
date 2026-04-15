package com.dsw02.empleado.api.dto;

import com.dsw02.empleado.domain.Empleado;

import java.util.List;

public record EmpleadoAuthProfileResponse(
    String actorType,
    String username,
    String displayName,
    List<String> permissions,
    EmpleadoResumen empleado
) {

    public record EmpleadoResumen(
        String clave,
        String correo,
        String nombre,
        String direccion,
        String telefono,
        String departamentoClave,
        boolean activo
    ) {
    }

    public static EmpleadoAuthProfileResponse admin(String username) {
        return new EmpleadoAuthProfileResponse(
            "ADMIN",
            username,
            "Administrador",
            List.of("CRUD_EMPLEADOS", "CRUD_DEPARTAMENTOS"),
            null
        );
    }

    public static EmpleadoAuthProfileResponse empleado(Empleado empleado) {
        return new EmpleadoAuthProfileResponse(
            "EMPLEADO",
            empleado.correo(),
            empleado.nombre(),
            List.of("SELF"),
            new EmpleadoResumen(
                empleado.clave(),
                empleado.correo(),
                empleado.nombre(),
                empleado.direccion(),
                empleado.telefono(),
                empleado.departamentoClave(),
                empleado.activo()
            )
        );
    }
}
