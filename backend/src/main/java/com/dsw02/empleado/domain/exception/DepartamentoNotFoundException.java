package com.dsw02.empleado.domain.exception;

import com.dsw02.empleado.domain.ErrorCode;

public class DepartamentoNotFoundException extends BusinessException {

    public DepartamentoNotFoundException(String message) {
        super(ErrorCode.NO_ENCONTRADO, message);
    }
}
