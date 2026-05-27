package org.gestion.proyecto_sanitario.cita.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gestion.proyecto_sanitario.cita.model.EstadoCita;
import org.gestion.proyecto_sanitario.medico.dto.response.SlotResponseDto;
import org.gestion.proyecto_sanitario.paciente.dto.response.PacienteResponseDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaResponseDto {

    private Long id;
    private PacienteResponseDto paciente;
    private SlotResponseDto slot;
    private EstadoCita estado;
    private String motivo;
    private String notas;
}
