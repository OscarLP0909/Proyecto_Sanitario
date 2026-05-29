package org.gestion.proyecto_sanitario.disponibilidad.service.impl;

import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.disponibilidad.dto.request.DisponibilidadRequestDto;
import org.gestion.proyecto_sanitario.disponibilidad.dto.response.DisponibilidadResponseDto;
import org.gestion.proyecto_sanitario.disponibilidad.mapper.DisponibilidadMapper;
import org.gestion.proyecto_sanitario.disponibilidad.model.DiaSemana;
import org.gestion.proyecto_sanitario.disponibilidad.model.Disponibilidad;
import org.gestion.proyecto_sanitario.disponibilidad.repository.DisponibilidadRepository;
import org.gestion.proyecto_sanitario.disponibilidad.service.DisponibilidadService;
import org.gestion.proyecto_sanitario.medico.model.Slot;
import org.gestion.proyecto_sanitario.medico.repository.MedicoRepository;
import org.gestion.proyecto_sanitario.medico.repository.SlotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DisponibilidadServiceImpl implements DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;
    private final DisponibilidadMapper disponibilidadMapper;
    private final SlotRepository slotRepository;
    private final MedicoRepository medicoRepository;


    @Override
    public DisponibilidadResponseDto crearDisponibilidad(DisponibilidadRequestDto dto) {
        var medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Medico no encontrado con id: " + dto.getMedicoId()));
        var disponibilidad = disponibilidadMapper.toEntity(dto);
        disponibilidad.setMedico(medico);
        LocalTime hora = dto.getHoraInicio();
        while(hora.isBefore(dto.getHoraFin())) {
            Slot slot = Slot.builder()
                    .medico(medico)
                    .fechaHora(LocalDateTime.of(LocalDate.now(), hora))
                    .disponible(true)
                    .build();
            slotRepository.save(slot);
            hora = hora.plusMinutes(dto.getDuracionMinutos());
        }
        var saved = disponibilidadRepository.save(disponibilidad);
        return disponibilidadMapper.toResponseDto(saved);

    }

    @Override
    public Page<DisponibilidadResponseDto> findAll(Pageable pageable) {
        return disponibilidadRepository.findAll(pageable)
                .map(disponibilidadMapper::toResponseDto);
    }

    @Override
    public DisponibilidadResponseDto findById(Long id) {
        return disponibilidadRepository.findById(id)
                .map(disponibilidadMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con id: " + id));
    }

    @Override
    public DisponibilidadResponseDto updateDisponibilidad(Long id, DisponibilidadRequestDto dto) {
        var medicoId = dto.getMedicoId();
        var medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Medico no encontrado con id: " + medicoId));
        var disponibilidad = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con id: " + id));
        disponibilidad.setHoraInicio(dto.getHoraInicio());
        disponibilidad.setHoraFin(dto.getHoraFin());
        disponibilidad.setDuracionMinutos(dto.getDuracionMinutos());
        slotRepository.deleteByMedicoIdAndDisponibleTrue(medicoId);
        LocalTime hora = dto.getHoraInicio();
        while(hora.isBefore(dto.getHoraFin())) {
            Slot slot = Slot.builder()
                    .medico(medico)
                    .fechaHora(LocalDateTime.of(LocalDate.now(), hora))
                    .disponible(true)
                    .build();
            slotRepository.save(slot);
            hora = hora.plusMinutes(dto.getDuracionMinutos());
        }
        var saved = disponibilidadRepository.save(disponibilidad);
        return disponibilidadMapper.toResponseDto(saved);
    }

    @Override
    public void deleteDisponibilidad(Long id) {
        if(!disponibilidadRepository.existsById(id)) {
            throw new RuntimeException("Disponibilidad no encontrada con id: " + id);
        }
        var disponibilidad = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada con id: " + id));
        slotRepository.deleteByMedicoIdAndDisponibleTrue(disponibilidad.getMedico().getId());
        disponibilidadRepository.deleteById(id);
    }

    @Override
    public Page<DisponibilidadResponseDto> findByMedicoId(Long medicoId, Pageable pageable) {
        return disponibilidadRepository.findByMedicoId(medicoId, pageable)
                .map(disponibilidadMapper::toResponseDto);
    }

    @Override
    public Page<DisponibilidadResponseDto> findbyDiaSemana(DiaSemana diaSemana, Pageable pageable) {
        return disponibilidadRepository.findByDiaSemana(diaSemana, pageable)
                .map(disponibilidadMapper::toResponseDto);
    }

    @Override
    public Page<DisponibilidadResponseDto> findByMedicoIdAndDiaSemana(Long medicoId, DiaSemana diaSemana, Pageable pageable) {
        return disponibilidadRepository.findByMedicoIdAndDiaSemana(medicoId, diaSemana, pageable)
                .map(disponibilidadMapper::toResponseDto);
    }
}
