import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { EmpleadoAuthFacade } from '../../core/auth/empleado-auth.facade';
import { extractApiErrorMessage } from '../../core/http/api-error.util';
import { initialEmpleadoLoginState } from './empleado-login.state';

@Component({
  selector: 'app-empleado-login-container',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './empleado-login.container.html',
  styleUrl: './empleado-login.container.scss',
})
export class EmpleadoLoginContainerComponent {
  private readonly fb = inject(FormBuilder);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly empleadoAuthFacade = inject(EmpleadoAuthFacade);

  protected readonly loginForm = this.fb.nonNullable.group({
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.pattern(/^\S+$/)]],
  });

  protected state = { ...initialEmpleadoLoginState };

  protected async login(): Promise<void> {
    if (this.loginForm.invalid || this.state.viewState === 'loading') {
      this.loginForm.markAllAsTouched();
      this.state = {
        ...this.state,
        viewState: 'error',
        errorMessage: 'Correo y contrasena son obligatorios y validos.',
      };
      return;
    }

    this.state = {
      ...this.state,
      viewState: 'loading',
      errorMessage: '',
    };

    try {
      const { correo, password } = this.loginForm.getRawValue();
      const profile = await this.empleadoAuthFacade.login(correo, password);
      this.state = {
        ...this.state,
        viewState: 'authenticated',
        profile,
        errorMessage: '',
        loadingListados: true,
      };

      await this.loadListados(0, 0);
    } catch (error) {
      const actorMismatch = error instanceof Error && error.message === 'ACTOR_NO_EMPLEADO';
      this.state = {
        ...this.state,
        profile: null,
        viewState: 'error',
        errorMessage: actorMismatch
          ? 'Tu cuenta no tiene permisos para el portal de empleado.'
          : extractApiErrorMessage(error, 'Credenciales invalidas.'),
      };
    } finally {
      this.requestRender();
    }
  }

  protected logout(): void {
    this.empleadoAuthFacade.logout();
    this.loginForm.reset({ correo: '', password: '' });
    this.state = { ...initialEmpleadoLoginState };
    this.requestRender();
  }

  protected async loadEmpleados(page: number): Promise<void> {
    if (!this.state.profile) {
      return;
    }

    try {
      const empleadosPage = await this.empleadoAuthFacade.listEmpleadosReadOnly(page, this.state.empleadosSize);
      this.state = {
        ...this.state,
        empleados: empleadosPage.content,
        empleadosPage: empleadosPage.page,
        empleadosTotalPages: empleadosPage.totalPages,
      };
    } catch (error) {
      this.handleAuthorizationError(error);
    } finally {
      this.requestRender();
    }
  }

  protected async loadDepartamentos(page: number): Promise<void> {
    if (!this.state.profile) {
      return;
    }

    try {
      const departamentosPage = await this.empleadoAuthFacade.listDepartamentosReadOnly(
        page,
        this.state.departamentosSize
      );
      this.state = {
        ...this.state,
        departamentos: departamentosPage.content,
        departamentosPage: departamentosPage.page,
        departamentosTotalPages: departamentosPage.totalPages,
      };
    } catch (error) {
      this.handleAuthorizationError(error);
    } finally {
      this.requestRender();
    }
  }

  private async loadListados(empleadosPage: number, departamentosPage: number): Promise<void> {
    try {
      const [empleados, departamentos] = await Promise.all([
        this.empleadoAuthFacade.listEmpleadosReadOnly(empleadosPage, this.state.empleadosSize),
        this.empleadoAuthFacade.listDepartamentosReadOnly(departamentosPage, this.state.departamentosSize),
      ]);

      this.state = {
        ...this.state,
        empleados: empleados.content,
        empleadosPage: empleados.page,
        empleadosTotalPages: empleados.totalPages,
        departamentos: departamentos.content,
        departamentosPage: departamentos.page,
        departamentosTotalPages: departamentos.totalPages,
        loadingListados: false,
      };
    } catch (error) {
      this.handleAuthorizationError(error);
    }
  }

  private handleAuthorizationError(error: unknown): void {
    if (error instanceof HttpErrorResponse && (error.status === 401 || error.status === 403)) {
      this.empleadoAuthFacade.logout();
      this.state = {
        ...initialEmpleadoLoginState,
        viewState: 'error',
        errorMessage:
          error.status === 401
            ? 'Tu sesion ya no es valida. Inicia sesion nuevamente.'
            : 'No tienes permisos para modificar informacion. Tu acceso es solo lectura.',
      };
      return;
    }

    this.state = {
      ...this.state,
      viewState: 'error',
      loadingListados: false,
      errorMessage: extractApiErrorMessage(error, 'No fue posible cargar los listados de consulta.'),
    };
  }

  private requestRender(): void {
    queueMicrotask(() => this.cdr.detectChanges());
  }
}
