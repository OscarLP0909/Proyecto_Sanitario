package org.gestion.proyecto_sanitario.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.auth.dto.request.LoginRequestDto;
import org.gestion.proyecto_sanitario.auth.dto.response.LoginResponseDto;
import org.gestion.proyecto_sanitario.auth.service.AuthService;
import org.gestion.proyecto_sanitario.auth.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        if (dto.getEmail().isEmpty() || dto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Email y contraseña son requeridos");
        }

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(dto.getEmail(), dto.getPassword()));
        var userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        var token = jwtService.generateToken(userDetails);
        return LoginResponseDto.builder()
                .token(token)
                .build();
    }
}
