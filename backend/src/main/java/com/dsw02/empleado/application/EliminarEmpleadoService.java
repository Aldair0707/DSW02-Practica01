package com.dsw02.empleado.application;

import com.dsw02.empleado.domain.ClaveParser;
import com.dsw02.empleado.domain.exception.NotFoundException;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoId;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class EliminarEmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final ClaveParser claveParser;

    public EliminarEmpleadoService(EmpleadoRepository empleadoRepository, ClaveParser claveParser) {
        this.empleadoRepository = empleadoRepository;
        this.claveParser = claveParser;
    }

    public void eliminar(String clave) {
        long consecutivo = claveParser.parseConsecutivo(clave);
        EmpleadoId id = new EmpleadoId(ClaveParser.PREFIJO, consecutivo);

        if (!empleadoRepository.existsById(id)) {
            throw new NotFoundException("Empleado no encontrado para clave: " + clave);
        }

        empleadoRepository.deleteById(id);
    }
}
