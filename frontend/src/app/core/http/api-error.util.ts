import { HttpErrorResponse } from '@angular/common/http';

import { ApiError } from '../models/auth.models';

export function extractApiErrorMessage(error: unknown, fallback: string): string {
  if (error instanceof HttpErrorResponse) {
    if (error.status === 401) {
      return 'Credenciales invalidas.';
    }

    if (error.status === 403) {
      return 'No tienes permisos para esta accion.';
    }

    const payload = error.error as ApiError | null;

    if (payload && typeof payload.message === 'string' && payload.message.length > 0) {
      return payload.message;
    }

    if (typeof error.message === 'string' && error.message.length > 0) {
      return error.message;
    }
  }

  return fallback;
}

export function isUnauthorizedOrForbidden(error: unknown): boolean {
  return error instanceof HttpErrorResponse && (error.status === 401 || error.status === 403);
}
