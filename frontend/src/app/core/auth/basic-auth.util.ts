import { HttpHeaders } from '@angular/common/http';

import { CredencialesLogin } from '../models/auth.models';

export function buildBasicAuthToken(credentials: CredencialesLogin): string {
  return btoa(`${credentials.username}:${credentials.password}`);
}

export function buildBasicAuthHeaders(credentials: CredencialesLogin): HttpHeaders {
  return new HttpHeaders({
    Authorization: `Basic ${buildBasicAuthToken(credentials)}`,
  });
}
