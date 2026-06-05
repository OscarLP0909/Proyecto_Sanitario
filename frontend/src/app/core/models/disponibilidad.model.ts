import { Medico } from './medico.model';

export interface Disponibilidad {
  id?: number;
  medico: Medico;
  diaSemana: string;
  horaInicio: string;
  horaFin: string;
  duracionMinutos?: number;
}

export interface DisponibilidadRequest {
  medicoId: number;
  diaSemana: string;
  horaInicio: string;
  horaFin: string;
  duracionMinutos?: number;
}

export const DIAS_SEMANA = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];
