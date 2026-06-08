package org.gestion.proyecto_sanitario.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.auth.dto.request.ChangePasswordRequestDto;
import org.gestion.proyecto_sanitario.auth.dto.request.LoginRequestDto;
import org.gestion.proyecto_sanitario.auth.dto.request.RefreshTokenRequestDto;
import org.gestion.proyecto_sanitario.auth.dto.response.LoginResponseDto;
import org.gestion.proyecto_sanitario.auth.service.AuthService;
import org.gestion.proyecto_sanitario.paciente.dto.request.PacienteRequestDto;
import org.gestion.proyecto_sanitario.paciente.dto.response.PacienteResponseDto;
import org.gestion.proyecto_sanitario.paciente.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PacienteService pacienteService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        var response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<PacienteResponseDto> register(@Valid @RequestBody PacienteRequestDto dto) {
        var response = pacienteService.crearPaciente(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        authService.changePassword(userDetails.getUsername(), dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto dto) {
        var response = authService.refreshToken(dto.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
