package org.gestion.proyecto_sanitario.paciente.service;

import org.gestion.proyecto_sanitario.paciente.dto.request.PacienteRequestDto;
import org.gestion.proyecto_sanitario.paciente.dto.response.PacienteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PacienteService {

    PacienteResponseDto crearPaciente(PacienteRequestDto dto);

    Page<PacienteResponseDto> findAll(Pageable pageable);

    PacienteResponseDto findById(Long id);

    PacienteResponseDto updatePaciente(Long id, PacienteRequestDto dto);

    void deletePaciente(Long id);

    PacienteResponseDto findByNif(String nif);
}
