package com.dsw02.empleado.api;

import com.dsw02.empleado.api.dto.ErrorResponse;
import com.dsw02.empleado.domain.ErrorCode;
import com.dsw02.empleado.domain.exception.BusinessException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException exception) {
        HttpStatus status = switch (exception.getErrorCode()) {
            case NO_ENCONTRADO -> HttpStatus.NOT_FOUND;
            case FORMATO_CLAVE_INVALIDO, CLAVE_NO_EDITABLE, VALIDACION -> HttpStatus.BAD_REQUEST;
            case CONFLICTO -> HttpStatus.CONFLICT;
            case AUTH_INVALIDA, CUENTA_INACTIVA -> HttpStatus.UNAUTHORIZED;
            case NO_AUTORIZADO -> HttpStatus.FORBIDDEN;
        };

        ErrorResponse response = new ErrorResponse(
            exception.getErrorCode().name(),
            exception.getMessage(),
            List.of()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult().getAllErrors().stream()
            .map(error -> {
                if (error instanceof FieldError fieldError) {
                    return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                }
                return error.getDefaultMessage();
            })
            .toList();

        ErrorResponse response = new ErrorResponse(
            ErrorCode.VALIDACION.name(),
            "Error de validación",
            details
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        ErrorResponse response = new ErrorResponse(
            ErrorCode.AUTH_INVALIDA.name(),
            exception.getMessage(),
            List.of()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
