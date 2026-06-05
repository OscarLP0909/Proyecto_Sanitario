import { Injectable, signal } from '@angular/core';

export interface Toast {
  id: number;
  type: 'success' | 'error' | 'info' | 'warning';
  message: string;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private _toasts = signal<Toast[]>([]);
  readonly toasts = this._toasts.asReadonly();
  private counter = 0;

  show(type: Toast['type'], message: string, duration = 4000) {
    const id = ++this.counter;
    this._toasts.update(t => [...t, { id, type, message }]);
    setTimeout(() => this.remove(id), duration);
  }

  success(msg: string) { this.show('success', msg); }
  error(msg: string) { this.show('error', msg); }
  info(msg: string) { this.show('info', msg); }
  warning(msg: string) { this.show('warning', msg); }

  remove(id: number) {
    this._toasts.update(t => t.filter(toast => toast.id !== id));
  }
}
