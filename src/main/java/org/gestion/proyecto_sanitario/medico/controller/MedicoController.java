package org.gestion.proyecto_sanitario.medico.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.medico.dto.request.MedicoRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.request.MedicoUpdateRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.response.MedicoResponseDto;
import org.gestion.proyecto_sanitario.medico.service.MedicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponseDto> crearMedico(@Valid @RequestBody MedicoRequestDto dto) {
        var response = medicoService.crearMedico(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    @Transactional
    public ResponseEntity<Page<MedicoResponseDto>> findAll(Pageable pageable) {
        var response = medicoService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    @Transactional
    public ResponseEntity<MedicoResponseDto> findById(@PathVariable Long id) {
        var response = medicoService.findbyId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponseDto> updateMedico(@PathVariable Long id, @Valid @RequestBody MedicoUpdateRequestDto dto) {
        return ResponseEntity.ok(medicoService.updateMedico(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedico(@PathVariable Long id) {
        medicoService.deleteMedico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/especialidad/{especialidadId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    @Transactional
    public ResponseEntity<Page<MedicoResponseDto>> findByEspecialidadID(@PathVariable Long especialidadId, Pageable pageable) {
        var response = medicoService.findByEspecialidadID(especialidadId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nif/{nif}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<MedicoResponseDto> findByNif(@PathVariable String nif) {
        var response = medicoService.findByNif(nif);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<MedicoResponseDto> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        var response = medicoService.findMe(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
