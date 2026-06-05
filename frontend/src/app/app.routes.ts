import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { LayoutComponent } from './shared/layout/layout.component';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent),
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
      },
      {
        path: 'medicos',
        loadComponent: () => import('./features/medicos/medicos.component').then(m => m.MedicosComponent),
        canActivate: [roleGuard(['ADMIN'])],
      },
      {
        path: 'pacientes',
        loadComponent: () => import('./features/pacientes/pacientes.component').then(m => m.PacientesComponent),
        canActivate: [roleGuard(['ADMIN'])],
      },
      {
        path: 'especialidades',
        loadComponent: () => import('./features/especialidades/especialidades.component').then(m => m.EspecialidadesComponent),
        canActivate: [roleGuard(['ADMIN'])],
      },
      {
        path: 'disponibilidades',
        loadComponent: () => import('./features/disponibilidades/disponibilidades.component').then(m => m.DisponibilidadesComponent),
        canActivate: [roleGuard(['ADMIN', 'MEDICO'])],
      },
      {
        path: 'citas',
        loadComponent: () => import('./features/citas/citas.component').then(m => m.CitasComponent),
        canActivate: [roleGuard(['ADMIN', 'MEDICO'])],
      },
      {
        path: 'mis-citas',
        loadComponent: () => import('./features/citas/mis-citas.component').then(m => m.MisCitasComponent),
        canActivate: [roleGuard(['PACIENTE'])],
      },
      {
        path: 'slots',
        loadComponent: () => import('./features/slots/slots.component').then(m => m.SlotsComponent),
        canActivate: [roleGuard(['PACIENTE'])],
      },
      {
        path: 'perfil',
        loadComponent: () => import('./features/perfil/perfil.component').then(m => m.PerfilComponent),
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ],
  },
  { path: '**', redirectTo: 'login' },
];
