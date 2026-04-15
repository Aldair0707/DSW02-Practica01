package com.dsw02.empleado.domain.exception;

import com.dsw02.empleado.domain.ErrorCode;

public class DepartamentoConEmpleadosException extends BusinessException {

    public DepartamentoConEmpleadosException(String message) {
        super(ErrorCode.CONFLICTO, message);
    }
}
