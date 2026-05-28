package org.gestion.proyecto_sanitario.cita.mapper;

import org.gestion.proyecto_sanitario.cita.dto.request.CitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.response.CitaResponseDto;
import org.gestion.proyecto_sanitario.cita.model.Cita;
import org.gestion.proyecto_sanitario.medico.mapper.SlotMapper;
import org.gestion.proyecto_sanitario.paciente.mapper.PacienteMapper;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    private final SlotMapper slotMapper;
    private final PacienteMapper pacienteMapper;

    public CitaMapper(SlotMapper slotMapper, PacienteMapper pacienteMapper) {
        this.slotMapper = slotMapper;
        this.pacienteMapper = pacienteMapper;
    }

    public Cita toEntity(CitaRequestDto dto) {
        return Cita.builder()
                .slot(null)
                .paciente(null)
                .build();
    }

    public CitaResponseDto toResponseDto(Cita cita) {
        return CitaResponseDto.builder()
                .id(cita.getId())
                .slot(slotMapper.toResponseDto(cita.getSlot()))
                .paciente(pacienteMapper.toResponseDto(cita.getPaciente()))
                .motivo(cita.getMotivo())
                .estado(cita.getEstado())
                .notas(cita.getNotas())
                .build();
    }
}
