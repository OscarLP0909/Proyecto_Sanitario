package org.gestion.proyecto_sanitario.cita.service.impl;

import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.cita.dto.request.CitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.request.UpdateCitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.response.CitaResponseDto;
import org.gestion.proyecto_sanitario.cita.mapper.CitaMapper;
import org.gestion.proyecto_sanitario.cita.model.EstadoCita;
import org.gestion.proyecto_sanitario.cita.repository.CitaRepository;
import org.gestion.proyecto_sanitario.cita.service.CitaService;
import org.gestion.proyecto_sanitario.medico.repository.SlotRepository;
import org.gestion.proyecto_sanitario.paciente.repository.PacienteRepository;
import org.gestion.proyecto_sanitario.paciente.service.PacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final PacienteRepository pacienteRepository;
    private final SlotRepository slotRepository;


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
    public CitaResponseDto updateCita(Long id, UpdateCitaRequestDto dto) {
        var citaExistente = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        citaExistente.setEstado(dto.getEstado());
        if (dto.getMotivo() != null) citaExistente.setMotivo(dto.getMotivo());
        if (dto.getNotas() != null) citaExistente.setNotas(dto.getNotas());
        var updated = citaRepository.save(citaExistente);
        return citaMapper.toResponseDto(updated);
    }


    @Override
    public void deleteCita(Long id) {

        if(!citaRepository.existsById(id)) {
            throw new IllegalArgumentException("Cita no encontrada");
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
        var citaExist = citaRepository.findById(id)
                .map(cita -> {
                    cita.setEstado(EstadoCita.CANCELADA);
                    return citaRepository.save(cita);
                })
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        return citaMapper.toResponseDto(citaExist);
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
