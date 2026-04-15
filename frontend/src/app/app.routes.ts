import { Routes } from '@angular/router';
import { AdminShellContainerComponent } from './features/admin/admin-shell.container';
import { EmpleadoLoginContainerComponent } from './features/empleado/empleado-login.container';

export const routes: Routes = [
	{
		path: 'empleado/login',
		component: EmpleadoLoginContainerComponent,
	},
	{
		path: '',
		component: AdminShellContainerComponent,
	},
];
