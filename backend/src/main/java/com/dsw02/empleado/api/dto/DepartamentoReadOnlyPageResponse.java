package com.dsw02.empleado.api.dto;

import java.util.List;

public record DepartamentoReadOnlyPageResponse(
    List<DepartamentoReadOnlyResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
}
