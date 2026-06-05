import { Injectable, signal, effect } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly THEME_KEY = 'theme';
  isDark = signal<boolean>(localStorage.getItem(this.THEME_KEY) === 'dark');

  constructor() {
    effect(() => {
      const dark = this.isDark();
      document.documentElement.classList.toggle('dark', dark);
      localStorage.setItem(this.THEME_KEY, dark ? 'dark' : 'light');
    });
    document.documentElement.classList.toggle('dark', this.isDark());
  }

  toggle() {
    this.isDark.update(v => !v);
  }
}
