package org.gestion.proyecto_sanitario.medico.service;

import org.gestion.proyecto_sanitario.medico.dto.request.MedicoRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.response.MedicoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicoService {

    MedicoResponseDto crearMedico(MedicoRequestDto dto);

    Page<MedicoResponseDto> findAll(Pageable pageable);

    MedicoResponseDto findbyId(Long id);

    MedicoResponseDto updateMedico(Long id, MedicoRequestDto dto);

    void deleteMedico(Long id);

    Page<MedicoResponseDto> findByEspecialidadID(Long especialidadId, Pageable pageable);

    MedicoResponseDto findByNif(String nif);
}
