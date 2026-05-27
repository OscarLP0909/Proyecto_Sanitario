package org.gestion.proyecto_sanitario.paciente.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteResponseDto {

    private Long id;
    private String email;
    private String name;
    private String surname;
    private String nif;
    private LocalDateTime fechaNacimiento;
}
