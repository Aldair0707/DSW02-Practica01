package com.dsw02.empleado.domain;

import org.springframework.stereotype.Component;

@Component
public class CorreoNormalizer {

    public String normalize(String correo) {
        if (correo == null) {
            return null;
        }
        return correo.trim().toLowerCase();
    }
}
