import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CitaService } from '../../core/services/cita.service';
import { PacienteService } from '../../core/services/paciente.service';
import { SlotService } from '../../core/services/slot.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { Cita, EstadoCita, citaPacienteNombre, citaMedicoNombre, citaEspecialidad, citaFecha, citaHora } from '../../core/models/cita.model';
import { Paciente } from '../../core/models/paciente.model';
import { Slot, slotFecha, slotHora } from '../../core/models/slot.model';
import { medicoNombre } from '../../core/models/medico.model';
import { SpinnerComponent } from '../../shared/spinner/spinner.component';

@Component({
  selector: 'app-citas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SpinnerComponent],
  templateUrl: './citas.component.html',
})
export class CitasComponent implements OnInit {
  private svc = inject(CitaService);
  private pacienteSvc = inject(PacienteService);
  private slotSvc = inject(SlotService);
  private auth = inject(AuthService);
  private toast = inject(ToastService);
  private fb = inject(FormBuilder);

  loading = signal(true);
  saving = signal(false);
  savingResultado = signal(false);

  showModal = signal(false);
  showResultadoModal = signal(false);
  showDetalle = signal(false);

  editing = signal<Cita | null>(null);
  editingCita = signal<Cita | null>(null);
  detalleCita = signal<Cita | null>(null);

  items = signal<Cita[]>([]);
  pacientes = signal<Paciente[]>([]);
  slots = signal<Slot[]>([]);
  searchTerm = signal('');
  filterEstado = signal<string>('');
  page = signal(0);
  readonly pageSize = 10;
  readonly estados: EstadoCita[] = ['PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'COMPLETADA'];

  citaPacienteNombre = citaPacienteNombre;
  citaMedicoNombre = citaMedicoNombre;
  citaEspecialidad = citaEspecialidad;
  citaFecha = citaFecha;
  citaHora = citaHora;
  slotLabel = (s: Slot) => `${medicoNombre(s.medico)} — ${slotFecha(s)} ${slotHora(s)}`;

  form = this.fb.group({
    pacienteId: [null as number | null, Validators.required],
    slotId: [null as number | null, Validators.required],
    motivo: ['', Validators.required],
    notas: [''],
  });

  resultadoForm = this.fb.group({
    estado: ['COMPLETADA' as EstadoCita, Validators.required],
    notas: [''],
  });

  isMedico() { return this.auth.userRole() === 'MEDICO'; }
  isAdmin() { return this.auth.userRole() === 'ADMIN'; }

  esPasada(cita: Cita): boolean {
    if (!cita.slot?.fechaHora) return false;
    return new Date(cita.slot.fechaHora) <= new Date();
  }

  get filtered() {
    const t = this.searchTerm().toLowerCase();
    const e = this.filterEstado();
    return this.items().filter(c => {
      const matchText = `${citaPacienteNombre(c)} ${citaMedicoNombre(c)} ${citaFecha(c)}`.toLowerCase().includes(t);
      return matchText && (!e || c.estado === e);
    });
  }

  get paginated() {
    const s = this.page() * this.pageSize;
    return this.filtered.slice(s, s + this.pageSize);
  }

  get totalPages() { return Math.ceil(this.filtered.length / this.pageSize); }

  ngOnInit() {
    this.load();
    if (this.isAdmin()) {
      this.pacienteSvc.getAll().subscribe(p => this.pacientes.set(p));
      this.slotSvc.getAll().subscribe(s => this.slots.set(s.filter(slot => slot.disponible !== false)));
    }
  }

  load() {
    this.loading.set(true);
    this.svc.getAll().subscribe({
      next: items => { this.items.set(items); this.loading.set(false); },
      error: () => { this.toast.error('Error al cargar citas'); this.loading.set(false); }
    });
  }

  openDetalle(item: Cita) {
    this.detalleCita.set(item);
    this.showDetalle.set(true);
  }

  // --- ADMIN: crear/editar cita completa ---
  openCreate() {
    this.editing.set(null);
    this.form.reset();
    this.showModal.set(true);
  }

  openEdit(item: Cita) {
    this.editing.set(item);
    this.form.patchValue({
      pacienteId: item.paciente?.id ?? null,
      slotId: item.slot?.id ?? null,
      motivo: item.motivo ?? '',
      notas: item.notas ?? '',
    });
    this.showModal.set(true);
  }

  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const v = this.form.value;
    const req = {
      pacienteId: v.pacienteId!,
      slotId: v.slotId!,
      motivo: v.motivo ?? '',
      notas: v.notas ?? '',
    };
    const obs = this.editing()
      ? this.svc.update(this.editing()!.id!, req)
      : this.svc.create(req);
    obs.subscribe({
      next: () => { this.toast.success(this.editing() ? 'Cita actualizada' : 'Cita creada'); this.showModal.set(false); this.load(); this.saving.set(false); },
      error: (e) => { this.toast.error(e?.error?.message ?? 'Error al guardar'); this.saving.set(false); }
    });
  }

  // --- MEDICO: aceptar cita pendiente ---
  confirmar(item: Cita) {
    this.svc.cambiarEstado(item.id!, 'CONFIRMADA').subscribe({
      next: () => { this.toast.success('Cita confirmada'); this.load(); },
      error: () => this.toast.error('Error al confirmar la cita')
    });
  }

  // --- MEDICO: cancelar cita ---
  cancelarMedico(item: Cita) {
    if (!confirm('¿Cancelar esta cita?')) return;
    this.svc.cambiarEstado(item.id!, 'CANCELADA').subscribe({
      next: () => { this.toast.success('Cita cancelada'); this.load(); },
      error: () => this.toast.error('Error al cancelar la cita')
    });
  }

  // --- MEDICO: editar resultado (fecha pasada) ---
  openEditarResultado(item: Cita) {
    this.editingCita.set(item);
    this.resultadoForm.patchValue({
      estado: item.estado === 'CONFIRMADA' ? 'COMPLETADA' : item.estado,
      notas: item.notas ?? '',
    });
    this.showResultadoModal.set(true);
  }

  guardarResultado() {
    const cita = this.editingCita();
    if (!cita) return;
    this.savingResultado.set(true);
    const v = this.resultadoForm.value;
    this.svc.update(cita.id!, {
      estado: v.estado!,
      notas: v.notas ?? '',
      motivo: cita.motivo ?? '',
    }).subscribe({
      next: () => { this.toast.success('Resultado guardado'); this.showResultadoModal.set(false); this.load(); this.savingResultado.set(false); },
      error: () => { this.toast.error('Error al guardar el resultado'); this.savingResultado.set(false); }
    });
  }

  // --- Cancelar/Eliminar (ADMIN) ---
  cancelar(item: Cita) {
    if (!confirm('¿Cancelar esta cita?')) return;
    this.svc.cancelar(item.id!).subscribe({
      next: () => { this.toast.success('Cita cancelada'); this.load(); },
      error: () => this.toast.error('Error al cancelar')
    });
  }

  delete(item: Cita) {
    if (!confirm('¿Eliminar esta cita?')) return;
    this.svc.delete(item.id!).subscribe({
      next: () => { this.toast.success('Cita eliminada'); this.load(); },
      error: () => this.toast.error('Error al eliminar')
    });
  }

  estadoBadgeClass(estado: string): string {
    const map: Record<string, string> = {
      PENDIENTE: 'bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400',
      CONFIRMADA: 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
      CANCELADA: 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400',
      COMPLETADA: 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-400',
    };
    return map[estado] ?? 'bg-gray-100 text-gray-700';
  }

  field(n: string) { return this.form.get(n)!; }
  isInvalid(n: string) { return this.field(n).invalid && this.field(n).touched; }
}
