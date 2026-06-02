package org.gestion.proyecto_sanitario.medico.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.medico.dto.request.SlotRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.response.SlotResponseDto;
import org.gestion.proyecto_sanitario.medico.service.SlotService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class SlotController {

    private final SlotService slotService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Page<SlotResponseDto>> findAll(Pageable pageable) {
        var response = slotService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<SlotResponseDto> findById(@PathVariable Long id) {
        var response = slotService.findById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        slotService.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medico/{medicoId}/disponibles")
    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
    public ResponseEntity<Page<SlotResponseDto>> findSlotsByMedicoDisponibles(@PathVariable Long medicoId, Pageable pageable) {
        var response = slotService.findSlotsByMedicoDisponibles(medicoId, pageable);
        return ResponseEntity.ok(response);
    }
}
