import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import {
  ApiPage,
  Departamento,
  DepartamentoReadOnly,
  Empleado,
  EmpleadoReadOnly,
  EmpleadoCreateRequest,
  EmpleadoDepartamentoPatchRequest,
  EmpleadoUpdateRequest,
} from '../models/empleado.models';
import { PerfilAuth } from '../models/auth.models';

@Injectable({
  providedIn: 'root',
})
export class EmpleadosApiService {
  private readonly http = inject(HttpClient);

  authMe(headers: HttpHeaders): Observable<PerfilAuth> {
    return this.http.get<PerfilAuth>('/api/v1/empleados/auth/me', { headers });
  }

  listEmpleados(page: number, size: number, headers: HttpHeaders): Observable<ApiPage<Empleado>> {
    return this.http.get<ApiPage<Empleado>>('/api/v1/empleados', {
      headers,
      params: {
        page: String(page),
        size: String(size),
      },
    });
  }

  createEmpleado(payload: EmpleadoCreateRequest, headers: HttpHeaders): Observable<Empleado> {
    return this.http.post<Empleado>('/api/v1/empleados', payload, { headers });
  }

  updateEmpleado(clave: string, payload: EmpleadoUpdateRequest, headers: HttpHeaders): Observable<Empleado> {
    return this.http.put<Empleado>(`/api/v1/empleados/${clave}`, payload, { headers });
  }

  assignDepartamento(
    clave: string,
    payload: EmpleadoDepartamentoPatchRequest,
    headers: HttpHeaders
  ): Observable<Empleado> {
    return this.http.patch<Empleado>(`/api/v1/empleados/${clave}/departamento`, payload, { headers });
  }

  deleteEmpleado(clave: string, headers: HttpHeaders): Observable<void> {
    return this.http.delete<void>(`/api/v1/empleados/${clave}`, { headers });
  }

  listDepartamentos(page: number, size: number, headers: HttpHeaders): Observable<ApiPage<Departamento>> {
    return this.http.get<ApiPage<Departamento>>('/api/v1/departamentos', {
      headers,
      params: {
        page: String(page),
        size: String(size),
      },
    });
  }

  listEmpleadosReadOnly(page: number, size: number, headers: HttpHeaders): Observable<ApiPage<EmpleadoReadOnly>> {
    return this.http.get<ApiPage<EmpleadoReadOnly>>('/api/v1/empleados', {
      headers,
      params: {
        page: String(page),
        size: String(size),
      },
    });
  }

  listDepartamentosReadOnly(
    page: number,
    size: number,
    headers: HttpHeaders
  ): Observable<ApiPage<DepartamentoReadOnly>> {
    return this.http.get<ApiPage<DepartamentoReadOnly>>('/api/v1/departamentos', {
      headers,
      params: {
        page: String(page),
        size: String(size),
      },
    });
  }
}
