import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AuthSessionService } from '../../core/auth/auth-session.service';
import { EmpleadosApiService } from '../../core/http/empleados-api.service';
import { extractApiErrorMessage, isUnauthorizedOrForbidden } from '../../core/http/api-error.util';
import { CredencialesLogin, PerfilAuth } from '../../core/models/auth.models';
import { Departamento, Empleado } from '../../core/models/empleado.models';

@Component({
  selector: 'app-admin-shell-container',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './admin-shell.container.html',
  styleUrl: './admin-shell.container.scss'
})
export class AdminShellContainerComponent {
  private readonly fb = inject(FormBuilder);
  private readonly http = inject(HttpClient);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly empleadosApi = inject(EmpleadosApiService);
  private readonly authSessionService = inject(AuthSessionService);
  private readonly authCredentialPattern = /^\S+$/;
  private readonly nombrePattern = /^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/;
  private readonly telefonoPattern = /^[0-9]+$/;

  protected readonly authForm = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.pattern(this.authCredentialPattern)]],
    password: ['', [Validators.required, Validators.pattern(this.authCredentialPattern)]],
  });

  protected readonly empleadoForm = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.pattern(this.nombrePattern), Validators.maxLength(100)]],
    direccion: ['', [Validators.required, Validators.maxLength(100)]],
    telefono: ['', [Validators.required, Validators.pattern(this.telefonoPattern), Validators.maxLength(100)]],
    correo: ['', [Validators.required, Validators.email, Validators.maxLength(150)]],
    contrasena: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(72)]],
    departamentoClave: [''],
  });

  protected readonly departamentoForm = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
  });

  protected perfil: PerfilAuth | null = null;
  protected empleados: Empleado[] = [];
  protected departamentos: Departamento[] = [];

  protected empleadosPage = 0;
  protected empleadosSize = 5;
  protected empleadosTotalPages = 0;
  protected departamentosPage = 0;
  protected departamentosSize = 5;
  protected departamentosTotalPages = 0;

  protected loadingAuth = false;
  protected loadingEmpleados = false;
  protected loadingDepartamentos = false;
  protected savingEmpleado = false;
  protected savingDepartamento = false;

  protected empleadoEditingClave: string | null = null;
  protected departamentoEditingClave: string | null = null;
  protected view: 'empleados' | 'departamentos' = 'empleados';
  protected message = '';
  protected errorMessage = '';

  protected get isAuthenticated(): boolean {
    return this.perfil !== null;
  }

  protected get isEditingEmpleado(): boolean {
    return this.empleadoEditingClave !== null;
  }

  protected get isEditingDepartamento(): boolean {
    return this.departamentoEditingClave !== null;
  }

  protected async login(): Promise<void> {
    if (this.loadingAuth) {
      return;
    }

    if (this.authForm.invalid) {
      this.authForm.markAllAsTouched();
      this.errorMessage = 'Usuario y contrasena son obligatorios y no deben contener espacios.';
      return;
    }

    const credentials: CredencialesLogin = this.authForm.getRawValue();

    this.resetFeedback();
    this.loadingAuth = true;

    try {
      this.authSessionService.setCredentials(credentials);

      const perfil = await firstValueFrom(this.empleadosApi.authMe(this.authHeaders()));
      if (perfil.actorType !== 'ADMIN') {
        this.authSessionService.clear();
        this.perfil = null;
        this.errorMessage = 'El usuario autenticado no tiene permisos de administrador.';
        return;
      }

      this.perfil = perfil;

      this.authSessionService.setProfile(this.perfil);
      this.message = `Sesion iniciada como ${this.perfil.displayName}`;

      // Run initial data loading without blocking the auth transition.
      // This avoids the UI waiting for multiple requests before rendering the shell.
      void this.preloadWorkspaceData();
    } catch (error) {
      this.perfil = null;
      this.authSessionService.clear();
      this.errorMessage = this.formatError(error, 'No fue posible iniciar sesion.');
    } finally {
      this.loadingAuth = false;
      this.requestRender();
    }
  }

  private async preloadWorkspaceData(): Promise<void> {
    await Promise.all([this.loadEmpleados(0), this.loadDepartamentos(0)]);
  }

  protected logout(): void {
    this.authSessionService.clear();
    this.perfil = null;
    this.empleados = [];
    this.departamentos = [];
    this.empleadoEditingClave = null;
    this.departamentoEditingClave = null;
    this.message = 'Sesion cerrada.';
    this.errorMessage = '';
    this.requestRender();
  }

  protected async loadEmpleados(page: number): Promise<void> {
    if (!this.isAuthenticated) {
      return;
    }

    this.loadingEmpleados = true;
    this.errorMessage = '';

    try {
      const response = await firstValueFrom(
        this.empleadosApi.listEmpleados(page, this.empleadosSize, this.authHeaders())
      );

      this.empleados = response.content;
      this.empleadosPage = response.page;
      this.empleadosTotalPages = response.totalPages;
    } catch (error) {
      if (this.handleUnauthorized(error)) {
        return;
      }
      this.errorMessage = this.formatError(error, 'No fue posible cargar empleados.');
    } finally {
      this.loadingEmpleados = false;
      this.requestRender();
    }
  }

  protected async loadDepartamentos(page: number): Promise<void> {
    if (!this.isAuthenticated) {
      return;
    }

    this.loadingDepartamentos = true;
    this.errorMessage = '';

    try {
      const response = await firstValueFrom(
        this.empleadosApi.listDepartamentos(page, this.departamentosSize, this.authHeaders())
      );

      this.departamentos = response.content;
      this.departamentosPage = response.page;
      this.departamentosTotalPages = response.totalPages;
    } catch (error) {
      if (this.handleUnauthorized(error)) {
        return;
      }
      this.errorMessage = this.formatError(error, 'No fue posible cargar departamentos.');
    } finally {
      this.loadingDepartamentos = false;
      this.requestRender();
    }
  }

  protected async switchToEmpleados(): Promise<void> {
    this.view = 'empleados';
    this.requestRender();

    if (!this.isAuthenticated) {
      return;
    }

    await this.loadEmpleados(this.empleadosPage);
  }

  protected async switchToDepartamentos(): Promise<void> {
    this.view = 'departamentos';
    this.requestRender();

    if (!this.isAuthenticated) {
      return;
    }

    await this.loadDepartamentos(this.departamentosPage);
  }

  protected startCreateEmpleado(): void {
    this.empleadoEditingClave = null;
    this.empleadoForm.reset({
      nombre: '',
      direccion: '',
      telefono: '',
      correo: '',
      contrasena: '',
      departamentoClave: '',
    });
    this.empleadoForm.controls.correo.setValidators([
      Validators.required,
      Validators.email,
      Validators.maxLength(150),
    ]);
    this.empleadoForm.controls.correo.enable();
    this.empleadoForm.controls.correo.updateValueAndValidity();
    this.empleadoForm.controls.contrasena.setValidators([
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(72),
    ]);
    this.empleadoForm.controls.contrasena.updateValueAndValidity();
    this.requestRender();
  }

  protected startEditEmpleado(empleado: Empleado): void {
    this.empleadoEditingClave = empleado.clave;
    this.view = 'empleados';
    this.empleadoForm.reset({
      nombre: empleado.nombre,
      direccion: empleado.direccion,
      telefono: empleado.telefono,
      correo: '',
      contrasena: '',
      departamentoClave: empleado.departamentoClave ?? '',
    });

    this.empleadoForm.controls.correo.clearValidators();
    this.empleadoForm.controls.correo.disable();
    this.empleadoForm.controls.correo.updateValueAndValidity();
    this.empleadoForm.controls.contrasena.setValidators([
      Validators.minLength(8),
      Validators.maxLength(72),
    ]);
    this.empleadoForm.controls.contrasena.updateValueAndValidity();
    this.requestRender();
  }

  protected cancelEmpleadoEdition(): void {
    this.startCreateEmpleado();
    this.requestRender();
  }

  protected async submitEmpleado(): Promise<void> {
    if (!this.isAuthenticated || this.empleadoForm.invalid || this.savingEmpleado) {
      return;
    }

    if (this.empleadoEditingClave) {
      const confirmed = window.confirm(`Deseas actualizar el empleado ${this.empleadoEditingClave}?`);
      if (!confirmed) {
        return;
      }
    }

    this.resetFeedback();
    this.savingEmpleado = true;

    try {
      const value = this.empleadoForm.getRawValue();

      if (this.empleadoEditingClave) {
        const payload = {
          nombre: value.nombre,
          direccion: value.direccion,
          telefono: value.telefono,
          contrasena: value.contrasena.trim() || undefined,
        };

        await firstValueFrom(
          this.empleadosApi.updateEmpleado(this.empleadoEditingClave, payload, this.authHeaders())
        );

        if (value.departamentoClave.trim()) {
          await firstValueFrom(
            this.empleadosApi.assignDepartamento(
              this.empleadoEditingClave,
              { departamentoClave: value.departamentoClave.trim() },
              this.authHeaders()
            )
          );
        }

        this.message = `Empleado ${this.empleadoEditingClave} actualizado.`;
      } else {
        const payload = {
          nombre: value.nombre,
          direccion: value.direccion,
          telefono: value.telefono,
          correo: value.correo,
          contrasena: value.contrasena,
          departamentoClave: value.departamentoClave.trim() || undefined,
        };

        await firstValueFrom(this.empleadosApi.createEmpleado(payload, this.authHeaders()));

        this.message = 'Empleado creado correctamente.';
      }

      this.startCreateEmpleado();
      await this.loadEmpleados(this.empleadosPage);
      await this.loadDepartamentos(this.departamentosPage);
    } catch (error) {
      if (this.handleUnauthorized(error)) {
        return;
      }
      this.errorMessage = this.formatError(error, 'No fue posible guardar el empleado.');
    } finally {
      this.savingEmpleado = false;
      this.requestRender();
    }
  }

  protected async deleteEmpleado(clave: string): Promise<void> {
    if (!this.isAuthenticated) {
      return;
    }

    const confirmed = window.confirm(`Deseas eliminar el empleado ${clave}?`);
    if (!confirmed) {
      return;
    }

    this.resetFeedback();

    try {
      await firstValueFrom(this.empleadosApi.deleteEmpleado(clave, this.authHeaders()));
      this.message = `Empleado ${clave} eliminado.`;
      await this.loadEmpleados(this.empleadosPage);
    } catch (error) {
      if (this.handleUnauthorized(error)) {
        return;
      }
      this.errorMessage = this.formatError(error, 'No fue posible eliminar el empleado.');
    } finally {
      this.requestRender();
    }
  }

  protected startCreateDepartamento(): void {
    this.departamentoEditingClave = null;
    this.departamentoForm.reset({ nombre: '' });
    this.requestRender();
  }

  protected startEditDepartamento(departamento: Departamento): void {
    this.departamentoEditingClave = departamento.clave;
    this.view = 'departamentos';
    this.departamentoForm.reset({ nombre: departamento.nombre });
    this.requestRender();
  }

  protected cancelDepartamentoEdition(): void {
    this.startCreateDepartamento();
    this.requestRender();
  }

  protected async submitDepartamento(): Promise<void> {
    if (!this.isAuthenticated || this.departamentoForm.invalid || this.savingDepartamento) {
      return;
    }

    this.resetFeedback();
    this.savingDepartamento = true;

    try {
      const payload = this.departamentoForm.getRawValue();

      if (this.departamentoEditingClave) {
        await firstValueFrom(
          this.http.put<Departamento>(
            `/api/v1/departamentos/${this.departamentoEditingClave}`,
            payload,
            { headers: this.authHeaders() }
          )
        );
        this.message = `Departamento ${this.departamentoEditingClave} actualizado.`;
      } else {
        await firstValueFrom(
          this.http.post<Departamento>('/api/v1/departamentos', payload, {
            headers: this.authHeaders(),
          })
        );
        this.message = 'Departamento creado correctamente.';
      }

      this.startCreateDepartamento();
      await this.loadDepartamentos(this.departamentosPage);
    } catch (error) {
      if (this.handleUnauthorized(error)) {
        return;
      }
      this.errorMessage = this.formatError(error, 'No fue posible guardar el departamento.');
    } finally {
      this.savingDepartamento = false;
      this.requestRender();
    }
  }

  protected async deleteDepartamento(clave: string): Promise<void> {
    if (!this.isAuthenticated) {
      return;
    }

    const confirmed = window.confirm(`Deseas eliminar el departamento ${clave}?`);
    if (!confirmed) {
      return;
    }

    this.resetFeedback();

    try {
      await firstValueFrom(
        this.http.delete<void>(`/api/v1/departamentos/${clave}`, {
          headers: this.authHeaders(),
        })
      );
      this.message = `Departamento ${clave} eliminado.`;
      await this.loadDepartamentos(this.departamentosPage);
    } catch (error) {
      if (this.handleUnauthorized(error)) {
        return;
      }
      this.errorMessage = this.formatError(error, 'No fue posible eliminar el departamento.');
    } finally {
      this.requestRender();
    }
  }

  protected canPrev(page: number): boolean {
    return page > 0;
  }

  protected canNext(page: number, totalPages: number): boolean {
    return page + 1 < totalPages;
  }

  private authHeaders(): HttpHeaders {
    return this.authSessionService.getAuthHeaders();
  }

  private resetFeedback(): void {
    this.message = '';
    this.errorMessage = '';
  }

  private formatError(error: unknown, fallback: string): string {
    return extractApiErrorMessage(error, fallback);
  }

  private handleUnauthorized(error: unknown): boolean {
    if (!isUnauthorizedOrForbidden(error)) {
      return false;
    }

    this.authSessionService.invalidateFromBackend();
    this.perfil = null;
    this.empleados = [];
    this.departamentos = [];
    this.errorMessage = 'Sesion invalidada por el backend. Inicia sesion nuevamente.';
    this.requestRender();
    return true;
  }

  private requestRender(): void {
    queueMicrotask(() => this.cdr.detectChanges());
  }
}
