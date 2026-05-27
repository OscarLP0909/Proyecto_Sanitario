package org.gestion.proyecto_sanitario.especialidad.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialidadRequestDto {

    @NotBlank(message = "El nombre de la especialidad es obligatorio")
    private String nombre;
}
