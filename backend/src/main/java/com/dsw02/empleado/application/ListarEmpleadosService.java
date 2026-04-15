package com.dsw02.empleado.application;

import com.dsw02.empleado.api.dto.EmpleadoPageResponse;
import com.dsw02.empleado.api.dto.EmpleadoReadOnlyPageResponse;
import com.dsw02.empleado.api.dto.EmpleadoReadOnlyResponse;
import com.dsw02.empleado.api.dto.EmpleadoResponse;
import com.dsw02.empleado.domain.ClaveParser;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoEntity;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ListarEmpleadosService {

    private final EmpleadoRepository empleadoRepository;
    private final ClaveParser claveParser;

    public ListarEmpleadosService(EmpleadoRepository empleadoRepository, ClaveParser claveParser) {
        this.empleadoRepository = empleadoRepository;
        this.claveParser = claveParser;
    }

    public EmpleadoPageResponse listar(int page, int size) {
        Page<EmpleadoEntity> result = empleadoRepository.findAll(PageRequest.of(page, size));
        return new EmpleadoPageResponse(
            result.getContent().stream().map(this::toResponse).toList(),
            result.getNumber(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages()
        );
    }

    public EmpleadoReadOnlyPageResponse listarReadOnly(int page, int size) {
        Page<EmpleadoEntity> result = empleadoRepository.findAll(PageRequest.of(page, size));
        return new EmpleadoReadOnlyPageResponse(
            result.getContent().stream().map(this::toReadOnlyResponse).toList(),
            result.getNumber(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages()
        );
    }

    private EmpleadoResponse toResponse(EmpleadoEntity entity) {
        return new EmpleadoResponse(
            claveParser.buildClave(entity.getId().getConsecutivo()),
            entity.getNombre(),
            entity.getDireccion(),
            entity.getTelefono(),
            entity.getDepartamentoClave()
        );
    }

    private EmpleadoReadOnlyResponse toReadOnlyResponse(EmpleadoEntity entity) {
        return new EmpleadoReadOnlyResponse(
            claveParser.buildClave(entity.getId().getConsecutivo()),
            entity.getNombre(),
            entity.getCorreo(),
            entity.getDepartamentoClave()
        );
    }

    public EmpleadoPageResponse listarConDefaultSize(int page) {
        return listar(page, 5);
    }

    public EmpleadoPageResponse listarDefault() {
        return listar(0, 5);
    }
}
