package org.gestion.proyecto_sanitario.especialidad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialidadResponseDto {

    private Long id;
    private String nombre;
}
