package com.dsw02.empleado.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, EmpleadoId> {

	Page<EmpleadoEntity> findAll(Pageable pageable);

	@Query("select e from EmpleadoEntity e where lower(e.correo) = lower(?1)")
	Optional<EmpleadoEntity> findByCorreoIgnoreCase(String correo);

	@Query("select count(e) > 0 from EmpleadoEntity e where lower(e.correo) = lower(?1)")
	boolean existsByCorreoIgnoreCase(String correo);

	Page<EmpleadoEntity> findByDepartamentoClave(String departamentoClave, Pageable pageable);

	long countByDepartamentoClave(String departamentoClave);
}
