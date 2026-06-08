package org.gestion.proyecto_sanitario.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.auth.dto.request.ChangePasswordRequestDto;
import org.gestion.proyecto_sanitario.auth.dto.request.LoginRequestDto;
import org.gestion.proyecto_sanitario.auth.dto.response.LoginResponseDto;
import org.gestion.proyecto_sanitario.auth.model.RefreshToken;
import org.gestion.proyecto_sanitario.auth.repository.UserRepository;
import org.gestion.proyecto_sanitario.auth.service.AuthService;
import org.gestion.proyecto_sanitario.auth.service.JwtService;
import org.gestion.proyecto_sanitario.auth.service.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        if (dto.getEmail().isEmpty() || dto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Email y contraseña son requeridos");
        }

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(dto.getEmail(), dto.getPassword()));
        var userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        var token = jwtService.generateToken(userDetails);
        var refreshToken = refreshTokenService.createRefreshToken(dto.getEmail());
        return LoginResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public LoginResponseDto refreshToken(String refreshToken) {
        RefreshToken token = refreshTokenService.validateRefreshToken(refreshToken);
        var userDetails = userDetailsService.loadUserByUsername(token.getUser().getEmail());
        var newToken = jwtService.generateToken(userDetails);
        return LoginResponseDto.builder()
                .token(newToken)
                .refreshToken(refreshToken) // El mismo refresh token se puede reutilizar hasta que expire
                .build();
    }

    @Override
    public void logout(String email) {
        refreshTokenService.deleteByUser(email);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequestDto dto) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("La contraseña actual no es correcta");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
