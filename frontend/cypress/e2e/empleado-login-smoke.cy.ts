describe('Empleado login smoke', () => {
  it('logs in with active empleado and returns EMPLEADO actor profile', () => {
    const unique = Date.now();
    const correo = `empleado.smoke.${unique}@empresa.com`;
    const contrasena = 'SmokePass123';

    cy.intercept('GET', '**/api/v1/empleados/auth/me', {
      statusCode: 200,
      body: {
        actorType: 'EMPLEADO',
        permissions: ['SELF'],
      },
    }).as('authMe');
    cy.intercept('GET', '**/api/v1/empleados?page=*&size=*', {
      statusCode: 200,
      body: {
        content: [],
        totalElements: 0,
        totalPages: 0,
        size: 10,
        number: 0,
      },
    }).as('listEmpleados');
    cy.intercept('GET', '**/api/v1/departamentos?page=*&size=*', {
      statusCode: 200,
      body: {
        content: [],
        totalElements: 0,
        totalPages: 0,
        size: 10,
        number: 0,
      },
    }).as('listDepartamentos');

    cy.visit('/empleado/login');

    cy.get('[data-cy="empleado-correo"]').clear().type(correo);
    cy.get('[data-cy="empleado-password"]').clear().type(contrasena);
    cy.get('[data-cy="empleado-login-submit"]').click();

    cy.wait('@authMe').then((interception) => {
      expect(interception.response?.statusCode).to.eq(200);
      expect(interception.response?.body?.actorType).to.eq('EMPLEADO');
      expect(interception.response?.body?.permissions).to.deep.equal(['SELF']);
    });

    cy.wait('@listEmpleados').then((interception) => {
      expect(interception.response?.statusCode).to.eq(200);
      expect(interception.response?.body?.content).to.be.an('array');
    });

    cy.wait('@listDepartamentos').then((interception) => {
      expect(interception.response?.statusCode).to.eq(200);
      expect(interception.response?.body?.content).to.be.an('array');
    });

    cy.get('[data-cy="empleado-login-success"]').should('be.visible');
    cy.get('[data-cy="empleado-readonly-note"]').should('be.visible');
    cy.get('[data-cy="empleado-readonly-empleados"]').should('be.visible');
    cy.get('[data-cy="empleado-readonly-departamentos"]').should('be.visible');

    cy.window().then((win) => {
      expect(win.localStorage.length).to.eq(0);
      expect(win.sessionStorage.length).to.eq(0);
    });
  });

  it('shows generic error for invalid password', () => {
    const unique = Date.now();
    const correo = `empleado.smoke.invalid.${unique}@empresa.com`;

    cy.intercept('GET', '**/api/v1/empleados/auth/me', {
      statusCode: 401,
      body: {
        message: 'Credenciales invalidas',
      },
    }).as('authMe');

    cy.visit('/empleado/login');

    cy.get('[data-cy="empleado-correo"]').clear().type(correo);
    cy.get('[data-cy="empleado-password"]').clear().type('PasswordIncorrecta');
    cy.get('[data-cy="empleado-login-submit"]').click();

    cy.get('[data-cy="empleado-login-error"]').should('contain.text', 'Credenciales invalidas');
  });

  it('shows same generic error for inactive account', () => {
    const unique = Date.now();
    const correo = `empleado.smoke.inactivo.${unique}@empresa.com`;
    const contrasena = 'SmokePass123';

    cy.intercept('GET', '**/api/v1/empleados/auth/me', {
      statusCode: 401,
      body: {
        message: 'Credenciales invalidas',
      },
    }).as('authMe');

    cy.visit('/empleado/login');

    cy.get('[data-cy="empleado-correo"]').clear().type(correo);
    cy.get('[data-cy="empleado-password"]').clear().type(contrasena);
    cy.get('[data-cy="empleado-login-submit"]').click();

    cy.get('[data-cy="empleado-login-error"]').should('contain.text', 'Credenciales invalidas');
  });
});
