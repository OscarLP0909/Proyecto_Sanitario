package org.gestion.proyecto_sanitario;

import org.gestion.proyecto_sanitario.auth.dto.response.UserResponseDto;
import org.gestion.proyecto_sanitario.auth.model.Role;
import org.gestion.proyecto_sanitario.auth.model.User;
import org.gestion.proyecto_sanitario.auth.repository.UserRepository;
import org.gestion.proyecto_sanitario.especialidad.repository.EspecialidadRepository;
import org.gestion.proyecto_sanitario.medico.dto.request.MedicoRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.response.MedicoResponseDto;
import org.gestion.proyecto_sanitario.medico.mapper.MedicoMapper;
import org.gestion.proyecto_sanitario.medico.model.Medico;
import org.gestion.proyecto_sanitario.medico.repository.MedicoRepository;
import org.gestion.proyecto_sanitario.medico.service.impl.MedicoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MedicoServiceImplTest {

    @Mock
    private MedicoRepository medicoRepository;

     @Mock
    private MedicoMapper medicoMapper;

     @Mock
    private EspecialidadRepository especialidadRepository;

     @Mock
    private UserRepository userRepository;

     @Mock
     private PasswordEncoder passwordEncoder;

     @Mock
     private JavaMailSender mailSender;

     @InjectMocks
    private MedicoServiceImpl medicoService;

     @Test
    void crearMedico_Success() {
         MedicoRequestDto dto = MedicoRequestDto.builder()
                 .email("juan@perez.com")
                 .name("Juan")
                 .surname("Perez")
                 .especialidadesIds(List.of(1L, 2L))
                 .build();

         User user = User.builder()
                 .email(dto.getEmail())
                 .password("encodedPassword")
                 .role(Role.MEDICO)
                 .activo(true)
                 .build();

         Medico medico = Medico.builder()
                 .id(1L)
                 .name(dto.getName())
                 .surname(dto.getSurname())
                 .user(user)
                 .build();

         MedicoResponseDto responseDto = MedicoResponseDto.builder()
                 .id(1L)
                 .email(dto.getEmail())
                 .name(dto.getName())
                 .surname(dto.getSurname())
                 .especialidades(List.of())
                 .build();

         when(userRepository.findByEmail(dto.getEmail())).thenReturn(java.util.Optional.empty());
         when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("encodedPassword");
         when(medicoMapper.toEntity(dto)).thenReturn(medico);
         when(especialidadRepository.findAllById(dto.getEspecialidadesIds())).thenReturn(List.of());
         when(medicoRepository.save(medico)).thenReturn(medico);
         when(medicoMapper.toResponseDto(medico)).thenReturn(responseDto);

         MedicoResponseDto result = medicoService.crearMedico(dto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        verify(userRepository).save(user);
        verify(medicoRepository).save(medicoMapper.toEntity(dto));
     }

     @Test
    void crearMedico_DuplicateEmail() {
         MedicoRequestDto dto = MedicoRequestDto.builder()
                 .email("juan@perez.com")
                 .name("Juan")
                 .surname("Perez")
                 .especialidadesIds(List.of(1L, 2L))
                 .build();

         when(userRepository.findByEmail(dto.getEmail())).thenReturn(java.util.Optional.of(new User()));

         assertThatThrownBy(() -> medicoService.crearMedico(dto))
                 .isInstanceOf(IllegalArgumentException.class)
                 .hasMessage("Ya existe un usuario con ese email");
     }

     @Test
    void deleteMedico_Success() {
         Long id = 1L;

         when(medicoRepository.existsById(id)).thenReturn(true);

         medicoService.deleteMedico(id);

         verify(medicoRepository).deleteById(id);
     }

     @Test
    void deleteMedico_NotFound() {
         Long id = 1L;

         when(medicoRepository.existsById(id)).thenReturn(false);

         assertThatThrownBy(() -> medicoService.deleteMedico(id))
                 .isInstanceOf(IllegalArgumentException.class)
                 .hasMessage("Medico no encontrado");
     }

     @Test
    void findMedicoById_Success() {
         Long id = 1L;

         Medico medico = Medico.builder()
                 .id(id)
                 .name("Juan")
                 .surname("Perez")
                 .build();

         MedicoResponseDto responseDto = MedicoResponseDto.builder()
                 .id(id)
                 .name("Juan")
                 .surname("Perez")
                 .especialidades(List.of())
                 .build();

         when(medicoRepository.findById(id)).thenReturn(java.util.Optional.of(medico));
         when(medicoMapper.toResponseDto(medico)).thenReturn(responseDto);

         MedicoResponseDto result = medicoService.findbyId(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(medico.getName());
        assertThat(result.getSurname()).isEqualTo(medico.getSurname());
     }
}
