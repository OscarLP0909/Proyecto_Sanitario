package org.gestion.proyecto_sanitario.disponibilidad.mapper;

import org.gestion.proyecto_sanitario.disponibilidad.dto.request.DisponibilidadRequestDto;
import org.gestion.proyecto_sanitario.disponibilidad.dto.response.DisponibilidadResponseDto;
import org.gestion.proyecto_sanitario.disponibilidad.model.Disponibilidad;
import org.gestion.proyecto_sanitario.medico.mapper.MedicoMapper;
import org.springframework.stereotype.Component;

@Component
public class DisponibilidadMapper {

    private final MedicoMapper medicoMapper;

    public DisponibilidadMapper(MedicoMapper medicoMapper) {
        this.medicoMapper = medicoMapper;
    }

    public Disponibilidad toEntity(DisponibilidadRequestDto dto) {
        return Disponibilidad.builder()
                .medico(null)
                .diaSemana(dto.getDiaSemana())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .duracionMinutos(dto.getDuracionMinutos())
                .build();
    }

    public DisponibilidadResponseDto toResponseDto(Disponibilidad disponibilidad) {
        return DisponibilidadResponseDto.builder()
                .id(disponibilidad.getId())
                .medico(medicoMapper.toResponseDto(disponibilidad.getMedico()))
                .diaSemana(disponibilidad.getDiaSemana())
                .horaInicio(disponibilidad.getHoraInicio())
                .horaFin(disponibilidad.getHoraFin())
                .duracionMinutos(disponibilidad.getDuracionMinutos())
                .build();
    }
}
