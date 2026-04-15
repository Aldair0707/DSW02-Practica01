package com.dsw02.empleado.domain;

import com.dsw02.empleado.domain.exception.InvalidClaveFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ClaveParser {

    public static final String PREFIJO = "EMP-";
    private static final Pattern CLAVE_PATTERN = Pattern.compile("^EMP-([0-9]+)$");

    public long parseConsecutivo(String clave) {
        if (clave == null || clave.isBlank()) {
            throw new InvalidClaveFormatException("La clave es obligatoria y debe cumplir formato EMP-<autonumérico>");
        }

        Matcher matcher = CLAVE_PATTERN.matcher(clave);
        if (!matcher.matches()) {
            throw new InvalidClaveFormatException("La clave debe cumplir formato EMP-<autonumérico>");
        }

        return Long.parseLong(matcher.group(1));
    }

    public String buildClave(long consecutivo) {
        return PREFIJO + consecutivo;
    }
}
