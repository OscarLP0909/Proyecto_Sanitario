import { Component, inject, signal } from '@angular/core';
import { RouterModule, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';

interface NavItem {
  label: string;
  icon: string;
  route: string;
  roles: string[];
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
})
export class SidebarComponent {
  auth = inject(AuthService);
  collapsed = signal(false);

  navItems: NavItem[] = [
    { label: 'Dashboard', icon: '⊞', route: '/dashboard', roles: ['ADMIN', 'MEDICO', 'PACIENTE'] },
    { label: 'Médicos', icon: '👨‍⚕️', route: '/medicos', roles: ['ADMIN'] },
    { label: 'Pacientes', icon: '🧑‍🤝‍🧑', route: '/pacientes', roles: ['ADMIN'] },
    { label: 'Especialidades', icon: '🏥', route: '/especialidades', roles: ['ADMIN'] },
    { label: 'Disponibilidades', icon: '📅', route: '/disponibilidades', roles: ['ADMIN', 'MEDICO'] },
    { label: 'Citas', icon: '📋', route: '/citas', roles: ['ADMIN', 'MEDICO'] },
    { label: 'Mis Citas', icon: '📋', route: '/mis-citas', roles: ['PACIENTE'] },
    { label: 'Reservar Cita', icon: '✚', route: '/slots', roles: ['PACIENTE'] },
    { label: 'Mi Perfil', icon: '👤', route: '/perfil', roles: ['MEDICO', 'PACIENTE'] },
  ];

  get visibleItems(): NavItem[] {
    const role = this.auth.userRole();
    return this.navItems.filter(item => role && item.roles.includes(role));
  }

  toggle() {
    this.collapsed.update(v => !v);
  }
}
