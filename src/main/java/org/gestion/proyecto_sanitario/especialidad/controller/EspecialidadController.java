package org.gestion.proyecto_sanitario.especialidad.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.especialidad.dto.request.EspecialidadRequestDto;
import org.gestion.proyecto_sanitario.especialidad.dto.response.EspecialidadResponseDto;
import org.gestion.proyecto_sanitario.especialidad.service.EspecialidadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/especialidades")
@RestController
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EspecialidadResponseDto> crearEspecialidad(@Valid @RequestBody EspecialidadRequestDto dto) {
        var response = especialidadService.crearEspecialidad(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<EspecialidadResponseDto>> findAll(Pageable pageable) {
        var response = especialidadService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDto> findById(@PathVariable Long id) {
        var response = especialidadService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EspecialidadResponseDto> updateEspecialidad(@PathVariable Long id, @Valid @RequestBody EspecialidadRequestDto dto) {
        return ResponseEntity.ok(especialidadService.updateEspecialidad(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEspecialidad(@PathVariable Long id) {
        especialidadService.deleteEspecialidad(id);
        return ResponseEntity.noContent().build();
    }
}
