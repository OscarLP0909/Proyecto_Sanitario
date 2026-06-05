import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ThemeService } from '../../../core/services/theme.service';
import { ToastComponent } from '../../../shared/toast/toast.component';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, ToastComponent],
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private toast = inject(ToastService);
  theme = inject(ThemeService);

  loading = signal(false);

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    name: ['', [Validators.required]],
    surname: ['', [Validators.required]],
    nif: ['', [Validators.required]],
    fechaNacimiento: ['', [Validators.required]],
  });

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    const v = this.form.value;
    this.auth.register({
      email: v.email!,
      password: v.password!,
      name: v.name!,
      surname: v.surname!,
      nif: v.nif!,
      fechaNacimiento: v.fechaNacimiento!,
    }).subscribe({
      next: () => {
        this.toast.success('¡Cuenta creada correctamente! Ya puedes iniciar sesión.');
        this.loading.set(false);
        this.router.navigate(['/login']);
      },
      error: (e) => {
        const msg = e?.error?.message ?? 'Error al registrarse. Comprueba los datos e inténtalo de nuevo.';
        this.toast.error(msg);
        this.loading.set(false);
      },
    });
  }

  field(name: string) { return this.form.get(name)!; }
  isInvalid(name: string) { return this.field(name).invalid && this.field(name).touched; }
}
