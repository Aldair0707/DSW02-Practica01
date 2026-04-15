package com.dsw02.empleado.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<DepartamentoEntity, DepartamentoId> {

    Optional<DepartamentoEntity> findByClave(String clave);

    boolean existsByClave(String clave);

    Page<DepartamentoEntity> findAll(Pageable pageable);
}
