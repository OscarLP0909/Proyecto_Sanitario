package org.gestion.proyecto_sanitario.medico.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotRequestDto {

    @NotBlank(message = "El ID del médico es obligatorio")
    private Long medicoId;

    @NotBlank(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHora;

}
