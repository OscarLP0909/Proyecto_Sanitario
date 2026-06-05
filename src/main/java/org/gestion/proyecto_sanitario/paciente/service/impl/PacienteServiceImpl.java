package org.gestion.proyecto_sanitario.paciente.service.impl;

import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.auth.model.User;
import org.gestion.proyecto_sanitario.auth.repository.UserRepository;
import org.gestion.proyecto_sanitario.paciente.dto.request.PacienteRequestDto;
import org.gestion.proyecto_sanitario.paciente.dto.response.PacienteResponseDto;
import org.gestion.proyecto_sanitario.paciente.mapper.PacienteMapper;
import org.gestion.proyecto_sanitario.paciente.model.Paciente;
import org.gestion.proyecto_sanitario.paciente.repository.PacienteRepository;
import org.gestion.proyecto_sanitario.paciente.service.PacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private void enviarEmailCredenciales(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Bienvenido al sistema sanitario");
        message.setText("Sus credenciales de acceso:\nEmail: " + email + "\nContraseña temporal: " + password
                + "\n\nNo comparta esta contraseña con nadie. Por favor, cambie su contraseña tras el primer inicio de sesión.");
        mailSender.send(message);
    }


    @Override
    public PacienteResponseDto crearPaciente(PacienteRequestDto dto) {
        if(pacienteRepository.findByNif(dto.getNif()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un paciente con ese NIF");
        }
        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }
        // Si el DTO trae contraseña (auto-registro), la usamos; si no, generamos una temporal
        String password = (dto.getPassword() != null && !dto.getPassword().isBlank())
                ? dto.getPassword()
                : UUID.randomUUID().toString().substring(0, 8);

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(password))
                .role(org.gestion.proyecto_sanitario.auth.model.Role.PACIENTE)
                .activo(true)
                .build();
        userRepository.save(user);
        Paciente paciente = pacienteMapper.toEntity(dto);
        paciente.setUser(user);
        var saved = pacienteRepository.save(paciente);

        // Solo enviamos email si la contraseña fue generada (admin creó el paciente)
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            try {
                enviarEmailCredenciales(dto.getEmail(), password);
            } catch (Exception ignored) {
                // El email no es crítico — el paciente ya fue creado
            }
        }

        return pacienteMapper.toResponseDto(saved);
    }

    @Override
    public Page<PacienteResponseDto> findAll(Pageable pageable) {
        return pacienteRepository.findAll(pageable)
                .map(pacienteMapper::toResponseDto);
    }

    @Override
    public PacienteResponseDto findById(Long id) {
        return pacienteRepository.findById(id)
                .map(pacienteMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
    }

    @Override
    public PacienteResponseDto updatePaciente(Long id, PacienteRequestDto dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        paciente.setName(dto.getName());
        paciente.setSurname(dto.getSurname());
        paciente.setNif(dto.getNif());

        var updated = pacienteRepository.save(paciente);
        return pacienteMapper.toResponseDto(updated);
    }

    @Override
    public void deletePaciente(Long id) {
        if(!pacienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Paciente no encontrado");
        }
        pacienteRepository.deleteById(id);
    }

    @Override
    public PacienteResponseDto findByNif(String nif) {
        return pacienteRepository.findByNif(nif)
                .map(pacienteMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
    }

    @Override
    public PacienteResponseDto findMe(String email) {
        return pacienteRepository.findByUserEmail(email)
                .map(pacienteMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
    }
}
