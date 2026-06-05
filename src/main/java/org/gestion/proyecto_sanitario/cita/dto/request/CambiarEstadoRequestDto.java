package org.gestion.proyecto_sanitario.cita.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gestion.proyecto_sanitario.cita.model.EstadoCita;

@Data
@NoArgsConstructor
public class CambiarEstadoRequestDto {
    @NotNull(message = "El estado es obligatorio")
    private EstadoCita estado;
}
