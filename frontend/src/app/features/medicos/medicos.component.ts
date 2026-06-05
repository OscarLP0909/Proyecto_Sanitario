import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { MedicoService } from '../../core/services/medico.service';
import { EspecialidadService } from '../../core/services/especialidad.service';
import { ToastService } from '../../core/services/toast.service';
import { Medico, medicoEspecialidad } from '../../core/models/medico.model';
import { Especialidad } from '../../core/models/especialidad.model';
import { SpinnerComponent } from '../../shared/spinner/spinner.component';

@Component({
  selector: 'app-medicos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SpinnerComponent],
  templateUrl: './medicos.component.html',
})
export class MedicosComponent implements OnInit {
  private svc = inject(MedicoService);
  private espSvc = inject(EspecialidadService);
  private toast = inject(ToastService);
  private fb = inject(FormBuilder);

  loading = signal(true);
  saving = signal(false);
  showModal = signal(false);
  editing = signal<Medico | null>(null);
  items = signal<Medico[]>([]);
  especialidades = signal<Especialidad[]>([]);
  searchTerm = signal('');
  page = signal(0);
  readonly pageSize = 10;

  // IDs de especialidades seleccionadas en el modal
  selectedEspIds = signal<number[]>([]);

  form = this.fb.group({
    name: ['', Validators.required],
    surname: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    nif: ['', Validators.required],
  });

  get filtered() {
    const t = this.searchTerm().toLowerCase();
    return this.items().filter(m =>
      `${m.name} ${m.surname} ${m.email} ${medicoEspecialidad(m)}`.toLowerCase().includes(t)
    );
  }

  get paginated() {
    const s = this.page() * this.pageSize;
    return this.filtered.slice(s, s + this.pageSize);
  }

  get totalPages() { return Math.ceil(this.filtered.length / this.pageSize); }

  ngOnInit() {
    this.load();
    this.espSvc.getAll().subscribe(e => this.especialidades.set(e));
  }

  load() {
    this.loading.set(true);
    this.svc.getAll().subscribe({
      next: items => { this.items.set(items); this.loading.set(false); },
      error: () => { this.toast.error('Error al cargar médicos'); this.loading.set(false); }
    });
  }

  openCreate() {
    this.editing.set(null);
    this.form.reset();
    this.selectedEspIds.set([]);
    this.showModal.set(true);
  }

  openEdit(item: Medico) {
    this.editing.set(item);
    this.form.patchValue({ name: item.name, surname: item.surname, email: item.email, nif: item.nif ?? '' });
    this.selectedEspIds.set(item.especialidades?.map(e => e.id!) ?? []);
    this.showModal.set(true);
  }

  toggleEsp(id: number) {
    const current = this.selectedEspIds();
    if (current.includes(id)) {
      this.selectedEspIds.set(current.filter(x => x !== id));
    } else {
      this.selectedEspIds.set([...current, id]);
    }
  }

  isEspSelected(id: number): boolean {
    return this.selectedEspIds().includes(id);
  }

  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    if (this.selectedEspIds().length === 0) { this.toast.error('Selecciona al menos una especialidad'); return; }
    this.saving.set(true);
    const v = this.form.value;

    if (this.editing()) {
      const req = { name: v.name!, surname: v.surname!, nif: v.nif!, especialidadesIds: this.selectedEspIds() };
      this.svc.update(this.editing()!.id!, req).subscribe({
        next: () => { this.toast.success('Médico actualizado'); this.showModal.set(false); this.load(); this.saving.set(false); },
        error: (e) => { this.toast.error(e?.error?.message ?? 'Error al guardar'); this.saving.set(false); }
      });
    } else {
      const req = { name: v.name!, surname: v.surname!, email: v.email!, nif: v.nif!, especialidadesIds: this.selectedEspIds() };
      this.svc.create(req).subscribe({
        next: () => { this.toast.success('Médico creado. Credenciales enviadas por email.'); this.showModal.set(false); this.load(); this.saving.set(false); },
        error: (e) => { this.toast.error(e?.error?.message ?? 'Error al guardar'); this.saving.set(false); }
      });
    }
  }

  delete(item: Medico) {
    if (!confirm(`¿Eliminar al médico ${item.name} ${item.surname}?`)) return;
    this.svc.delete(item.id!).subscribe({
      next: () => { this.toast.success('Médico eliminado'); this.load(); },
      error: () => this.toast.error('Error al eliminar')
    });
  }

  field(n: string) { return this.form.get(n)!; }
  isInvalid(n: string) { return this.field(n).invalid && this.field(n).touched; }
}
