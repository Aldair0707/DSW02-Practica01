package com.dsw02.empleado.application;

import com.dsw02.empleado.domain.Departamento;
import com.dsw02.empleado.domain.exception.DepartamentoNotFoundException;
import com.dsw02.empleado.infrastructure.persistence.DepartamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class ObtenerDepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public ObtenerDepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public Departamento obtenerPorClave(String clave) {
        var entity = departamentoRepository.findByClave(clave)
            .orElseThrow(() -> new DepartamentoNotFoundException("Departamento no encontrado para clave: " + clave));
        return new Departamento(entity.getClave(), entity.getNombre());
    }
}
