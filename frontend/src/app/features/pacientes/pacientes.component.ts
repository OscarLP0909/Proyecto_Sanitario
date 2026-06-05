import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { PacienteService } from '../../core/services/paciente.service';
import { ToastService } from '../../core/services/toast.service';
import { Paciente } from '../../core/models/paciente.model';

export function formatFecha(fecha: string): string {
  if (!fecha) return '—';
  const [y, m, d] = fecha.split('-');
  return d && m && y ? `${d}/${m}/${y}` : fecha;
}
import { SpinnerComponent } from '../../shared/spinner/spinner.component';

@Component({
  selector: 'app-pacientes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SpinnerComponent],
  templateUrl: './pacientes.component.html',
})
export class PacientesComponent implements OnInit {
  formatFecha = formatFecha;
  private svc = inject(PacienteService);
  private toast = inject(ToastService);
  private fb = inject(FormBuilder);

  loading = signal(true);
  saving = signal(false);
  showModal = signal(false);
  editing = signal<Paciente | null>(null);
  items = signal<Paciente[]>([]);
  searchTerm = signal('');
  page = signal(0);
  readonly pageSize = 10;

  form = this.fb.group({
    name: ['', Validators.required],
    surname: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: [''],
    nif: ['', Validators.required],
    fechaNacimiento: ['', Validators.required],
  });

  get filtered() {
    const t = this.searchTerm().toLowerCase();
    return this.items().filter(p =>
      `${p.name} ${p.surname} ${p.email} ${p.nif}`.toLowerCase().includes(t)
    );
  }

  get paginated() {
    const s = this.page() * this.pageSize;
    return this.filtered.slice(s, s + this.pageSize);
  }

  get totalPages() { return Math.ceil(this.filtered.length / this.pageSize); }

  ngOnInit() { this.load(); }

  load() {
    this.loading.set(true);
    this.svc.getAll().subscribe({
      next: items => { this.items.set(items); this.loading.set(false); },
      error: () => { this.toast.error('Error al cargar pacientes'); this.loading.set(false); }
    });
  }

  openCreate() {
    this.editing.set(null);
    this.form.reset();
    this.form.get('password')?.setValidators([Validators.required, Validators.minLength(6)]);
    this.form.get('password')?.updateValueAndValidity();
    this.showModal.set(true);
  }

  openEdit(item: Paciente) {
    this.editing.set(item);
    this.form.patchValue({ ...item, password: '' });
    this.form.get('password')?.clearValidators();
    this.form.get('password')?.updateValueAndValidity();
    this.showModal.set(true);
  }

  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const v = this.form.value;
    const req = { name: v.name!, surname: v.surname!, email: v.email!, nif: v.nif!, fechaNacimiento: v.fechaNacimiento!, ...(v.password ? { password: v.password } : {}) };
    const obs = this.editing()
      ? this.svc.update(this.editing()!.id!, req)
      : this.svc.create(req);
    obs.subscribe({
      next: () => { this.toast.success(this.editing() ? 'Paciente actualizado' : 'Paciente creado'); this.showModal.set(false); this.load(); this.saving.set(false); },
      error: () => { this.toast.error('Error al guardar'); this.saving.set(false); }
    });
  }

  delete(item: Paciente) {
    if (!confirm(`¿Eliminar al paciente ${item.name} ${item.surname}?`)) return;
    this.svc.delete(item.id!).subscribe({
      next: () => { this.toast.success('Paciente eliminado'); this.load(); },
      error: () => this.toast.error('Error al eliminar')
    });
  }

  field(n: string) { return this.form.get(n)!; }
  isInvalid(n: string) { return this.field(n).invalid && this.field(n).touched; }
}
