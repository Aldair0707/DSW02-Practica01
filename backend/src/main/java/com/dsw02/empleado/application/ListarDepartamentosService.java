package com.dsw02.empleado.application;

import com.dsw02.empleado.api.dto.DepartamentoPageResponse;
import com.dsw02.empleado.api.dto.DepartamentoReadOnlyPageResponse;
import com.dsw02.empleado.api.dto.DepartamentoReadOnlyResponse;
import com.dsw02.empleado.api.dto.DepartamentoResponse;
import com.dsw02.empleado.infrastructure.persistence.DepartamentoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ListarDepartamentosService {

    private final DepartamentoRepository departamentoRepository;

    public ListarDepartamentosService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public DepartamentoPageResponse listar(int page, int size) {
        var result = departamentoRepository.findAll(PageRequest.of(page, size));
        return new DepartamentoPageResponse(
            result.getContent().stream().map(entity -> new DepartamentoResponse(entity.getClave(), entity.getNombre())).toList(),
            result.getNumber(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages()
        );
    }

    public DepartamentoReadOnlyPageResponse listarReadOnly(int page, int size) {
        var result = departamentoRepository.findAll(PageRequest.of(page, size));
        return new DepartamentoReadOnlyPageResponse(
            result.getContent().stream()
                .map(entity -> new DepartamentoReadOnlyResponse(entity.getClave(), entity.getNombre()))
                .toList(),
            result.getNumber(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages()
        );
    }
}
