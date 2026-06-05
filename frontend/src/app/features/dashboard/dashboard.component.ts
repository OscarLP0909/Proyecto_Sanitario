import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { MedicoService } from '../../core/services/medico.service';
import { PacienteService } from '../../core/services/paciente.service';
import { CitaService } from '../../core/services/cita.service';
import { Cita, citaPacienteNombre, citaMedicoNombre, citaEspecialidad, citaFecha, citaHora } from '../../core/models/cita.model';
import { SpinnerComponent } from '../../shared/spinner/spinner.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, SpinnerComponent],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  auth = inject(AuthService);
  private medicoSvc = inject(MedicoService);
  private pacienteSvc = inject(PacienteService);
  private citaSvc = inject(CitaService);

  loading = signal(true);
  totalMedicos = signal(0);
  totalPacientes = signal(0);
  citas = signal<Cita[]>([]);
  misCitas = signal<Cita[]>([]);

  // Helpers para templates
  citaPacienteNombre = citaPacienteNombre;
  citaMedicoNombre = citaMedicoNombre;
  citaEspecialidad = citaEspecialidad;
  citaFecha = citaFecha;
  citaHora = citaHora;

  get role() { return this.auth.userRole(); }

  get citasHoy(): Cita[] {
    const today = new Date().toISOString().split('T')[0];
    return this.citas().filter(c => citaFecha(c) === today);
  }

  get citasPendientes(): Cita[] {
    return this.citas().filter(c => c.estado === 'PENDIENTE');
  }

  ngOnInit() {
    if (this.role === 'ADMIN') this.loadAdmin();
    else if (this.role === 'MEDICO') this.loadMedico();
    else if (this.role === 'PACIENTE') this.loadPaciente();
    else this.loading.set(false);
  }

  private loadAdmin() {
    this.medicoSvc.getAll().subscribe(m => this.totalMedicos.set(m.length));
    this.pacienteSvc.getAll().subscribe(p => this.totalPacientes.set(p.length));
    this.citaSvc.getAll().subscribe(c => {
      this.citas.set(c);
      this.loading.set(false);
    });
  }

  private loadMedico() {
    this.citaSvc.getAll().subscribe(c => {
      this.citas.set(c);
      this.loading.set(false);
    });
  }

  private loadPaciente() {
    this.citaSvc.getMisCitas().subscribe({
      next: c => { this.misCitas.set(c); this.loading.set(false); },
      error: () => this.loading.set(false),
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
