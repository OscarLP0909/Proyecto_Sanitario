package org.gestion.proyecto_sanitario.medico.service.impl;

import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.medico.dto.request.SlotRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.response.SlotResponseDto;
import org.gestion.proyecto_sanitario.medico.mapper.SlotMapper;
import org.gestion.proyecto_sanitario.medico.model.Medico;
import org.gestion.proyecto_sanitario.medico.repository.MedicoRepository;
import org.gestion.proyecto_sanitario.medico.repository.SlotRepository;
import org.gestion.proyecto_sanitario.medico.service.SlotService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final SlotRepository slotRepository;
    private final SlotMapper slotMapper;
    private final MedicoRepository medicoRepository;


    @Override
    public SlotResponseDto crearSlot(SlotRequestDto dto) {
        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Medico no encontrado con id: " + dto.getMedicoId()));
        var slotEntity = slotMapper.toEntity(dto);
        slotEntity.setMedico(medico);
        var savedSlot = slotRepository.save(slotEntity);
        return slotMapper.toResponseDto(savedSlot);
    }

    @Override
    public Page<SlotResponseDto> findAll(Pageable pageable) {
        return slotRepository.findAll(pageable)
                .map(slotMapper::toResponseDto);
    }

    @Override
    public SlotResponseDto findById(Long id) {
        return slotRepository.findById(id)
                .map(slotMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Slot no encontrado con id: " + id));
    }

    @Override
    public SlotResponseDto updateSlot(Long id, SlotRequestDto dto) {
        var existingSlot = slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot no encontrado con id: " + id));

        existingSlot.setFechaHora(dto.getFechaHora());
        var updatedSlot = slotRepository.save(existingSlot);
        return slotMapper.toResponseDto(updatedSlot);
    }

    @Override
    public void deleteSlot(Long id) {
        if (!slotRepository.existsById(id)) {
            throw new RuntimeException("Slot no encontrado con id: " + id);
        }
        slotRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Page<SlotResponseDto> findSlotsByMedicoDisponibles(Long medicoId, Pageable pageable) {
        return slotRepository.findByMedicoIdAndDisponibleTrue(medicoId, pageable)
                .map(slotMapper::toResponseDto);
    }
}
