package org.gestion.proyecto_sanitario.cita.service.impl;

import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.cita.dto.request.CitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.request.UpdateCitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.response.CitaResponseDto;
import org.gestion.proyecto_sanitario.cita.mapper.CitaMapper;
import org.gestion.proyecto_sanitario.cita.model.Cita;
import org.gestion.proyecto_sanitario.cita.model.EstadoCita;
import org.gestion.proyecto_sanitario.cita.repository.CitaRepository;
import org.gestion.proyecto_sanitario.cita.service.CitaService;
import org.gestion.proyecto_sanitario.medico.repository.SlotRepository;
import org.gestion.proyecto_sanitario.paciente.repository.PacienteRepository;
import org.gestion.proyecto_sanitario.paciente.service.PacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final PacienteRepository pacienteRepository;
    private final SlotRepository slotRepository;
    private final JavaMailSender mailSender;

    private void enviarEmailNotificacion(String email, EstadoCita estado) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Estado de su cita médica");

        String texto = switch (estado) {
            case PENDIENTE -> "Su cita ha sido registrada y está pendiente de confirmación.";
            case COMPLETADA -> "Su cita ha sido marcada como completada. Gracias por confiar en nosotros.";
            case CONFIRMADA -> "Su cita ha sido confirmada. Esperamos verle pronto.";
            case CANCELADA -> "Su cita ha sido cancelada. Si tiene alguna duda, contacte con nosotros.";
        };

        message.setText(texto);
        mailSender.send(message);
    }


    @Override
    public CitaResponseDto crearCita(CitaRequestDto dto) {
        if(citaRepository.findBySlotId(dto.getSlotId()).isPresent()) {
            throw new IllegalArgumentException("El slot ya está reservado");
        }
        var cita = citaMapper.toEntity(dto);
        cita.setPaciente(pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado")));
        cita.setEstado(EstadoCita.PENDIENTE);
        var slot = slotRepository.findById(dto.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot no encontrado"));
        cita.setSlot(slot);
        slot.setDisponible(false);
        slotRepository.save(slot);
        var saved = citaRepository.save(cita);
        return citaMapper.toResponseDto(saved);
    }

    @Override
    public Page<CitaResponseDto> findAll(Pageable pageable) {
        return citaRepository.findAll(pageable)
                .map(citaMapper::toResponseDto);
    }

    @Override
    public CitaResponseDto findById(Long id) {
        return citaRepository.findById(id)
                .map(citaMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
    }

    @Override
    @Transactional
    public CitaResponseDto updateCita(Long id, UpdateCitaRequestDto dto) {
        var citaExistente = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        if (dto.getEstado() != null) citaExistente.setEstado(dto.getEstado());
        if (dto.getMotivo() != null) citaExistente.setMotivo(dto.getMotivo());
        if (dto.getNotas() != null) citaExistente.setNotas(dto.getNotas());

        var updated = citaRepository.save(citaExistente);

        try {
            String emailPaciente = citaExistente.getPaciente().getUser().getEmail();
            System.out.println("Enviando email a: " + emailPaciente + " con estado: " + dto.getEstado());
            if (dto.getEstado() != null) {
                enviarEmailNotificacion(emailPaciente, dto.getEstado());
            }
        } catch (Exception e) {
            System.err.println("Error al enviar email de notificación: " + e.getMessage());
        }
        return citaMapper.toResponseDto(updated);
    }


    @Override
    public void deleteCita(Long id) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        if (cita.getEstado() == EstadoCita.COMPLETADA) {
            throw new IllegalArgumentException("No se puede eliminar una cita completada");
        }
        citaRepository.deleteById(id);
    }

    @Override
    public Page<CitaResponseDto> findbyPacienteId(Long pacienteId, Pageable pageable) {
        return citaRepository.findByPacienteId(pacienteId, pageable)
                .map(citaMapper::toResponseDto);
    }

    @Override
    public CitaResponseDto cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        if (cita.getEstado() == EstadoCita.COMPLETADA) {
            throw new IllegalArgumentException("No se puede cancelar una cita completada");
        }
        cita.setEstado(EstadoCita.CANCELADA);
        return citaMapper.toResponseDto(citaRepository.save(cita));
    }

    @Override
    public CitaResponseDto cambiarEstado(Long id, EstadoCita nuevoEstado) {
        var cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        cita.setEstado(nuevoEstado);
        return citaMapper.toResponseDto(citaRepository.save(cita));
    }

    @Override
    public Page<CitaResponseDto> findByEstado(EstadoCita estado, Pageable pageable) {
        return citaRepository.findByEstado(estado, pageable)
                .map(citaMapper::toResponseDto);
    }

    @Override
    public Page<CitaResponseDto> findBySlotMedicoUserEmail(String email, Pageable pageable) {
        return citaRepository.findBySlotMedicoUserEmail(email, pageable)
                .map(citaMapper::toResponseDto);
    }

    @Override
    public Page<CitaResponseDto> findByPacienteUserEmail(String email, Pageable pageable) {
        return citaRepository.findByPacienteUserEmail(email, pageable)
                .map(citaMapper::toResponseDto);
    }
}
