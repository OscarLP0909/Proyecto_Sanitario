package org.gestion.proyecto_sanitario;

import org.gestion.proyecto_sanitario.cita.dto.request.CitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.response.CitaResponseDto;
import org.gestion.proyecto_sanitario.cita.mapper.CitaMapper;
import org.gestion.proyecto_sanitario.cita.model.Cita;
import org.gestion.proyecto_sanitario.cita.model.EstadoCita;
import org.gestion.proyecto_sanitario.cita.repository.CitaRepository;
import org.gestion.proyecto_sanitario.cita.service.impl.CitaServiceImpl;
import org.gestion.proyecto_sanitario.medico.model.Slot;
import org.gestion.proyecto_sanitario.medico.repository.SlotRepository;
import org.gestion.proyecto_sanitario.paciente.model.Paciente;
import org.gestion.proyecto_sanitario.paciente.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CitaServiceImplTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private CitaMapper citaMapper;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private CitaServiceImpl citaService;


    @Test
    void crearCita_Success() {
        CitaRequestDto dto = CitaRequestDto.builder()
                .pacienteId(1L)
                .slotId(1L)
                .build();

        Paciente paciente = Paciente.builder()
                .id(1L)
                .name("Juan")
                .surname("Perez")
                .build();

        Slot slot = Slot.builder()
                .id(1L)
                .disponible(true)
                .build();

        Cita cita = Cita.builder()
                .id(1L)
                .paciente(paciente)
                .slot(slot)
                .estado(EstadoCita.PENDIENTE)
                .build();

        CitaResponseDto responseDto = CitaResponseDto.builder()
                .id(1L)
                .estado(EstadoCita.PENDIENTE)
                .build();

        when(citaRepository.findBySlotId(1L)).thenReturn(Optional.empty());
        when(citaMapper.toEntity(dto)).thenReturn(cita);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(citaRepository.save(cita)).thenReturn(cita);
        when(citaMapper.toResponseDto(cita)).thenReturn(responseDto);

        CitaResponseDto result = citaService.crearCita(dto);

        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo(EstadoCita.PENDIENTE);
        verify(slotRepository).save(slot);
        verify(citaRepository).save(cita);
    }

    @Test
    void crearCita_SlotNoDisponible() {
        CitaRequestDto dto = CitaRequestDto.builder()
                .pacienteId(1L)
                .slotId(1L)
                .build();

        when(citaRepository.findBySlotId(1L)).thenReturn(Optional.of(new Cita()));

        assertThatThrownBy(() -> citaService.crearCita(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El slot ya está reservado");
    }

    @Test
    void deleteCita_Success() {
        Long citaId = 1L;

        Cita cita = Cita.builder()
                .id(citaId)
                .estado(EstadoCita.PENDIENTE)
                .build();

        when(citaRepository.findById(citaId)).thenReturn(Optional.of(cita));

        citaService.deleteCita(citaId);

        verify(citaRepository).deleteById(citaId);
    }
}
