package com.dsw02.empleado.api.dto;

public record EmpleadoReadOnlyResponse(
    String id,
    String nombre,
    String correo,
    String departamento
) {
}
