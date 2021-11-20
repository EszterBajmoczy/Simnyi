import {RouterModule, Routes} from '@angular/router';

import {CaffComponent} from './caff/caff.component';
import {AuthComponent} from './auth/auth.component';
import {AuthGuard} from './_helpers/auth.guard';

const routes: Routes = [
  {path: '', component: CaffComponent, canActivate: [AuthGuard]},

  {path: 'login', component: AuthComponent},
  {path: '**', redirectTo: ''}
];

export const AppRoutingModule = RouterModule.forRoot(routes);
