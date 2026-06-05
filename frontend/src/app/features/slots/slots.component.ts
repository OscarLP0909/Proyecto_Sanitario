import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { SlotService } from '../../core/services/slot.service';
import { CitaService } from '../../core/services/cita.service';
import { MedicoService } from '../../core/services/medico.service';
import { PacienteService } from '../../core/services/paciente.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { Slot, slotFecha, slotHora } from '../../core/models/slot.model';
import { Medico, medicoNombre, medicoEspecialidad } from '../../core/models/medico.model';
import { SpinnerComponent } from '../../shared/spinner/spinner.component';

@Component({
  selector: 'app-slots',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SpinnerComponent],
  templateUrl: './slots.component.html',
})
export class SlotsComponent implements OnInit {
  private slotSvc = inject(SlotService);
  private citaSvc = inject(CitaService);
  private medicoSvc = inject(MedicoService);
  private pacienteSvc = inject(PacienteService);
  private auth = inject(AuthService);
  private toast = inject(ToastService);
  private fb = inject(FormBuilder);

  loading = signal(false);
  booking = signal(false);
  showModal = signal(false);
  selectedSlot = signal<Slot | null>(null);
  slots = signal<Slot[]>([]);
  medicos = signal<Medico[]>([]);
  selectedMedicoId = signal<number | null>(null);
  currentPacienteId = signal<number | null>(null);

  slotFecha = slotFecha;
  slotHora = slotHora;
  medicoNombre = medicoNombre;
  medicoEspecialidad = medicoEspecialidad;

  bookForm = this.fb.group({ motivo: [''], notas: [''] });

  ngOnInit() {
    // Obtener ID del paciente actual para poder crear la cita
    this.pacienteSvc.getMe().subscribe({
      next: p => this.currentPacienteId.set(p.id!),
      error: () => {}
    });

    this.medicoSvc.getAll().subscribe(m => {
      this.medicos.set(m);
      if (m.length > 0) {
        this.selectedMedicoId.set(m[0].id!);
        this.buscarPorMedico(m[0].id!);
      }
    });
  }

  buscarPorMedico(medicoId: number) {
    this.selectedMedicoId.set(medicoId);
    this.loading.set(true);
    this.slotSvc.getDisponiblesByMedico(medicoId).subscribe({
      next: slots => { this.slots.set(slots); this.loading.set(false); },
      error: () => { this.toast.error('Error al cargar slots'); this.loading.set(false); }
    });
  }

  openBooking(slot: Slot) {
    this.selectedSlot.set(slot);
    this.bookForm.reset();
    this.showModal.set(true);
  }

  confirmarReserva() {
    const slot = this.selectedSlot();
    if (!slot) return;
    const motivo = this.bookForm.value.motivo?.trim();
    if (!motivo) { this.toast.error('El motivo es obligatorio'); return; }

    const pacienteId = this.currentPacienteId();
    if (!pacienteId) { this.toast.error('No se pudo obtener el perfil del paciente'); return; }

    this.booking.set(true);
    this.citaSvc.create({
      pacienteId,
      slotId: slot.id!,
      motivo,
      notas: this.bookForm.value.notas ?? '',
    }).subscribe({
      next: () => {
        this.toast.success('¡Cita reservada con éxito!');
        this.showModal.set(false);
        if (this.selectedMedicoId()) this.buscarPorMedico(this.selectedMedicoId()!);
        this.booking.set(false);
      },
      error: (e) => { this.toast.error(e?.error?.message ?? 'Error al reservar la cita'); this.booking.set(false); }
    });
  }
}
