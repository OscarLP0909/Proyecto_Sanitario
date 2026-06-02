package org.gestion.proyecto_sanitario.cita.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.cita.dto.request.CitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.request.UpdateCitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.response.CitaResponseDto;
import org.gestion.proyecto_sanitario.cita.model.EstadoCita;
import org.gestion.proyecto_sanitario.cita.service.CitaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('PACIENTE', 'ADMIN')")
    public ResponseEntity<CitaResponseDto> crearCita(@Valid @RequestBody CitaRequestDto dto) {
        var response = citaService.crearCita(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    //TODO: filtrar por médico autenticado
    public ResponseEntity<Page<CitaResponseDto>> findAll(Pageable pageable) {
        var response = citaService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    //TODO: validar que el paciente o médico autenticado tenga acceso a sus citas
    public ResponseEntity<CitaResponseDto> findById(@PathVariable Long id) {
        var response = citaService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    public ResponseEntity<CitaResponseDto> updateCita(@PathVariable Long id, @Valid @RequestBody UpdateCitaRequestDto dto) {
        return ResponseEntity.ok(citaService.updateCita(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        citaService.deleteCita(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Page<CitaResponseDto>> findbyPacienteId(@PathVariable Long pacienteId, Pageable pageable) {
        var response = citaService.findbyPacienteId(pacienteId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('PACIENTE', 'ADMIN')")
    public ResponseEntity<CitaResponseDto> cancelarCita(@PathVariable Long id) {
        var response = citaService.cancelarCita(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<Page<CitaResponseDto>> findByEstado(@PathVariable EstadoCita estado, Pageable pageable) {
        var response = citaService.findByEstado(estado, pageable);
        return ResponseEntity.ok(response);
    }
}
