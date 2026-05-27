package org.gestion.proyecto_sanitario.paciente.mapper;

import org.gestion.proyecto_sanitario.paciente.dto.request.PacienteRequestDto;
import org.gestion.proyecto_sanitario.paciente.dto.response.PacienteResponseDto;
import org.gestion.proyecto_sanitario.paciente.model.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public Paciente toEntity(PacienteRequestDto dto) {
        return Paciente.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .user(null)
                .nif(dto.getNif())
                .fechaNacimiento(dto.getFechaNacimiento())
                .build();
    }

    public PacienteResponseDto toResponseDto(Paciente paciente) {
        return PacienteResponseDto.builder()
                .id(paciente.getId())
                .name(paciente.getName())
                .email(paciente.getUser().getEmail())
                .surname(paciente.getSurname())
                .nif(paciente.getNif())
                .fechaNacimiento(paciente.getFechaNacimiento())
                .build();
    }
}
