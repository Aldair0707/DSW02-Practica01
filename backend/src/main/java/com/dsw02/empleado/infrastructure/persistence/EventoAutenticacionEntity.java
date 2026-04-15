package com.dsw02.empleado.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "evento_autenticacion")
public class EventoAutenticacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correo_intentado", length = 150)
    private String correoIntentado;

    @Column(name = "resultado", nullable = false, length = 20)
    private String resultado;

    @Column(name = "motivo", nullable = false, length = 80)
    private String motivo;

    @Column(name = "origen_solicitud", length = 120)
    private String origenSolicitud;

    @Column(name = "fecha_hora", nullable = false)
    private Instant fechaHora;

    public Long getId() {
        return id;
    }

    public String getCorreoIntentado() {
        return correoIntentado;
    }

    public void setCorreoIntentado(String correoIntentado) {
        this.correoIntentado = correoIntentado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getOrigenSolicitud() {
        return origenSolicitud;
    }

    public void setOrigenSolicitud(String origenSolicitud) {
        this.origenSolicitud = origenSolicitud;
    }

    public Instant getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Instant fechaHora) {
        this.fechaHora = fechaHora;
    }
}
