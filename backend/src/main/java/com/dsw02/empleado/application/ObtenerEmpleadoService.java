package com.dsw02.empleado.application;

import com.dsw02.empleado.domain.ClaveParser;
import com.dsw02.empleado.domain.Empleado;
import com.dsw02.empleado.domain.exception.NotFoundException;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoEntity;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoId;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class ObtenerEmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final ClaveParser claveParser;

    public ObtenerEmpleadoService(EmpleadoRepository empleadoRepository, ClaveParser claveParser) {
        this.empleadoRepository = empleadoRepository;
        this.claveParser = claveParser;
    }

    public Empleado obtenerPorClave(String clave) {
        long consecutivo = claveParser.parseConsecutivo(clave);
        EmpleadoId id = new EmpleadoId(ClaveParser.PREFIJO, consecutivo);

        EmpleadoEntity entity = empleadoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Empleado no encontrado para clave: " + clave));

        return new Empleado(
            claveParser.buildClave(entity.getId().getConsecutivo()),
            entity.getNombre(),
            entity.getDireccion(),
            entity.getTelefono(),
            entity.getDepartamentoClave(),
            entity.getCorreo(),
            entity.isActivo()
        );
    }
}
