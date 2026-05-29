package org.gestion.proyecto_sanitario.medico.service;

import org.gestion.proyecto_sanitario.medico.dto.request.SlotRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.response.SlotResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;


public interface SlotService {

    SlotResponseDto crearSlot(SlotRequestDto dto);

    Page<SlotResponseDto> findAll(Pageable pageable);

    SlotResponseDto findById(Long id);

    SlotResponseDto updateSlot(Long id, SlotRequestDto dto);

    void deleteSlot(Long id);

    Page<SlotResponseDto> findSlotsByMedicoDisponibles(Long medicoId, Pageable pageable);
}
