import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Paciente, PacienteRequest } from '../models/paciente.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class PacienteService {
  private readonly API = '/api/pacientes';
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Page<Paciente>>(`${this.API}?size=100`).pipe(map(p => p.content));
  }
  getMe() { return this.http.get<Paciente>(`${this.API}/me`); }
  getById(id: number) { return this.http.get<Paciente>(`${this.API}/${id}`); }
  create(req: PacienteRequest) { return this.http.post<Paciente>(this.API, req); }
  update(id: number, req: PacienteRequest) { return this.http.put<Paciente>(`${this.API}/${id}`, req); }
  delete(id: number) { return this.http.delete<void>(`${this.API}/${id}`); }
}
