describe('Admin MVP flow', () => {
  it('logs in as admin and navigates between modules', () => {
    cy.intercept({ method: 'GET', url: '**/api/v1/**' }, (req) => {
      const pathname = new URL(req.url).pathname;

      if (pathname.endsWith('/api/v1/empleados/auth/me')) {
        req.alias = 'authMe';
      }

      if (pathname.endsWith('/api/v1/empleados')) {
        req.alias = 'listEmpleados';
      }

      if (pathname.endsWith('/api/v1/departamentos')) {
        req.alias = 'listDepartamentos';
      }

      req.continue();
    });

    cy.visit('/');

    cy.get('[data-cy="login-card"]').should('be.visible');

    cy.window().then(async (win: any) => {
      const adminShellRoot = win.document.querySelector('app-admin-shell-container');
      const shellInstance = win.ng.getComponent(adminShellRoot);

      shellInstance.authForm.setValue({ username: 'admin', password: 'admin123' });
      await shellInstance.login();
      win.ng.applyChanges(shellInstance);
    });

    cy.wait('@authMe').then((interception) => {
      expect(interception.response?.statusCode).to.eq(200);
      expect(interception.response?.body?.actorType).to.eq('ADMIN');
    });
    cy.wait('@listEmpleados').its('response.statusCode').should('eq', 200);
    cy.wait('@listDepartamentos').its('response.statusCode').should('eq', 200);

    cy.get('[data-cy="global-error"]').should('not.exist');
    cy.get('[data-cy="admin-toolbar"]', { timeout: 15000 }).should('be.visible');
    cy.get('[data-cy="nav-empleados"]').should('be.visible');
    cy.get('[data-cy="nav-departamentos"]').should('be.visible');

    cy.get('[data-cy="empleados-list-title"]').should('be.visible');

    cy.get('[data-cy="nav-departamentos"]').click();
    cy.get('[data-cy="departamentos-list-title"]').should('be.visible');

    cy.get('[data-cy="nav-empleados"]').click();
    cy.get('[data-cy="empleados-list-title"]').should('be.visible');
  });
});
