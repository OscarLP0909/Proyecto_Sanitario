package org.gestion.proyecto_sanitario.cita.repository;

import org.gestion.proyecto_sanitario.cita.model.Cita;
import org.gestion.proyecto_sanitario.cita.model.EstadoCita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Buscar citas por paciente
    Page<Cita> findByPacienteId(Long pacienteId, Pageable pageable);

    // Buscar por Estado
    Page<Cita> findByEstado(EstadoCita estado, Pageable pageable);

    // Buscar por slot
    Optional<Cita>findBySlotId(Long slotId);
}
