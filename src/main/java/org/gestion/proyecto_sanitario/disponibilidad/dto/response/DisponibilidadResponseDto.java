package org.gestion.proyecto_sanitario.disponibilidad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gestion.proyecto_sanitario.disponibilidad.model.DiaSemana;
import org.gestion.proyecto_sanitario.medico.dto.response.MedicoResponseDto;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadResponseDto {

    private Long id;
    private MedicoResponseDto medico;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer duracionMinutos;
}
