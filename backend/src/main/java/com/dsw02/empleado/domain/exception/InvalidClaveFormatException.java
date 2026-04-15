package com.dsw02.empleado.domain.exception;

import com.dsw02.empleado.domain.ErrorCode;

public class InvalidClaveFormatException extends BusinessException {

    public InvalidClaveFormatException(String message) {
        super(ErrorCode.FORMATO_CLAVE_INVALIDO, message);
    }
}
