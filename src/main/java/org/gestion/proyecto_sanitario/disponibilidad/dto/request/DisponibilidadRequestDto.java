package org.gestion.proyecto_sanitario.disponibilidad.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gestion.proyecto_sanitario.disponibilidad.model.DiaSemana;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadRequestDto {

    @NotBlank(message = "El ID del médico es obligatorio")
    private Long medicoId;

    @NotBlank(message = "El dia de la semana es obligatorio")
    private DiaSemana diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotNull(message = "La duración en minutos es obligatoria")
    private Integer duracionMinutos;
}
