package org.gestion.proyecto_sanitario.cita.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gestion.proyecto_sanitario.cita.model.EstadoCita;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCitaRequestDto {

    @NotNull(message = "El estado de la cita es obligatorio")
    private EstadoCita estado;

    @NotBlank(message = "El motivo de la cita es obligatorio")
    private String motivo;

    @NotBlank(message = "Las notas de la cita son obligatorias")
    private String notas;
}
