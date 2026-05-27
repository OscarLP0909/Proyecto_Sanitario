package org.gestion.proyecto_sanitario.especialidad.mapper;

import org.gestion.proyecto_sanitario.especialidad.dto.request.EspecialidadRequestDto;
import org.gestion.proyecto_sanitario.especialidad.dto.response.EspecialidadResponseDto;
import org.gestion.proyecto_sanitario.especialidad.model.Especialidad;
import org.springframework.stereotype.Component;

@Component
public class EspecialidadMapper {

    public Especialidad toEntity(EspecialidadRequestDto dto) {
        return Especialidad.builder()
                .nombre(dto.getNombre())
                .build();
    }

    public EspecialidadResponseDto toResponseDto(Especialidad especialidad) {
        return EspecialidadResponseDto.builder()
                .id(especialidad.getId())
                .nombre(especialidad.getNombre())
                .build();
    }
}
