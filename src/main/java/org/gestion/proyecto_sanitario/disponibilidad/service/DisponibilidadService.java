package org.gestion.proyecto_sanitario.disponibilidad.service;

import org.gestion.proyecto_sanitario.disponibilidad.dto.request.DisponibilidadRequestDto;
import org.gestion.proyecto_sanitario.disponibilidad.dto.response.DisponibilidadResponseDto;
import org.gestion.proyecto_sanitario.disponibilidad.model.DiaSemana;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DisponibilidadService {

    DisponibilidadResponseDto crearDisponibilidad(DisponibilidadRequestDto dto);

    Page<DisponibilidadResponseDto> findAll(Pageable pageable);

    DisponibilidadResponseDto findById(Long id);

    DisponibilidadResponseDto updateDisponibilidad(Long id, DisponibilidadRequestDto dto);

    void deleteDisponibilidad(Long id);

    Page<DisponibilidadResponseDto> findByMedicoId(Long medicoId, Pageable pageable);

    Page<DisponibilidadResponseDto> findbyDiaSemana(DiaSemana diaSemana, Pageable pageable);

    Page<DisponibilidadResponseDto> findByMedicoIdAndDiaSemana(Long medicoId, DiaSemana diaSemana, Pageable pageable);





}
