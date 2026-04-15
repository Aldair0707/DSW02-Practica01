package com.dsw02.empleado.domain;

public record Empleado(
    String clave,
    String nombre,
    String direccion,
    String telefono,
    String departamentoClave,
    String correo,
    boolean activo
) {
}
