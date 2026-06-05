import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Cita, CitaRequest, EstadoCita } from '../models/cita.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class CitaService {
  private readonly API = '/api/citas';
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Page<Cita>>(`${this.API}?size=200`).pipe(map(p => p.content));
  }
  getMisCitas() {
    return this.http.get<Page<Cita>>(`${this.API}/mis-citas?size=100`).pipe(map(p => p.content));
  }
  getById(id: number) { return this.http.get<Cita>(`${this.API}/${id}`); }
  create(req: CitaRequest) { return this.http.post<Cita>(this.API, req); }
  update(id: number, req: Partial<CitaRequest & { estado: string; notas: string }>) {
    return this.http.put<Cita>(`${this.API}/${id}`, req);
  }
  cambiarEstado(id: number, estado: EstadoCita) {
    return this.http.patch<Cita>(`${this.API}/${id}/estado`, { estado });
  }
  cancelar(id: number) { return this.http.post<Cita>(`${this.API}/${id}/cancelar`, {}); }
  delete(id: number) { return this.http.delete<void>(`${this.API}/${id}`); }
}
