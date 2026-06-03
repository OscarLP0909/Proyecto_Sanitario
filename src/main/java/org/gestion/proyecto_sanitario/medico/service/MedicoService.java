package org.gestion.proyecto_sanitario.medico.service;

import jakarta.transaction.Transactional;
import org.gestion.proyecto_sanitario.medico.dto.request.MedicoRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.request.MedicoUpdateRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.response.MedicoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicoService {

    MedicoResponseDto crearMedico(MedicoRequestDto dto);

    Page<MedicoResponseDto> findAll(Pageable pageable);

    MedicoResponseDto findbyId(Long id);

    @Transactional
    MedicoResponseDto updateMedico(Long id, MedicoUpdateRequestDto dto);

    void deleteMedico(Long id);

    Page<MedicoResponseDto> findByEspecialidadID(Long especialidadId, Pageable pageable);

    MedicoResponseDto findByNif(String nif);
}
