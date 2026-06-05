import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';

function passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
  const newPass = control.get('newPassword')?.value;
  const confirm = control.get('confirmPassword')?.value;
  return newPass && confirm && newPass !== confirm ? { mismatch: true } : null;
}

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './perfil.component.html',
})
export class PerfilComponent {
  auth = inject(AuthService);
  private http = inject(HttpClient);
  private toast = inject(ToastService);
  private fb = inject(FormBuilder);

  saving = signal(false);
  showCurrent = signal(false);
  showNew = signal(false);
  showConfirm = signal(false);

  form = this.fb.group({
    currentPassword: ['', Validators.required],
    newPassword: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required],
  }, { validators: passwordsMatchValidator });

  get email() { return this.auth.userFullName(); }
  get role() { return this.auth.userRole(); }

  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const v = this.form.value;
    this.http.put('/api/auth/change-password', {
      currentPassword: v.currentPassword,
      newPassword: v.newPassword,
    }).subscribe({
      next: () => {
        this.toast.success('Contraseña actualizada correctamente');
        this.form.reset();
        this.saving.set(false);
      },
      error: (e) => {
        const msg = e?.error?.message ?? e?.status === 401 ? 'La contraseña actual no es correcta' : 'Error al cambiar la contraseña';
        this.toast.error(msg);
        this.saving.set(false);
      }
    });
  }

  field(n: string) { return this.form.get(n)!; }
  isInvalid(n: string) { return this.field(n).invalid && this.field(n).touched; }
}
