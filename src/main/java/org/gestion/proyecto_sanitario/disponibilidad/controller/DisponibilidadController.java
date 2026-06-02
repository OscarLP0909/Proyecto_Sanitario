package org.gestion.proyecto_sanitario.disponibilidad.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.disponibilidad.dto.request.DisponibilidadRequestDto;
import org.gestion.proyecto_sanitario.disponibilidad.dto.response.DisponibilidadResponseDto;
import org.gestion.proyecto_sanitario.disponibilidad.model.DiaSemana;
import org.gestion.proyecto_sanitario.disponibilidad.service.DisponibilidadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disponibilidades")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DisponibilidadResponseDto> crearDisponibilidad(@Valid @RequestBody DisponibilidadRequestDto dto) {
        var response = disponibilidadService.crearDisponibilidad(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Page<DisponibilidadResponseDto>> findAll(Pageable pageable) {
        var response = disponibilidadService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadResponseDto> findById(@PathVariable Long id) {
        var response = disponibilidadService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisponibilidadResponseDto> updateDisponibilidad(@PathVariable Long id, @Valid @RequestBody DisponibilidadRequestDto dto) {
        var response = disponibilidadService.updateDisponibilidad(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisponibilidad(@PathVariable Long id) {
        disponibilidadService.deleteDisponibilidad(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<Page<DisponibilidadResponseDto>> findByMedicoId(@PathVariable Long medicoId, Pageable pageable) {
        var response = disponibilidadService.findByMedicoId(medicoId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/diaSemana/{diaSemana}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Page<DisponibilidadResponseDto>> findByDiaSemana(@PathVariable DiaSemana diaSemana, Pageable pageable) {
        var response = disponibilidadService.findbyDiaSemana(diaSemana, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/medico/{medicoId}/diaSemana/{diaSemana}")
    public ResponseEntity<Page<DisponibilidadResponseDto>> findByMedicoIdAndDiaSemana(@PathVariable Long medicoId, @PathVariable DiaSemana diaSemana, Pageable pageable) {
        var response = disponibilidadService.findByMedicoIdAndDiaSemana(medicoId, diaSemana, pageable);
        return ResponseEntity.ok(response);
    }
}
