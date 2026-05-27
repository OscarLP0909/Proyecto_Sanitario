package org.gestion.proyecto_sanitario.medico.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotResponseDto {

    private Long id;
    private MedicoResponseDto medico;
    private LocalDateTime fechaHora;
}
