package com.dsw02.empleado.application;

import com.dsw02.empleado.api.dto.DepartamentoUpdateRequest;
import com.dsw02.empleado.domain.Departamento;
import com.dsw02.empleado.domain.exception.DepartamentoNotFoundException;
import com.dsw02.empleado.infrastructure.persistence.DepartamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarDepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public ActualizarDepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public Departamento actualizar(String clave, DepartamentoUpdateRequest request) {
        var entity = departamentoRepository.findByClave(clave)
            .orElseThrow(() -> new DepartamentoNotFoundException("Departamento no encontrado para clave: " + clave));

        entity.setNombre(request.getNombre());
        var saved = departamentoRepository.save(entity);

        return new Departamento(saved.getClave(), saved.getNombre());
    }
}
