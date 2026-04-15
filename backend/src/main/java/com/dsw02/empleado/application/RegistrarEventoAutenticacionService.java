package com.dsw02.empleado.application;

import com.dsw02.empleado.infrastructure.persistence.EventoAutenticacionEntity;
import com.dsw02.empleado.infrastructure.persistence.EventoAutenticacionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RegistrarEventoAutenticacionService {

    private final EventoAutenticacionRepository eventoAutenticacionRepository;

    public RegistrarEventoAutenticacionService(EventoAutenticacionRepository eventoAutenticacionRepository) {
        this.eventoAutenticacionRepository = eventoAutenticacionRepository;
    }

    public void registrar(String correoIntentado, String resultado, String motivo, String origenSolicitud) {
        EventoAutenticacionEntity entity = new EventoAutenticacionEntity();
        entity.setCorreoIntentado(correoIntentado);
        entity.setResultado(resultado);
        entity.setMotivo(motivo);
        entity.setOrigenSolicitud(origenSolicitud);
        entity.setFechaHora(Instant.now());
        eventoAutenticacionRepository.save(entity);
    }
}
