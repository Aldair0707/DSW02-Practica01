package com.dsw02.empleado.application;

import com.dsw02.empleado.api.dto.EmpleadoPageResponse;
import com.dsw02.empleado.api.dto.EmpleadoResponse;
import com.dsw02.empleado.infrastructure.persistence.DepartamentoRepository;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ListarEmpleadosPorDepartamentoService {

    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoRepository departamentoRepository;

    public ListarEmpleadosPorDepartamentoService(
        EmpleadoRepository empleadoRepository,
        DepartamentoRepository departamentoRepository
    ) {
        this.empleadoRepository = empleadoRepository;
        this.departamentoRepository = departamentoRepository;
    }

    public EmpleadoPageResponse listar(String departamentoClave, Integer page, Integer size) {
        if (!departamentoRepository.existsByClave(departamentoClave)) {
            throw new com.dsw02.empleado.domain.exception.DepartamentoNotFoundException(
                "Departamento no encontrado para clave: " + departamentoClave
            );
        }

        int pageNumber = page == null || page < 0 ? 0 : page;
        int pageSize = size == null || size <= 0 ? 5 : size;

        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id.consecutivo").ascending());
        var result = empleadoRepository.findByDepartamentoClave(departamentoClave, pageable);

        List<EmpleadoResponse> content = result.getContent().stream()
            .map(emp -> new EmpleadoResponse(
                String.format("EMP-%04d", emp.getId().getConsecutivo()),
                emp.getNombre(),
                emp.getDireccion(),
                emp.getTelefono(),
                emp.getDepartamentoClave()
            ))
            .toList();

        return new EmpleadoPageResponse(
            content,
            result.getNumber(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages()
        );
    }
}
