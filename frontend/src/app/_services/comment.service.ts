import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {globals} from '../_helpers/globals';

import {User} from '../_models/user';
import {CommentDto} from '../_models/commentDto';

@Injectable({providedIn: 'root'})
export class CommentService {
  public currentUser: Observable<User>;
  private currentUserSubject: BehaviorSubject<User>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(<string> localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  comment(comment: CommentDto) {
    return this.http.post(`${globals.apiUrl}/comment`, comment);
  }

}
