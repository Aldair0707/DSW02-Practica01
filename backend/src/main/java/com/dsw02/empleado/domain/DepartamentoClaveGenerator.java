package com.dsw02.empleado.domain;

import com.dsw02.empleado.infrastructure.persistence.ConsecutivoRepository;
import org.springframework.stereotype.Component;

@Component
public class DepartamentoClaveGenerator {

    private final ConsecutivoRepository consecutivoRepository;
    private final DepartamentoClaveParser departamentoClaveParser;

    public DepartamentoClaveGenerator(
        ConsecutivoRepository consecutivoRepository,
        DepartamentoClaveParser departamentoClaveParser
    ) {
        this.consecutivoRepository = consecutivoRepository;
        this.departamentoClaveParser = departamentoClaveParser;
    }

    public String generar() {
        long consecutivo = consecutivoRepository.siguienteConsecutivoDepartamento();
        return departamentoClaveParser.buildClave(consecutivo);
    }
}
