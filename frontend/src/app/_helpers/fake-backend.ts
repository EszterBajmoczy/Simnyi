import {Injectable} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {delay, dematerialize, materialize, mergeMap} from 'rxjs/operators';

import {User} from '../_models/user';

//TODO Fake backend for FE testing
let users = JSON.parse(<string> localStorage.getItem('users')) || [];

@Injectable()
export class FakeBackendInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const {url, method, headers, body} = request;

    return of(null)
      .pipe(mergeMap(handleRoute))
      .pipe(materialize())
      .pipe(delay(500))
      .pipe(dematerialize());

    function handleRoute() {
      switch (true) {
        case url.endsWith('/user/authenticate') && method === 'POST':
          return authenticate();
        case url.endsWith('/user/register') && method === 'POST':
          return register();
        case url.endsWith('/user') && method === 'GET':
          return getUsers();
        case url.match(/\/user\/\d+$/) && method === 'DELETE':
          return deleteUser();
        default:
          return next.handle(request);
      }
    }

    function authenticate() {
      const {username, password} = body;
      const user = users.find((x: User) => x.username === username && x.password === password);
      if (!user) {
        return error('Username or password is incorrect');
      }
      return ok({
        username: user.username,
        token: 'fake-jwt-token'
      });
    }

    function register() {
      const user = body;

      if (users.find((x: User) => x.username === user.username)) {
        return error('Username "' + user.username + '" is already taken');
      }

      users.push(user);
      localStorage.setItem('users', JSON.stringify(users));

      return ok();
    }

    function getUsers() {
      if (!isLoggedIn()) {
        return unauthorized();
      }
      return ok(users);
    }

    function deleteUser() {
      if (!isLoggedIn()) {
        return unauthorized();
      }

      users = users.filter((x: User) => x.username != nameFromURL());
      localStorage.setItem('users', JSON.stringify(users));
      return ok();
    }

    function ok(body?: any) {
      return of(new HttpResponse({status: 200, body}));
    }

    function error(message: any) {
      return throwError({error: {message}});
    }

    function unauthorized() {
      return throwError({status: 401, error: {message: 'Unauthorised'}});
    }

    function isLoggedIn() {
      return headers.get('Authorization') === 'Bearer fake-jwt-token';
    }

    function nameFromURL() {
      const urlParts = url.split('/');
      return urlParts[urlParts.length - 1];
    }
  }
}

export const fakeBackendProvider = {
  provide: HTTP_INTERCEPTORS,
  useClass: FakeBackendInterceptor,
  multi: true
};
