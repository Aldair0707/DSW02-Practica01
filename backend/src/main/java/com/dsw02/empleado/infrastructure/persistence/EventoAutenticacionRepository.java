package com.dsw02.empleado.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoAutenticacionRepository extends JpaRepository<EventoAutenticacionEntity, Long> {
}
