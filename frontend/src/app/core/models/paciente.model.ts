export interface Paciente {
  id?: number;
  name: string;
  surname: string;
  email: string;
  nif: string;
  fechaNacimiento: string;
}

export interface PacienteRequest {
  name: string;
  surname: string;
  email: string;
  password?: string;
  nif: string;
  fechaNacimiento: string;
}
