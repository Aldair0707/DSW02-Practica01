import { PerfilAuth } from '../../core/models/auth.models';
import { DepartamentoReadOnly, EmpleadoReadOnly } from '../../core/models/empleado.models';

export type EmpleadoLoginViewState = 'idle' | 'loading' | 'authenticated' | 'error';

export interface EmpleadoLoginState {
  viewState: EmpleadoLoginViewState;
  profile: PerfilAuth | null;
  errorMessage: string;
  empleados: EmpleadoReadOnly[];
  departamentos: DepartamentoReadOnly[];
  empleadosPage: number;
  empleadosSize: number;
  empleadosTotalPages: number;
  departamentosPage: number;
  departamentosSize: number;
  departamentosTotalPages: number;
  loadingListados: boolean;
}

export const initialEmpleadoLoginState: EmpleadoLoginState = {
  viewState: 'idle',
  profile: null,
  errorMessage: '',
  empleados: [],
  departamentos: [],
  empleadosPage: 0,
  empleadosSize: 5,
  empleadosTotalPages: 0,
  departamentosPage: 0,
  departamentosSize: 5,
  departamentosTotalPages: 0,
  loadingListados: false,
};
