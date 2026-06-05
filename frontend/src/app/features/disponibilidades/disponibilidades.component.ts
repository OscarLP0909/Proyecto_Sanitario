import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { DisponibilidadService } from '../../core/services/disponibilidad.service';
import { MedicoService } from '../../core/services/medico.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { Disponibilidad, DisponibilidadRequest, DIAS_SEMANA } from '../../core/models/disponibilidad.model';
import { Medico, medicoNombre } from '../../core/models/medico.model';
import { SpinnerComponent } from '../../shared/spinner/spinner.component';

@Component({
  selector: 'app-disponibilidades',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SpinnerComponent],
  templateUrl: './disponibilidades.component.html',
})
export class DisponibilidadesComponent implements OnInit {
  private svc = inject(DisponibilidadService);
  private medicoSvc = inject(MedicoService);
  private auth = inject(AuthService);
  private toast = inject(ToastService);
  private fb = inject(FormBuilder);

  loading = signal(true);
  saving = signal(false);
  showModal = signal(false);
  editing = signal<Disponibilidad | null>(null);
  items = signal<Disponibilidad[]>([]);
  medicos = signal<Medico[]>([]);
  currentMedicoId = signal<number | null>(null);
  readonly dias = DIAS_SEMANA;
  medicoNombre = medicoNombre;

  form = this.fb.group({
    medicoId: [null as number | null, Validators.required],
    diaSemana: ['', Validators.required],
    horaInicio: ['', Validators.required],
    horaFin: ['', Validators.required],
    duracionMinutos: [30],
  });

  ngOnInit() {
    this.load();
    if (this.auth.userRole() === 'ADMIN') {
      this.medicoSvc.getAll().subscribe(m => this.medicos.set(m));
    } else if (this.auth.userRole() === 'MEDICO') {
      this.medicoSvc.getMe().subscribe(m => this.currentMedicoId.set(m.id!));
    }
  }

  load() {
    this.loading.set(true);
    this.svc.getAll().subscribe({
      next: items => { this.items.set(items); this.loading.set(false); },
      error: () => { this.toast.error('Error al cargar disponibilidades'); this.loading.set(false); }
    });
  }

  openCreate() {
    this.editing.set(null);
    this.form.reset({ duracionMinutos: 30, medicoId: this.currentMedicoId() });
    this.showModal.set(true);
  }

  openEdit(item: Disponibilidad) {
    this.editing.set(item);
    this.form.patchValue({
      medicoId: item.medico?.id ?? null,
      diaSemana: item.diaSemana,
      horaInicio: item.horaInicio,
      horaFin: item.horaFin,
      duracionMinutos: item.duracionMinutos ?? 30,
    });
    this.showModal.set(true);
  }

  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const v = this.form.value;
    const req: DisponibilidadRequest = {
      medicoId: v.medicoId ?? this.currentMedicoId()!,
      diaSemana: v.diaSemana!,
      horaInicio: v.horaInicio!,
      horaFin: v.horaFin!,
      duracionMinutos: v.duracionMinutos ?? 30,
    };
    const obs = this.editing()
      ? this.svc.update(this.editing()!.id!, req)
      : this.svc.create(req);
    obs.subscribe({
      next: () => { this.toast.success(this.editing() ? 'Actualizado' : 'Creado'); this.showModal.set(false); this.load(); this.saving.set(false); },
      error: () => { this.toast.error('Error al guardar'); this.saving.set(false); }
    });
  }

  delete(item: Disponibilidad) {
    if (!confirm('¿Eliminar esta disponibilidad?')) return;
    this.svc.delete(item.id!).subscribe({
      next: () => { this.toast.success('Eliminado'); this.load(); },
      error: () => this.toast.error('Error al eliminar')
    });
  }

  field(n: string) { return this.form.get(n)!; }
  isInvalid(n: string) { return this.field(n).invalid && this.field(n).touched; }
  isAdmin() { return this.auth.userRole() === 'ADMIN'; }
}
