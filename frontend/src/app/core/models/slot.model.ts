import { Medico } from './medico.model';

export interface Slot {
  id?: number;
  medico: Medico;
  fechaHora: string;
  disponible?: boolean;
}

export function slotFecha(s: Slot): string {
  return s.fechaHora?.split('T')[0] ?? '';
}

export function slotHora(s: Slot): string {
  return s.fechaHora?.split('T')[1]?.substring(0, 5) ?? '';
}
