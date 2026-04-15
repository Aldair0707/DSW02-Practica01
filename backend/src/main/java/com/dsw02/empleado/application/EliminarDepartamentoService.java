package com.dsw02.empleado.application;

import com.dsw02.empleado.domain.exception.DepartamentoConEmpleadosException;
import com.dsw02.empleado.domain.exception.DepartamentoNotFoundException;
import com.dsw02.empleado.infrastructure.persistence.DepartamentoRepository;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class EliminarDepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;

    public EliminarDepartamentoService(
        DepartamentoRepository departamentoRepository,
        EmpleadoRepository empleadoRepository
    ) {
        this.departamentoRepository = departamentoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public void eliminar(String clave) {
        var entity = departamentoRepository.findByClave(clave)
            .orElseThrow(() -> new DepartamentoNotFoundException("Departamento no encontrado para clave: " + clave));

        if (empleadoRepository.countByDepartamentoClave(clave) > 0) {
            throw new DepartamentoConEmpleadosException("No se puede eliminar un departamento con empleados asociados");
        }

        departamentoRepository.delete(entity);
    }
}
