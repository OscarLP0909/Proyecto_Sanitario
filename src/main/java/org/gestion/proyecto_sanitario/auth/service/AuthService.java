package org.gestion.proyecto_sanitario.auth.service;

import org.gestion.proyecto_sanitario.auth.dto.request.ChangePasswordRequestDto;
import org.gestion.proyecto_sanitario.auth.dto.request.LoginRequestDto;
import org.gestion.proyecto_sanitario.auth.dto.response.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto dto);

    void changePassword(String email, ChangePasswordRequestDto dto);
}
