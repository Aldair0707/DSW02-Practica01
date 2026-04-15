export interface ApiPage<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface Empleado {
  clave: string;
  nombre: string;
  direccion: string;
  telefono: string;
  correo: string;
  departamentoClave: string | null;
  activo: boolean;
}

export interface EmpleadoCreateRequest {
  nombre: string;
  direccion: string;
  telefono: string;
  correo: string;
  contrasena: string;
  departamentoClave?: string;
}

export interface EmpleadoUpdateRequest {
  nombre: string;
  direccion: string;
  telefono: string;
  contrasena?: string;
}

export interface EmpleadoDepartamentoPatchRequest {
  departamentoClave: string;
}

export interface Departamento {
  clave: string;
  nombre: string;
}

export interface EmpleadoReadOnly {
  id: string;
  nombre: string;
  correo: string;
  departamento: string | null;
}

export interface DepartamentoReadOnly {
  id: string;
  nombre: string;
}
