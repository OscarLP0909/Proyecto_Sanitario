import { Especialidad } from './especialidad.model';

export interface Medico {
  id?: number;
  email: string;
  name: string;
  surname: string;
  nif?: string;
  especialidades?: Especialidad[];
}

export interface MedicoRequest {
  name: string;
  surname: string;
  email: string;
  nif: string;
  especialidadesIds: number[];
}

export interface MedicoUpdateRequest {
  name: string;
  surname: string;
  nif: string;
  especialidadesIds: number[];
}

export function medicoNombre(m: Medico): string {
  return `${m.name} ${m.surname}`;
}

export function medicoEspecialidad(m: Medico): string {
  return m.especialidades?.[0]?.nombre ?? '—';
}
