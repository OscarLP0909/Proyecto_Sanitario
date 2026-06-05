import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CitaService } from '../../core/services/cita.service';
import { ToastService } from '../../core/services/toast.service';
import { Cita, citaMedicoNombre, citaEspecialidad, citaFecha, citaHora } from '../../core/models/cita.model';
import { SpinnerComponent } from '../../shared/spinner/spinner.component';

@Component({
  selector: 'app-mis-citas',
  standalone: true,
  imports: [CommonModule, RouterModule, SpinnerComponent],
  templateUrl: './mis-citas.component.html',
})
export class MisCitasComponent implements OnInit {
  private svc = inject(CitaService);
  private toast = inject(ToastService);

  loading = signal(true);
  items = signal<Cita[]>([]);
  filterEstado = signal('');
  readonly estados = ['PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'COMPLETADA'];

  citaMedicoNombre = citaMedicoNombre;
  citaEspecialidad = citaEspecialidad;
  citaFecha = citaFecha;
  citaHora = citaHora;

  get filtered() {
    const e = this.filterEstado();
    return this.items().filter(c => !e || c.estado === e);
  }

  ngOnInit() { this.load(); }

  load() {
    this.loading.set(true);
    this.svc.getMisCitas().subscribe({
      next: items => { this.items.set(items); this.loading.set(false); },
      error: () => { this.toast.error('Error al cargar citas'); this.loading.set(false); }
    });
  }

  cancelar(item: Cita) {
    if (!confirm('¿Cancelar esta cita?')) return;
    this.svc.cancelar(item.id!).subscribe({
      next: () => { this.toast.success('Cita cancelada'); this.load(); },
      error: () => this.toast.error('Error al cancelar')
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
}
