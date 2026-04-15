package com.dsw02.empleado.domain.exception;

import com.dsw02.empleado.domain.ErrorCode;

public class ClaveNoEditableException extends BusinessException {

    public ClaveNoEditableException(String message) {
        super(ErrorCode.CLAVE_NO_EDITABLE, message);
    }
}
