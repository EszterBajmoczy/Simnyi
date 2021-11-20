import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {globals} from '../_helpers/globals';

import {User} from '../_models/user';

@Injectable({providedIn: 'root'})
export class AuthenticationService {
  public currentUser: Observable<User>;
  private currentUserSubject: BehaviorSubject<User>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(<string> localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  register(user: User) {
    return this.http.post(`${globals.apiUrl}/user/register`, user);
  }

  delete(name: string) {
    return this.http.delete(`${globals.apiUrl}/user/${name}`);
  }

  getAll() {
    return this.http.get<User[]>(`${globals.apiUrl}/user`);
  }

  login(username: string, password: string) {
    return this.http.post<any>(`${globals.apiUrl}/user/authenticate`, {username, password})
      .pipe(map(user => {
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
        return user;
      }));
  }

  logout() {
    localStorage.removeItem('currentUser');
    //TODO remove user token from db

    // @ts-ignore
    this.currentUserSubject.next(null);
  }
}
