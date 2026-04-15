package com.dsw02.empleado.application;

import com.dsw02.empleado.api.dto.EmpleadoEstadoRequest;
import com.dsw02.empleado.api.dto.EmpleadoUpdateRequest;
import com.dsw02.empleado.domain.ClaveParser;
import com.dsw02.empleado.domain.Empleado;
import com.dsw02.empleado.domain.ErrorCode;
import com.dsw02.empleado.domain.exception.BusinessException;
import com.dsw02.empleado.domain.exception.ClaveNoEditableException;
import com.dsw02.empleado.domain.exception.NotFoundException;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoEntity;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoId;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarEmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final ClaveParser claveParser;

    public ActualizarEmpleadoService(EmpleadoRepository empleadoRepository, ClaveParser claveParser) {
        this.empleadoRepository = empleadoRepository;
        this.claveParser = claveParser;
    }

    public Empleado actualizar(String clave, EmpleadoUpdateRequest request) {
        if (request.getClave() != null && !request.getClave().isBlank()) {
            throw new ClaveNoEditableException("La clave no puede modificarse");
        }
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            throw new BusinessException(ErrorCode.VALIDACION, "El cambio de contrasena esta fuera de alcance en este feature");
        }

        long consecutivo = claveParser.parseConsecutivo(clave);
        EmpleadoId id = new EmpleadoId(ClaveParser.PREFIJO, consecutivo);

        EmpleadoEntity entity = empleadoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Empleado no encontrado para clave: " + clave));

        entity.setNombre(request.getNombre());
        entity.setDireccion(request.getDireccion());
        entity.setTelefono(request.getTelefono());

        EmpleadoEntity saved = empleadoRepository.save(entity);

        return new Empleado(
            claveParser.buildClave(saved.getId().getConsecutivo()),
            saved.getNombre(),
            saved.getDireccion(),
            saved.getTelefono(),
            saved.getDepartamentoClave(),
            saved.getCorreo(),
            saved.isActivo()
        );
    }

    public Empleado actualizarEstado(String clave, EmpleadoEstadoRequest request) {
        long consecutivo = claveParser.parseConsecutivo(clave);
        EmpleadoId id = new EmpleadoId(ClaveParser.PREFIJO, consecutivo);

        EmpleadoEntity entity = empleadoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Empleado no encontrado para clave: " + clave));

        entity.setActivo(request.getActivo());
        EmpleadoEntity saved = empleadoRepository.save(entity);

        return new Empleado(
            claveParser.buildClave(saved.getId().getConsecutivo()),
            saved.getNombre(),
            saved.getDireccion(),
            saved.getTelefono(),
            saved.getDepartamentoClave(),
            saved.getCorreo(),
            saved.isActivo()
        );
    }
}
