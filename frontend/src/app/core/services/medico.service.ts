import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Medico, MedicoRequest, MedicoUpdateRequest } from '../models/medico.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class MedicoService {
  private readonly API = '/api/medicos';
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Page<Medico>>(`${this.API}?size=100`).pipe(map(p => p.content));
  }
  getMe() { return this.http.get<Medico>(`${this.API}/me`); }
  getById(id: number) { return this.http.get<Medico>(`${this.API}/${id}`); }
  create(req: MedicoRequest) { return this.http.post<Medico>(this.API, req); }
  update(id: number, req: MedicoUpdateRequest) { return this.http.put<Medico>(`${this.API}/${id}`, req); }
  delete(id: number) { return this.http.delete<void>(`${this.API}/${id}`); }
}
