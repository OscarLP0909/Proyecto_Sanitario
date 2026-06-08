package org.gestion.proyecto_sanitario.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequestDto {

    @NotBlank(message = "El token de refresco es obligatorio")
    private String refreshToken;
}
