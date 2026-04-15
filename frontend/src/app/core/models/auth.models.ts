export interface ApiError {
  code: string;
  message: string;
  details: string[];
}

export interface CredencialesLogin {
  username: string;
  password: string;
}

export type ActorType = 'ADMIN' | 'EMPLEADO';

export interface PerfilAuthEmpleadoResumen {
  clave: string;
  correo: string;
  nombre: string;
  direccion: string;
  telefono: string;
  departamentoClave: string | null;
  activo: boolean;
}

export interface PerfilAuth {
  actorType: ActorType;
  username: string;
  displayName: string;
  permissions: string[];
  empleado: PerfilAuthEmpleadoResumen | null;
}
