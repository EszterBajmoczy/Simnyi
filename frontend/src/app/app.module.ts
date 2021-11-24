import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import {fakeBackendProvider} from './_helpers/fake-backend';

import {AppRoutingModule} from './app-routing.module';
import {JwtInterceptor} from './_helpers/jwt.interceptor';
import {ErrorInterceptor} from './_helpers/error.interceptor';
import {AppComponent} from './app.component';
import {CaffComponent} from './caff/caff.component';
import {AuthComponent} from './auth/auth.component';
import {AlertComponent} from './_components/alert.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    CaffComponent,
    AlertComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},

    //TODO Fake backend for FE testing
    fakeBackendProvider
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
