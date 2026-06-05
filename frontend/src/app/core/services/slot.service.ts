import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Slot } from '../models/slot.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class SlotService {
  private readonly API = '/api/slots';
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Page<Slot>>(`${this.API}?size=200`).pipe(map(p => p.content));
  }

  getDisponiblesByMedico(medicoId: number) {
    return this.http.get<Page<Slot>>(`${this.API}/medico/${medicoId}/disponibles?size=50`).pipe(map(p => p.content));
  }

  getById(id: number) { return this.http.get<Slot>(`${this.API}/${id}`); }
}
