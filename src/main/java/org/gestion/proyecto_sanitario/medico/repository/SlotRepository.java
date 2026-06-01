package org.gestion.proyecto_sanitario.medico.repository;

import org.gestion.proyecto_sanitario.medico.model.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface SlotRepository extends JpaRepository<Slot, Long> {

    // Disponibilidad de slots para un médico específico
    boolean existsByMedicoIdAndFechaHora(Long medicoId, LocalDateTime fechaHora);

    // Disponibilidad de slots para un médico específico en un rango de fechas
    boolean existsByMedicoIdAndFechaHoraBetween(Long medicoId, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin);

    // Buscar slots disponibles para un médico específico
    Page<Slot> findByMedicoIdAndDisponibleTrue(Long medicoId, Pageable pageable);

    void deleteByMedicoIdAndDisponibleTrue(Long medicoId);
}
