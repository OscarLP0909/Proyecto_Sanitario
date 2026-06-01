package org.gestion.proyecto_sanitario.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.auth.dto.request.LoginRequestDto;
import org.gestion.proyecto_sanitario.auth.dto.response.LoginResponseDto;
import org.gestion.proyecto_sanitario.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        var response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
