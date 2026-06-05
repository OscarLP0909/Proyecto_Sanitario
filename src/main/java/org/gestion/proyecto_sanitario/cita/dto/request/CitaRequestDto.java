package org.gestion.proyecto_sanitario.cita.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaRequestDto {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El ID del slot es obligatorio")
    private Long slotId;

    @NotBlank(message = "El motivo de la cita es obligatorio")
    private String motivo;

    private String notas = "";
}
