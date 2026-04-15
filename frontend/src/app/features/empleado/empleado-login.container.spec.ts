import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';

import { EmpleadoLoginContainerComponent } from './empleado-login.container';
import { EmpleadoAuthFacade } from '../../core/auth/empleado-auth.facade';

describe('EmpleadoLoginContainerComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpleadoLoginContainerComponent],
      providers: [provideHttpClient(), provideRouter([])],
    }).compileComponents();
  });

  it('should create the component', () => {
    const fixture = TestBed.createComponent(EmpleadoLoginContainerComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render dedicated empleado title', async () => {
    const fixture = TestBed.createComponent(EmpleadoLoginContainerComponent);
    fixture.detectChanges();
    await fixture.whenStable();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Acceso Empleado');
  });

  it('should block submit when form is invalid', async () => {
    const fixture = TestBed.createComponent(EmpleadoLoginContainerComponent);
    const component = fixture.componentInstance as any;

    component.loginForm.setValue({ correo: '', password: '' });
    await component.login();

    expect(component.state.viewState).toBe('error');
    expect(component.state.errorMessage).toContain('obligatorios');
  });

  it('should show permissions error when actor is not EMPLEADO', async () => {
    const facade = TestBed.inject(EmpleadoAuthFacade);
    vi.spyOn(facade, 'login').mockRejectedValue(new Error('ACTOR_NO_EMPLEADO'));

    const fixture = TestBed.createComponent(EmpleadoLoginContainerComponent);
    const component = fixture.componentInstance as any;
    component.loginForm.setValue({ correo: 'admin@empresa.com', password: 'admin123' });

    await component.login();

    expect(component.state.viewState).toBe('error');
    expect(component.state.errorMessage).toContain('permisos');
  });
});
