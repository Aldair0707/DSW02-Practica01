import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { AdminShellContainerComponent } from './admin-shell.container';

describe('AdminShellContainerComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminShellContainerComponent],
      providers: [provideRouter([])],
    }).compileComponents();
  });

  it('should create the admin shell container', () => {
    const fixture = TestBed.createComponent(AdminShellContainerComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });

  it('should render login title', async () => {
    const fixture = TestBed.createComponent(AdminShellContainerComponent);
    fixture.detectChanges();
    await fixture.whenStable();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h2')?.textContent).toContain('Iniciar sesión');
  });

  it('should block login when credentials are blank', async () => {
    const fixture = TestBed.createComponent(AdminShellContainerComponent);
    const component = fixture.componentInstance as any;

    component.authForm.setValue({ username: '', password: '' });
    await component.login();

    expect(component.errorMessage).toContain('obligatorios');
    expect(component.authForm.controls.username.invalid).toBe(true);
    expect(component.authForm.controls.password.invalid).toBe(true);
  });
});
