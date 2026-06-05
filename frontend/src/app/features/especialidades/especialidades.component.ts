import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { EspecialidadService } from '../../core/services/especialidad.service';
import { ToastService } from '../../core/services/toast.service';
import { Especialidad } from '../../core/models/especialidad.model';
import { SpinnerComponent } from '../../shared/spinner/spinner.component';

@Component({
  selector: 'app-especialidades',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SpinnerComponent],
  templateUrl: './especialidades.component.html',
})
export class EspecialidadesComponent implements OnInit {
  private svc = inject(EspecialidadService);
  private toast = inject(ToastService);
  private fb = inject(FormBuilder);

  loading = signal(true);
  saving = signal(false);
  showModal = signal(false);
  editing = signal<Especialidad | null>(null);
  items = signal<Especialidad[]>([]);
  searchTerm = signal('');

  form = this.fb.group({
    nombre: ['', Validators.required],
    descripcion: [''],
  });

  get filtered() {
    const t = this.searchTerm().toLowerCase();
    return this.items().filter(e => e.nombre.toLowerCase().includes(t) || (e.descripcion ?? '').toLowerCase().includes(t));
  }

  ngOnInit() { this.load(); }

  load() {
    this.loading.set(true);
    this.svc.getAll().subscribe({
      next: items => { this.items.set(items); this.loading.set(false); },
      error: () => { this.toast.error('Error al cargar especialidades'); this.loading.set(false); }
    });
  }

  openCreate() {
    this.editing.set(null);
    this.form.reset();
    this.showModal.set(true);
  }

  openEdit(item: Especialidad) {
    this.editing.set(item);
    this.form.patchValue(item);
    this.showModal.set(true);
  }

  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const v = this.form.value;
    const req: Especialidad = { nombre: v.nombre!, descripcion: v.descripcion ?? '' };
    const obs = this.editing()
      ? this.svc.update(this.editing()!.id!, req)
      : this.svc.create(req);
    obs.subscribe({
      next: () => { this.toast.success(this.editing() ? 'Especialidad actualizada' : 'Especialidad creada'); this.showModal.set(false); this.load(); this.saving.set(false); },
      error: () => { this.toast.error('Error al guardar'); this.saving.set(false); }
    });
  }

  delete(item: Especialidad) {
    if (!confirm(`¿Eliminar la especialidad "${item.nombre}"?`)) return;
    this.svc.delete(item.id!).subscribe({
      next: () => { this.toast.success('Especialidad eliminada'); this.load(); },
      error: () => this.toast.error('Error al eliminar')
    });
  }

  field(n: string) { return this.form.get(n)!; }
  isInvalid(n: string) { return this.field(n).invalid && this.field(n).touched; }
}
