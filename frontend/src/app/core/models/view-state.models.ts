export interface FeatureViewState<T> {
  loading: boolean;
  errorMessage: string | null;
  items: T[];
  page: number;
  size: number;
  totalPages: number;
}

export interface AdminShellState {
  isAuthenticated: boolean;
  activeView: 'empleados' | 'departamentos';
  authLoading: boolean;
  globalErrorMessage: string | null;
  globalSuccessMessage: string | null;
}
