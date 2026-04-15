package com.dsw02.empleado.domain;

import com.dsw02.empleado.domain.exception.InvalidClaveFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class DepartamentoClaveParser {

    public static final String PREFIJO = "DEP-";
    private static final Pattern CLAVE_PATTERN = Pattern.compile("^DEP-([0-9]{4})$");

    public long parseConsecutivo(String clave) {
        if (clave == null || clave.isBlank()) {
            throw new InvalidClaveFormatException("La clave de departamento es obligatoria y debe cumplir formato DEP-XXXX");
        }

        Matcher matcher = CLAVE_PATTERN.matcher(clave);
        if (!matcher.matches()) {
            throw new InvalidClaveFormatException("La clave de departamento debe cumplir formato DEP-XXXX");
        }

        return Long.parseLong(matcher.group(1));
    }

    public String buildClave(long consecutivo) {
        if (consecutivo < 0 || consecutivo > 9999) {
            throw new InvalidClaveFormatException("El consecutivo de departamento debe estar entre 0000 y 9999");
        }
        return PREFIJO + String.format("%04d", consecutivo);
    }
}
