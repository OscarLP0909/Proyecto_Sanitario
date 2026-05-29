package org.gestion.proyecto_sanitario.medico.mapper;

import org.gestion.proyecto_sanitario.medico.dto.request.SlotRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.response.SlotResponseDto;
import org.gestion.proyecto_sanitario.medico.model.Slot;
import org.springframework.stereotype.Component;

@Component
public class SlotMapper {

    private final MedicoMapper medicoMapper;

    public SlotMapper(MedicoMapper medicoMapper) {
        this.medicoMapper = medicoMapper;
    }


    public Slot toEntity(SlotRequestDto dto) {
        return Slot.builder()
                .medico(null)
                .disponible(true)
                .fechaHora(dto.getFechaHora())
                .build();
    }

    public SlotResponseDto toResponseDto(Slot slot) {
        return SlotResponseDto.builder()
                .id(slot.getId())
                .medico(medicoMapper.toResponseDto(slot.getMedico()))
                .fechaHora(slot.getFechaHora())
                .build();
    }
}
