package org.gestion.proyecto_sanitario.medico.repository;

import org.gestion.proyecto_sanitario.medico.model.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByNif(String nif);
    Optional<Medico> findByUserEmail(String email);
    Page<Medico> findByEspecialidadesId(Long especialidadId, Pageable pageable);
}
