package org.gestion.proyecto_sanitario;

import org.gestion.proyecto_sanitario.especialidad.dto.request.EspecialidadRequestDto;
import org.gestion.proyecto_sanitario.especialidad.dto.response.EspecialidadResponseDto;
import org.gestion.proyecto_sanitario.especialidad.mapper.EspecialidadMapper;
import org.gestion.proyecto_sanitario.especialidad.model.Especialidad;
import org.gestion.proyecto_sanitario.especialidad.repository.EspecialidadRepository;
import org.gestion.proyecto_sanitario.especialidad.service.impl.EspecialidadServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class EspecialidadServiceImplTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @Mock
    private EspecialidadMapper especialidadMapper;

    @InjectMocks
    private EspecialidadServiceImpl especialidadService;

    @Test
    void crearEspecialidad_Success() {
        EspecialidadRequestDto dto = EspecialidadRequestDto.builder()
                .nombre("Cardiología")
                .build();

        Especialidad especialidad = Especialidad.builder()
                .id(1L)
                .nombre("Cardiología")
                .build();

        EspecialidadResponseDto responseDto = EspecialidadResponseDto.builder()
                .id(1L)
                .nombre("Cardiología")
                .build();

        when(especialidadRepository.existsByNombre(dto.getNombre())).thenReturn(false);
        when(especialidadMapper.toEntity(dto)).thenReturn(especialidad);
        when(especialidadRepository.save(especialidad)).thenReturn(especialidad);
        when(especialidadMapper.toResponseDto(especialidad)).thenReturn(responseDto);

        // WHEN
        EspecialidadResponseDto result = especialidadService.crearEspecialidad(dto);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo(dto.getNombre());
        verify(especialidadRepository).save(especialidad);
    }

    @Test
    void crearEspecialidad_DuplicateName() {
        EspecialidadRequestDto dto = EspecialidadRequestDto.builder()
                .nombre("Cardiología")
                .build();

        when(especialidadRepository.existsByNombre(dto.getNombre())).thenReturn(true);

        // WHEN / THEN
        assertThatThrownBy(() -> especialidadService.crearEspecialidad(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ya existe una especialidad con ese nombre");
    }

    @Test
    void deleteEspecialidad_Success() {
        Long id = 1L;

        when(especialidadRepository.existsById(id)).thenReturn(true);

        especialidadService.deleteEspecialidad(id);

        verify(especialidadRepository).deleteById(id);
    }

    @Test
    void deleteEspecialidad_NotFound() {
        Long id = 1L;

        when(especialidadRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> especialidadService.deleteEspecialidad(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Especialidad no encontrada");
    }
}
