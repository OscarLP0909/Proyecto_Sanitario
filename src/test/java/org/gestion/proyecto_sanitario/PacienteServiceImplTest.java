package org.gestion.proyecto_sanitario;

import org.gestion.proyecto_sanitario.auth.model.Role;
import org.gestion.proyecto_sanitario.auth.model.User;
import org.gestion.proyecto_sanitario.auth.repository.UserRepository;
import org.gestion.proyecto_sanitario.paciente.dto.request.PacienteRequestDto;
import org.gestion.proyecto_sanitario.paciente.dto.response.PacienteResponseDto;
import org.gestion.proyecto_sanitario.paciente.mapper.PacienteMapper;
import org.gestion.proyecto_sanitario.paciente.model.Paciente;
import org.gestion.proyecto_sanitario.paciente.repository.PacienteRepository;
import org.gestion.proyecto_sanitario.paciente.service.impl.PacienteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceImplTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PacienteMapper pacienteMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    @Test
    void crearPaciente_Success() {
        PacienteRequestDto dto = PacienteRequestDto.builder()
                .email("juan@perez.com")
                .nif("12345678A")
                .name("Juan")
                .surname("Perez")
                .fechaNacimiento(LocalDate.parse("1990-01-01"))
                .build();

        User user = User.builder()
                .email(dto.getEmail())
                .password("encodedPassword")
                .role(Role.PACIENTE)
                .activo(true)
                .build();

        Paciente paciente = Paciente.builder()
                .id(1L)
                .nif(dto.getNif())
                .name(dto.getName())
                .surname(dto.getSurname())
                .fechaNacimiento(dto.getFechaNacimiento())
                .user(user)
                .build();

        PacienteResponseDto responseDto = PacienteResponseDto.builder()
                .id(1L)
                .nif(dto.getNif())
                .name(dto.getName())
                .surname(dto.getSurname())
                .fechaNacimiento(dto.getFechaNacimiento())
                .email(dto.getEmail())
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("encodedPassword");
        when(pacienteMapper.toEntity(dto)).thenReturn(paciente);
        when(userRepository.save(user)).thenReturn(user);
        when(pacienteRepository.save(paciente)).thenReturn(paciente);
        when(pacienteMapper.toResponseDto(paciente)).thenReturn(responseDto);

        PacienteResponseDto result = pacienteService.crearPaciente(dto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        verify(userRepository).save(user);
        verify(pacienteRepository).save(paciente);
    }

    @Test
    void crearPaciente_DuplicateEmail() {
        PacienteRequestDto dto = PacienteRequestDto.builder()
                .email("juan@perez.com")
                .name("Juan")
                .surname("Perez")
                .nif("12345678A")
                .fechaNacimiento(LocalDate.parse("1990-01-01"))
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> pacienteService.crearPaciente(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ya existe un usuario con ese email");
    }

    @Test
    void deletePaciente_Success() {
        Long id = 1L;

        when(pacienteRepository.existsById(id)).thenReturn(true);

        pacienteService.deletePaciente(id);

        verify(pacienteRepository).deleteById(id);
    }

    @Test
    void deletePaciente_NotFound() {
        Long id = 1L;

        when(pacienteRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> pacienteService.deletePaciente(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Paciente no encontrado");
    }

    @Test
    void findPacienteById_Success() {
        Long id = 1L;

        Paciente paciente = Paciente.builder()
                .id(id)
                .nif("12345678A")
                .name("Juan")
                .surname("Perez")
                .fechaNacimiento(LocalDate.parse("1990-01-01"))
                .build();

        PacienteResponseDto responseDto = PacienteResponseDto.builder()
                .id(id)
                .nif(paciente.getNif())
                .name(paciente.getName())
                .surname(paciente.getSurname())
                .fechaNacimiento(paciente.getFechaNacimiento())
                .email("juan@perez.com")
                .build();

        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));
        when(pacienteMapper.toResponseDto(paciente)).thenReturn(responseDto);
        PacienteResponseDto result = pacienteService.findById(id);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(pacienteRepository).findById(id);
    }
}
