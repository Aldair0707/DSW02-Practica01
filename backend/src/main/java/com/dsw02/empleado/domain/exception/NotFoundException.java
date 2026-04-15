package com.dsw02.empleado.domain.exception;

import com.dsw02.empleado.domain.ErrorCode;

public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(ErrorCode.NO_ENCONTRADO, message);
    }
}
