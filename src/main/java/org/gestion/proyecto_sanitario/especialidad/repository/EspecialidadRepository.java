package org.gestion.proyecto_sanitario.especialidad.repository;

import org.gestion.proyecto_sanitario.especialidad.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
        // Buscar especialidad por nombre
        Optional<Especialidad> findByNombre(String nombre);

        // Verificar si una especialidad existe por nombre
        boolean existsByNombre(String nombre);
}
