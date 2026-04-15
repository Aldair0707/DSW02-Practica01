package com.dsw02.empleado.application;

import com.dsw02.empleado.api.dto.DepartamentoCreateRequest;
import com.dsw02.empleado.domain.Departamento;
import com.dsw02.empleado.domain.DepartamentoClaveGenerator;
import com.dsw02.empleado.domain.DepartamentoClaveParser;
import com.dsw02.empleado.infrastructure.persistence.DepartamentoEntity;
import com.dsw02.empleado.infrastructure.persistence.DepartamentoId;
import com.dsw02.empleado.infrastructure.persistence.DepartamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class CrearDepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final DepartamentoClaveGenerator departamentoClaveGenerator;
    private final DepartamentoClaveParser departamentoClaveParser;

    public CrearDepartamentoService(
        DepartamentoRepository departamentoRepository,
        DepartamentoClaveGenerator departamentoClaveGenerator,
        DepartamentoClaveParser departamentoClaveParser
    ) {
        this.departamentoRepository = departamentoRepository;
        this.departamentoClaveGenerator = departamentoClaveGenerator;
        this.departamentoClaveParser = departamentoClaveParser;
    }

    public Departamento crear(DepartamentoCreateRequest request) {
        String clave = departamentoClaveGenerator.generar();
        long consecutivo = departamentoClaveParser.parseConsecutivo(clave);
        DepartamentoEntity saved = departamentoRepository.save(
            new DepartamentoEntity(
                new DepartamentoId(DepartamentoClaveParser.PREFIJO, consecutivo),
                clave,
                request.getNombre()
            )
        );
        return new Departamento(saved.getClave(), saved.getNombre());
    }
}
