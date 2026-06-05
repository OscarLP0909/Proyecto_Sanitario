package org.gestion.proyecto_sanitario.cita.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.cita.dto.request.CambiarEstadoRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.request.CitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.request.UpdateCitaRequestDto;
import org.gestion.proyecto_sanitario.cita.dto.response.CitaResponseDto;
import org.gestion.proyecto_sanitario.cita.model.EstadoCita;
import org.gestion.proyecto_sanitario.cita.service.CitaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('PACIENTE', 'ADMIN')")
    @Transactional
    public ResponseEntity<CitaResponseDto> crearCita(@Valid @RequestBody CitaRequestDto dto) {
        var response = citaService.crearCita(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Transactional
    public ResponseEntity<Page<CitaResponseDto>> findAll(Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            return ResponseEntity.ok(citaService.findAll(pageable));
        } else {
            return ResponseEntity.ok(citaService.findBySlotMedicoUserEmail(email, pageable));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    @Transactional
    public ResponseEntity<CitaResponseDto> findById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        var cita = citaService.findById(id);

        assert role != null;

        if (role.equals("ROLE_MEDICO") && !cita.getSlot().getMedico().getEmail().equals(email)) {
            return ResponseEntity.status(403).build();
        }
        if (role.equals("ROLE_PACIENTE") && !cita.getPaciente().getEmail().equals(email)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(cita);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    @Transactional
    public ResponseEntity<CitaResponseDto> updateCita(@PathVariable Long id, @Valid @RequestBody UpdateCitaRequestDto dto) {
        return ResponseEntity.ok(citaService.updateCita(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        citaService.deleteCita(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Transactional
    public ResponseEntity<Page<CitaResponseDto>> findbyPacienteId(@PathVariable Long pacienteId, Pageable pageable) {
        var response = citaService.findbyPacienteId(pacienteId, pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    @Transactional
    public ResponseEntity<CitaResponseDto> cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambiarEstadoRequestDto dto) {
        return ResponseEntity.ok(citaService.cambiarEstado(id, dto.getEstado()));
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('PACIENTE', 'ADMIN')")
    @Transactional
    public ResponseEntity<CitaResponseDto> cancelarCita(@PathVariable Long id) {
        var response = citaService.cancelarCita(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    @Transactional
    public ResponseEntity<Page<CitaResponseDto>> findByEstado(@PathVariable EstadoCita estado, Pageable pageable) {
        var response = citaService.findByEstado(estado, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mis-citas")
    @PreAuthorize("hasRole('PACIENTE')")
    @Transactional
    public ResponseEntity<Page<CitaResponseDto>> misCitas(Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        var response = citaService.findByPacienteUserEmail(userDetails.getUsername(), pageable);
        return ResponseEntity.ok(response);
    }
}
