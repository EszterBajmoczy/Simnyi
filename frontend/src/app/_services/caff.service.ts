import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {globals} from '../_helpers/globals';

import {User} from '../_models/user';
import {CaffUpdateDto} from '../_models/CaffUpdateDto';

@Injectable({providedIn: 'root'})
export class CaffService {
  public currentUser: Observable<User>;
  private currentUserSubject: BehaviorSubject<User>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(<string> localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  upload(name: String, file: File) {
    return this.http.post(`${globals.apiUrl}/caff`, {name: name, file: file});
  }

  modify(dto : CaffUpdateDto) {
    return this.http.put(`${globals.apiUrl}/caff`, dto);
  }

  findCaffById(caffId : String) {
    return this.http.get(`${globals.apiUrl}/caff/${caffId}`);
  }

  getBmpByCaffId(caffId : String) {
    return this.http.get(`${globals.apiUrl}/public/caff/bmp/${caffId}`);
  }


  getAllBmp() {
    return this.http.get(`${globals.apiUrl}/public/caff/bmp`);
  }

  getTmpBmpByCaffId(caffId : String) {
    return this.http.get(`${globals.apiUrl}/public/caff/tmp/bmp/${caffId}`);
  }


  getTmpAllBmp() {
    return this.http.get(`${globals.apiUrl}/public/caff/tmp/bmp`);
  }

  getTmpBmpByCaffIdWithAuth(caffId : String) {
    return this.http.get(`${globals.apiUrl}/caff/tmp/bmp/${caffId}`);
  }

  getTmpAllBmpWithAuth() {
    return this.http.get(`${globals.apiUrl}/caff/tmp/bmp`);
  }

}
