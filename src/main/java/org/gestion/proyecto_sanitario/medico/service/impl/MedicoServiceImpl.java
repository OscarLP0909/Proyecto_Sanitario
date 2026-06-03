package org.gestion.proyecto_sanitario.medico.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.auth.model.Role;
import org.gestion.proyecto_sanitario.auth.model.User;
import org.gestion.proyecto_sanitario.auth.repository.UserRepository;
import org.gestion.proyecto_sanitario.especialidad.model.Especialidad;
import org.gestion.proyecto_sanitario.especialidad.repository.EspecialidadRepository;
import org.gestion.proyecto_sanitario.medico.dto.request.MedicoRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.request.MedicoUpdateRequestDto;
import org.gestion.proyecto_sanitario.medico.dto.response.MedicoResponseDto;
import org.gestion.proyecto_sanitario.medico.mapper.MedicoMapper;
import org.gestion.proyecto_sanitario.medico.model.Medico;
import org.gestion.proyecto_sanitario.medico.repository.MedicoRepository;
import org.gestion.proyecto_sanitario.medico.service.MedicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;
    private final UserRepository userRepository;
    private final EspecialidadRepository especialidadRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private void enviarEmailCredenciales(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Bienvenido al sistema sanitario");
        message.setText("Sus credenciales de acceso:\nEmail: " + email + "\nContraseña temporal: " + password);
        mailSender.send(message);
    }


    @Override
    public MedicoResponseDto crearMedico(MedicoRequestDto dto) {
        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }
        String passTemporal = UUID.randomUUID().toString().substring(0, 8);
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(passTemporal))
                .role(Role.MEDICO)
                .activo(true)
                .build();
        userRepository.save(user);
        Medico medico = medicoMapper.toEntity(dto);
        medico.setUser(user);
        List<Especialidad> especialidades = especialidadRepository.findAllById(dto.getEspecialidadesIds());
        medico.setEspecialidades(especialidades);
        var saved = medicoRepository.save(medico);
        enviarEmailCredenciales(dto.getEmail(), passTemporal);
        return medicoMapper.toResponseDto(saved);
    }

    @Override
    public Page<MedicoResponseDto> findAll(Pageable pageable) {
        return medicoRepository.findAll(pageable)
                .map(medicoMapper::toResponseDto);
    }

    @Override
    public MedicoResponseDto findbyId(Long id) {
        return medicoRepository.findById(id)
                .map(medicoMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
    }

    @Transactional
    @Override
    public MedicoResponseDto updateMedico(Long id, MedicoUpdateRequestDto dto) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));

        if (dto.getName() != null) medico.setName(dto.getName());
        if (dto.getSurname() != null) medico.setSurname(dto.getSurname());
        if (dto.getNif() != null) medico.setNif(dto.getNif());
        if (dto.getEspecialidadesIds() != null && !dto.getEspecialidadesIds().isEmpty()) {
            List<Especialidad> especialidades = especialidadRepository.findAllById(dto.getEspecialidadesIds());
            medico.setEspecialidades(especialidades);
        }

        return medicoMapper.toResponseDto(medicoRepository.save(medico));
    }


    @Override
    public void deleteMedico(Long id) {
            if(!medicoRepository.existsById(id)) {
                throw new IllegalArgumentException("Medico no encontrado");
            }
            medicoRepository.deleteById(id);
    }

    @Override
    public Page<MedicoResponseDto> findByEspecialidadID(Long especialidadId, Pageable pageable) {
        especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));
        return medicoRepository.findByEspecialidadesId(especialidadId, pageable)
                .map(medicoMapper::toResponseDto);
    }

    public MedicoResponseDto findByNif(String nif) {
        return medicoRepository.findByNif(nif)
                .map(medicoMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
    }
}
