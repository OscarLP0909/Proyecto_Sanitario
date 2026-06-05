import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Especialidad } from '../models/especialidad.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class EspecialidadService {
  private readonly API = '/api/especialidades';
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Page<Especialidad>>(`${this.API}?size=100`).pipe(map(p => p.content));
  }
  getById(id: number) { return this.http.get<Especialidad>(`${this.API}/${id}`); }
  create(req: Especialidad) { return this.http.post<Especialidad>(this.API, req); }
  update(id: number, req: Especialidad) { return this.http.put<Especialidad>(`${this.API}/${id}`, req); }
  delete(id: number) { return this.http.delete<void>(`${this.API}/${id}`); }
}
