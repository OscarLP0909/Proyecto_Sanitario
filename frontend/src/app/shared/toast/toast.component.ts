import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="fixed top-4 right-4 z-50 flex flex-col gap-2 min-w-72 max-w-sm">
      @for (toast of toastService.toasts(); track toast.id) {
        <div
          class="flex items-center gap-3 px-4 py-3 rounded-lg shadow-lg text-white text-sm font-medium transition-all animate-fade-in"
          [class]="toastClass(toast.type)"
        >
          <span class="text-lg">{{ toastIcon(toast.type) }}</span>
          <span class="flex-1">{{ toast.message }}</span>
          <button
            (click)="toastService.remove(toast.id)"
            class="ml-2 opacity-70 hover:opacity-100 text-lg leading-none"
          >&times;</button>
        </div>
      }
    </div>
  `,
})
export class ToastComponent {
  toastService = inject(ToastService);

  toastClass(type: string): string {
    const map: Record<string, string> = {
      success: 'bg-emerald-500',
      error: 'bg-red-500',
      info: 'bg-blue-500',
      warning: 'bg-amber-500',
    };
    return map[type] ?? 'bg-gray-600';
  }

  toastIcon(type: string): string {
    const map: Record<string, string> = {
      success: '✓',
      error: '✕',
      info: 'ℹ',
      warning: '⚠',
    };
    return map[type] ?? '•';
  }
}
