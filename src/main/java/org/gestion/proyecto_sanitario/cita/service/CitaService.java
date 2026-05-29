package org.gestion.proyecto_sanitario.cita.service;

import org.gestion.proyecto_sanitario.cita.dto.request.CitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.request.UpdateCitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.response.CitaResponseDto;
import org.gestion.proyecto_sanitario.cita.model.EstadoCita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CitaService {

    CitaResponseDto crearCita(CitaRequestDto dto);

    Page<CitaResponseDto> findAll(Pageable pageable);

    CitaResponseDto findById(Long id);

    CitaResponseDto updateCita(Long id, UpdateCitaRequestDto dto);

    void deleteCita(Long id);

    Page<CitaResponseDto> findbyPacienteId(Long pacienteId, Pageable pageable);

    CitaResponseDto cancelarCita(Long id);

    Page<CitaResponseDto> findByEstado(EstadoCita estado, Pageable pageable);
}
