import {Injectable} from '@angular/core';
import {HttpClient,  HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {globals} from '../_helpers/globals';

import {User} from '../_models/user';
import {CaffUpdateDto} from '../_models/caffUpdateDto';
import {CaffDto} from '../_models/caffDto';
import {Caff} from '../_models/caff';

@Injectable({providedIn: 'root'})
export class CaffService {
  public currentUser: Observable<User>;
  private currentUserSubject: BehaviorSubject<User>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(<string> localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  upload(name: String, file: String) {
    const headers = new HttpHeaders({ enctype: 'multipart/form-data' });

    // @ts-ignore
    return this.http.post<any>(`${globals.apiUrl}/caff`, btoa(file), {headers: headers, params: {name: name}});
  }

  modify(dto : CaffUpdateDto) {
    return this.http.put(`${globals.apiUrl}/caff/modify`, dto);
  }

  delete(caffId : String) {
    return this.http.delete(`${globals.apiUrl}/caff/delete/${caffId}`);
  }

  downloadCaffById(caffId : String) {
    // @ts-ignore
    return this.http.get<String>(`${globals.apiUrl}/caff/${caffId}`,{responseType: 'text'});
  }

  getBmpByCaffId(caffId : String) {
    return this.http.get<Caff>(`${globals.apiUrl}/caff/bmp/${caffId}`);
  }

  getAllBmp() {
    return this.http.get<CaffDto[]>(`${globals.apiUrl}/caff/bmp`);
  }

  //unused
  searchAllBmp(searchInName : String) {
    return this.http.get<CaffDto[]>(`${globals.apiUrl}/caff/bmp/search/${searchInName}`);
  }

  /* unused
  getTmpBmpByCaffId(caffId: string) {
    return this.http.get<Caff>(`${globals.apiUrl}/public/caff/tmp/bmp/${caffId}`);
  }

  getTmpAllBmp() {
    return this.http.get<CaffDto[]>(`${globals.apiUrl}/public/caff/tmp/bmp`);
  }

  getTmpBmpByCaffIdWithAuth(caffId : String) {
    return this.http.get(`${globals.apiUrl}/caff/tmp/bmp/${caffId}`);
  }

  getTmpAllBmpWithAuth() {
    return this.http.get(`${globals.apiUrl}/caff/tmp/bmp`);
  }
*/
}
