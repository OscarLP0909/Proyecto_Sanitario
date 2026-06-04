package org.gestion.proyecto_sanitario.medico;

import lombok.RequiredArgsConstructor;
import org.gestion.proyecto_sanitario.disponibilidad.repository.DisponibilidadRepository;
import org.gestion.proyecto_sanitario.medico.model.Slot;
import org.gestion.proyecto_sanitario.medico.repository.SlotRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlotScheduler {

    private final DisponibilidadRepository disponibilidadRepository;
    private final SlotRepository slotRepository;

    @Scheduled(cron = "0 0 2 * * ?") // Ejecutar cada día a medianoche a las 2:00 AM
    public void generarSlotsSemana() {
        // Obtener todas las disponibilidades de los médicos
        var disponibilidades = disponibilidadRepository.findAll();

        // Por cada disponibilidad, calcular la proxima fecha que corresponda a su DiaSemana y generar los slots correspondientes si no existen ya

        for (var disponibilidad : disponibilidades) {
            var medico = disponibilidad.getMedico();
            var diaSemana = disponibilidad.getDiaSemana();
            var horaInicio = disponibilidad.getHoraInicio();
            var horaFin = disponibilidad.getHoraFin();
            var duracionMinutos = disponibilidad.getDuracionMinutos();

            // Calcular la próxima fecha que corresponda al diaSemana
            var proximaFecha = java.time.LocalDate.now().with(java.time.temporal.TemporalAdjusters.nextOrSame(diaSemana.toTemporalDayOfWeek()));

            // Generar slots para esa fecha si no existen ya
            var hora = horaInicio;
            while (hora.isBefore(horaFin)) {
                var fechaHora = java.time.LocalDateTime.of(proximaFecha, hora);
                if (!slotRepository.existsByMedicoAndFechaHora(medico, fechaHora)) {
                    var slot = Slot.builder()
                            .medico(medico)
                            .fechaHora(fechaHora)
                            .disponible(true)
                            .build();
                    slotRepository.save(slot);
                }
                hora = hora.plusMinutes(duracionMinutos);
            }
        }

    }
}
