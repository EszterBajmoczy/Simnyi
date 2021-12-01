import {RouterModule, Routes} from '@angular/router';

import {CaffComponent} from './caff/caff.component';
import {AuthComponent} from './auth/auth.component';
import {AuthGuard} from './_helpers/auth.guard';
import {MainComponent} from './main/main.component';
import {ProfileComponent} from './profile/profile.component';
import {UploadComponent} from './upload/upload.component';

const routes: Routes = [
  {path: '', component: MainComponent, canActivate: [AuthGuard]},
  {path: 'caff/:id', component: CaffComponent, canActivate: [AuthGuard]},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'new', component: UploadComponent, canActivate: [AuthGuard]},

  {path: 'login', component: AuthComponent},
  {path: '**', redirectTo: ''}
];

export const AppRoutingModule = RouterModule.forRoot(routes);
