import { Paciente } from './paciente.model';
import { Slot, slotFecha, slotHora } from './slot.model';
import { medicoNombre, medicoEspecialidad } from './medico.model';

export interface Cita {
  id?: number;
  paciente: Paciente;
  slot: Slot;
  estado: EstadoCita;
  motivo?: string;
  notas?: string;
}

export interface CitaRequest {
  pacienteId: number;
  slotId: number;
  motivo: string;
  notas: string;
}

export type EstadoCita = 'PENDIENTE' | 'CONFIRMADA' | 'CANCELADA' | 'COMPLETADA';

export function citaPacienteNombre(c: Cita): string {
  return c.paciente ? `${c.paciente.name} ${c.paciente.surname}` : '—';
}

export function citaMedicoNombre(c: Cita): string {
  return c.slot?.medico ? medicoNombre(c.slot.medico) : '—';
}

export function citaEspecialidad(c: Cita): string {
  return c.slot?.medico ? medicoEspecialidad(c.slot.medico) : '—';
}

export function citaFecha(c: Cita): string {
  return c.slot ? slotFecha(c.slot) : '—';
}

export function citaHora(c: Cita): string {
  return c.slot ? slotHora(c.slot) : '—';
}
