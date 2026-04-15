import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';

import { buildBasicAuthHeaders } from './basic-auth.util';
import { CredencialesLogin, PerfilAuth } from '../models/auth.models';

@Injectable({
  providedIn: 'root',
})
export class AuthSessionService {
  private credentials: CredencialesLogin | null = null;
  private profile: PerfilAuth | null = null;

  setCredentials(credentials: CredencialesLogin): void {
    this.credentials = credentials;
  }

  setProfile(profile: PerfilAuth | null): void {
    this.profile = profile;
  }

  getProfile(): PerfilAuth | null {
    return this.profile;
  }

  isAuthenticated(): boolean {
    return this.credentials !== null && this.profile !== null;
  }

  getAuthHeaders(): HttpHeaders {
    if (!this.credentials) {
      return new HttpHeaders();
    }

    return buildBasicAuthHeaders(this.credentials);
  }

  clear(): void {
    this.credentials = null;
    this.profile = null;
  }

  invalidateFromBackend(): void {
    this.clear();
  }
}
