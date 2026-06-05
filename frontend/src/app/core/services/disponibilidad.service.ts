import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Disponibilidad, DisponibilidadRequest } from '../models/disponibilidad.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class DisponibilidadService {
  private readonly API = '/api/disponibilidades';
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Page<Disponibilidad>>(`${this.API}?size=200`).pipe(map(p => p.content));
  }
  getById(id: number) { return this.http.get<Disponibilidad>(`${this.API}/${id}`); }
  create(req: DisponibilidadRequest) { return this.http.post<Disponibilidad>(this.API, req); }
  update(id: number, req: DisponibilidadRequest) { return this.http.put<Disponibilidad>(`${this.API}/${id}`, req); }
  delete(id: number) { return this.http.delete<void>(`${this.API}/${id}`); }
}
