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

  login(req: User) {
    return this.http.post<any>(`${globals.apiUrl}/public/user/login`, req, {observe: "response"})
      .pipe(map(resp => {
        const user = new User();
        user.username = req.username;
        user.token = resp.headers.get('Authorization');
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
        return user;
      }));
  }

  register(user: User) {
    return this.http.post(`${globals.apiUrl}/public/user/register`, user);
  }

  userUpdate(user: User) {
    return this.http.patch(`${globals.apiUrl}/user/user-update`, user);
  }

  passwordUpdate(password: string){
    return this.http.patch(`${globals.apiUrl}/user/password-update`, password);
  }

  deleteMe() {
    return this.http.delete(`${globals.apiUrl}/user}`);
  }

  delete(name: string) {
    return this.http.delete(`${globals.apiUrl}/user/${name}`);
  }

  registerAdmin(user: User) {
    return this.http.post(`${globals.apiUrl}/admin/register`, user);
  }


  logout() {
    localStorage.removeItem('currentUser');
    //TODO remove user token from db

    // @ts-ignore
    this.currentUserSubject.next(null);
  }
}
