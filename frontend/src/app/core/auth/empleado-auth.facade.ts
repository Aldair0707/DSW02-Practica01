import { Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';

import { AuthSessionService } from './auth-session.service';
import { EmpleadosApiService } from '../http/empleados-api.service';
import { PerfilAuth } from '../models/auth.models';
import { ApiPage, DepartamentoReadOnly, EmpleadoReadOnly } from '../models/empleado.models';

@Injectable({
  providedIn: 'root',
})
export class EmpleadoAuthFacade {
  private readonly authSessionService = inject(AuthSessionService);
  private readonly empleadosApiService = inject(EmpleadosApiService);

  async login(correo: string, password: string): Promise<PerfilAuth> {
    const normalizedCorreo = correo.trim().toLowerCase();
    this.authSessionService.setCredentials({ username: normalizedCorreo, password });

    try {
      const profile = await firstValueFrom(
        this.empleadosApiService.authMe(this.authSessionService.getAuthHeaders())
      );

      if (profile.actorType !== 'EMPLEADO') {
        this.authSessionService.clear();
        throw new Error('ACTOR_NO_EMPLEADO');
      }

      this.authSessionService.setProfile(profile);
      return profile;
    } catch (error) {
      this.authSessionService.clear();
      throw error;
    }
  }

  logout(): void {
    this.authSessionService.clear();
  }

  async listEmpleadosReadOnly(page: number, size: number): Promise<ApiPage<EmpleadoReadOnly>> {
    return firstValueFrom(this.empleadosApiService.listEmpleadosReadOnly(page, size, this.authHeaders()));
  }

  async listDepartamentosReadOnly(page: number, size: number): Promise<ApiPage<DepartamentoReadOnly>> {
    return firstValueFrom(this.empleadosApiService.listDepartamentosReadOnly(page, size, this.authHeaders()));
  }

  private authHeaders() {
    return this.authSessionService.getAuthHeaders();
  }
}
