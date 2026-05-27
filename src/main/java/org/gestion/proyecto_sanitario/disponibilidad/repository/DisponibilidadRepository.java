package org.gestion.proyecto_sanitario.disponibilidad.repository;

import org.gestion.proyecto_sanitario.disponibilidad.model.DiaSemana;
import org.gestion.proyecto_sanitario.disponibilidad.model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

    // Buscar por médico y día
    Optional<Disponibilidad> findByMedicoIdAndDiaSemana(Long medicoId, DiaSemana dia);

    // Eliminar por médico y día
    void deleteByMedicoIdAndDiaSemana(Long medicoId, DiaSemana dia);

    // Verificar si existe por médico y día
    boolean existsByMedicoIdAndDiaSemana(Long medicoId, DiaSemana dia);

    // Buscar por médico
    List<Disponibilidad> findByMedicoId(Long medicoId);

    // Buscar por día
    Disponibilidad findByDiaSemana(DiaSemana dia);
}
