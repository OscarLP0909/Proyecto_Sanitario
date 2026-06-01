package org.gestion.proyecto_sanitario.shared.config;

import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.auth.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
