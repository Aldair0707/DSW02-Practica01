package com.dsw02.empleado.infrastructure.security;

import com.dsw02.empleado.application.RegistrarEventoAutenticacionService;
import com.dsw02.empleado.domain.CorreoNormalizer;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthenticationAuditListener {

    private final RegistrarEventoAutenticacionService registrarEventoAutenticacionService;
    private final CorreoNormalizer correoNormalizer;
    private final EmpleadoRepository empleadoRepository;

    public AuthenticationAuditListener(
        RegistrarEventoAutenticacionService registrarEventoAutenticacionService,
        CorreoNormalizer correoNormalizer,
        EmpleadoRepository empleadoRepository
    ) {
        this.registrarEventoAutenticacionService = registrarEventoAutenticacionService;
        this.correoNormalizer = correoNormalizer;
        this.empleadoRepository = empleadoRepository;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        registrarEventoAutenticacionService.registrar(
            correoNormalizer.normalize(event.getAuthentication().getName()),
            "EXITO",
            "AUTENTICACION_OK",
            origenSolicitud()
        );
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        String correo = null;
        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            correo = correoNormalizer.normalize(event.getAuthentication().getName());
        }

        String motivo = "CREDENCIALES_INVALIDAS";
        if (event.getException() instanceof DisabledException) {
            motivo = "CUENTA_INACTIVA";
        } else if (correo != null) {
            boolean cuentaInactiva = empleadoRepository.findByCorreoIgnoreCase(correo)
                .map(entity -> !entity.isActivo())
                .orElse(false);
            if (cuentaInactiva) {
                motivo = "CUENTA_INACTIVA";
            }
        }

        registrarEventoAutenticacionService.registrar(
            correo,
            "FALLO",
            motivo,
            origenSolicitud()
        );
    }

    private String origenSolicitud() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs) {
            return attrs.getRequest().getRemoteAddr();
        }
        return "UNKNOWN";
    }
}
