package org.gestion.proyecto_sanitario.medico.mapper;

import org.gestion.proyecto_sanitario.especialidad.mapper.EspecialidadMapper;
import org.gestion.proyecto_sanitario.medico.dto.request.MedicoRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.response.MedicoResponseDto;
import org.gestion.proyecto_sanitario.medico.model.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper {

    private final EspecialidadMapper especialidadMapper;

    public MedicoMapper(EspecialidadMapper especialidadMapper) {
        this.especialidadMapper = especialidadMapper;
    }

    public Medico toEntity(MedicoRequestDto dto) {
        return Medico.builder()
                .name(dto.getName())
                .user(null)
                .surname(dto.getSurname())
                .nif(dto.getNif())
                .especialidades(null) // Las especialidades se asignarán en el servicio
                .build();

    }

    public MedicoResponseDto toResponseDto(Medico medico) {
        return MedicoResponseDto.builder()
                .id(medico.getId())
                .name(medico.getName())
                .email(medico.getUser().getEmail())
                .surname(medico.getSurname())
                .nif(medico.getNif())
                .especialidades(medico.getEspecialidades().stream()
                        .map(especialidadMapper::toResponseDto)
                .toList())
                .build();
    }
}
