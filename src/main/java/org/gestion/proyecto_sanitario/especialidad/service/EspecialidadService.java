package org.gestion.proyecto_sanitario.especialidad.service;

import org.gestion.proyecto_sanitario.especialidad.dto.request.EspecialidadRequestDto;
import org.gestion.proyecto_sanitario.especialidad.dto.response.EspecialidadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EspecialidadService {

    EspecialidadResponseDto crearEspecialidad(EspecialidadRequestDto dto);

    Page<EspecialidadResponseDto> findAll(Pageable pageable);

    EspecialidadResponseDto findById(Long id);

    EspecialidadResponseDto updateEspecialidad(Long id, EspecialidadRequestDto dto);

    void deleteEspecialidad(Long id);

    EspecialidadResponseDto findByNombre(String nombre);


}
