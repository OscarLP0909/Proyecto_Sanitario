package org.gestion.proyecto_sanitario.medico.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicoUpdateRequestDto {

    private String name;
    private String surname;
    private String nif;
    private List<Long> especialidadesIds;
}
