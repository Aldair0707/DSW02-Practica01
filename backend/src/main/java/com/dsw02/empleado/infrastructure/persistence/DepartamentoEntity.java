package com.dsw02.empleado.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "departamento")
public class DepartamentoEntity {

    @EmbeddedId
    private DepartamentoId id;

    @Column(name = "clave", nullable = false, length = 8, unique = true)
    private String clave;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public DepartamentoEntity() {
    }

    public DepartamentoEntity(DepartamentoId id, String clave, String nombre) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
    }

    public DepartamentoId getId() {
        return id;
    }

    public String getClave() {
        return clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
