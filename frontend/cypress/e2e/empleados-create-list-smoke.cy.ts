describe('Empleados Smoke - Crear y listar', () => {
  it('autentica admin y crea+lista un empleado', () => {
    const unique = Date.now();
    const correo = `smoke.${unique}@example.com`;

    cy.intercept('GET', '**/api/v1/empleados/auth/me').as('authMe');
    cy.intercept('GET', '**/api/v1/empleados*').as('empleadosList');
    cy.intercept('POST', '**/api/v1/empleados').as('empleadoCreate');

    cy.visit('/');

    cy.window().then(async (win: any) => {
      const adminShellRoot = win.document.querySelector('app-admin-shell-container');
      const shellInstance = win.ng.getComponent(adminShellRoot);

      shellInstance.authForm.setValue({ username: 'admin', password: 'admin123' });
      await shellInstance.login();
      win.ng.applyChanges(shellInstance);
    });

    cy.wait('@authMe').its('response.statusCode').should('eq', 200);
    cy.wait('@empleadosList').its('response.statusCode').should('eq', 200);
    cy.get('[data-cy="admin-toolbar"]').should('be.visible');
    cy.get('[data-cy="nav-empleados"]').click();
    cy.get('[data-cy="empleados-list-title"]').should('contain.text', 'Empleados');

    cy.get('[data-cy="empleados-section"]').within(() => {
      cy.get('input[formcontrolname="nombre"]').clear().type(`Smoke User ${unique}`);
      cy.get('input[formcontrolname="direccion"]').clear().type('Direccion Smoke 123');
      cy.get('input[formcontrolname="telefono"]').clear().type('5551234');
      cy.get('input[formcontrolname="correo"]').clear().type(correo);
      cy.get('input[formcontrolname="contrasena"]').clear().type('smoke1234');
      cy.get('button[type="submit"]').contains('Crear').click();
    });

    cy.wait('@empleadoCreate').its('response.statusCode').should('be.oneOf', [200, 201]);
    cy.wait('@empleadosList').its('response.statusCode').should('eq', 200);

    cy.get('.feedback.success').should('contain.text', 'Empleado creado correctamente.');
  });
});
