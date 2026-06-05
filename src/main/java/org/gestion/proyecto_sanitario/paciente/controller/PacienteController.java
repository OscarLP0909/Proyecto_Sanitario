package org.gestion.proyecto_sanitario.paciente.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.paciente.dto.request.PacienteRequestDto;
import org.gestion.proyecto_sanitario.paciente.dto.response.PacienteResponseDto;
import org.gestion.proyecto_sanitario.paciente.service.PacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    @Transactional
    public ResponseEntity<PacienteResponseDto> crearPaciente(@Valid @RequestBody PacienteRequestDto dto) {
        var response = pacienteService.crearPaciente(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Transactional
    public ResponseEntity<Page<PacienteResponseDto>> findAll(Pageable pageable) {
        var response = pacienteService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Transactional
    public ResponseEntity<PacienteResponseDto> findById(@PathVariable Long id) {
        var response = pacienteService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PACIENTE')")
    @Transactional
    public ResponseEntity<PacienteResponseDto> updatePaciente(@PathVariable Long id, @Valid @RequestBody PacienteRequestDto dto) {
        return ResponseEntity.ok(pacienteService.updatePaciente(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteService.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nif/{nif}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Transactional
    public ResponseEntity<PacienteResponseDto> findByNif(@PathVariable String nif) {
        var response = pacienteService.findByNif(nif);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    @Transactional
    public ResponseEntity<PacienteResponseDto> me(@AuthenticationPrincipal UserDetails userDetails) {
        var response = pacienteService.findMe(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
