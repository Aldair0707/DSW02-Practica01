import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    specPattern: 'cypress/e2e/**/*.cy.ts',
    supportFile: 'cypress/support/e2e.ts',
  },
  video: false,
  screenshotsFolder: 'cypress/screenshots',
  viewportWidth: 1366,
  viewportHeight: 768,
});
