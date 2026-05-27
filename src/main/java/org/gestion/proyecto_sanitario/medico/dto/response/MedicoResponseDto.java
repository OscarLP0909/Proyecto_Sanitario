package org.gestion.proyecto_sanitario.medico.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gestion.proyecto_sanitario.especialidad.dto.response.EspecialidadResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoResponseDto {

    private Long id;
    private String email;
    private String name;
    private String surname;
    private String nif;
    private List<EspecialidadResponseDto> especialidades;
}
