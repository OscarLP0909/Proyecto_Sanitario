package org.gestion.proyecto_sanitario.especialidad.service.impl;

import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.especialidad.dto.request.EspecialidadRequestDto;
import org.gestion.proyecto_sanitario.especialidad.dto.response.EspecialidadResponseDto;
import org.gestion.proyecto_sanitario.especialidad.mapper.EspecialidadMapper;
import org.gestion.proyecto_sanitario.especialidad.model.Especialidad;
import org.gestion.proyecto_sanitario.especialidad.repository.EspecialidadRepository;
import org.gestion.proyecto_sanitario.especialidad.service.EspecialidadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final EspecialidadMapper especialidadMapper;


    @Override
    public EspecialidadResponseDto crearEspecialidad(EspecialidadRequestDto dto) {
        if(especialidadRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe una especialidad con ese nombre");
        }
        var especialidad = especialidadMapper.toEntity(dto);
        var saved = especialidadRepository.save(especialidad);
        return especialidadMapper.toResponseDto(saved);
    }

    @Override
    public Page<EspecialidadResponseDto> findAll(Pageable pageable) {
        return especialidadRepository.findAll(pageable)
                .map(especialidadMapper::toResponseDto);
    }

    @Override
    public EspecialidadResponseDto findById(Long id) {
        return especialidadRepository.findById(id)
                .map(especialidadMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));
    }

    @Override
    public EspecialidadResponseDto updateEspecialidad(Long id, EspecialidadRequestDto dto) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        especialidad.setNombre(dto.getNombre());
        var updated = especialidadRepository.save(especialidad);
        return especialidadMapper.toResponseDto(updated);

    }

    @Override
    public void deleteEspecialidad(Long id) {
        if(!especialidadRepository.existsById(id)) {
            throw new IllegalArgumentException("Especialidad no encontrada");
        }
        especialidadRepository.deleteById(id);
    }

    @Override
    public EspecialidadResponseDto findByNombre(String nombre) {
        return especialidadRepository.findByNombre(nombre)
                .map(especialidadMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));
    }
}
