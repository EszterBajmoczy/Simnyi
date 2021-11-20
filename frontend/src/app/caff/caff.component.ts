import {Component, OnInit} from '@angular/core';
import {first} from 'rxjs/operators';

import {User} from '../_models/user';
import {AuthenticationService} from '../_services/authentication.service';

@Component({
  selector: 'app-caff',
  templateUrl: './caff.component.html',
  styleUrls: ['./caff.component.css']
})
export class CaffComponent implements OnInit {
  currentUser: User;
  users: User[] | undefined;

  constructor(
    private authenticationService: AuthenticationService,
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
  }

  ngOnInit() {
    this.loadAllUsers();
  }

  //TODO fix (CORS policy)
  deleteUser(name: string) {
    this.authenticationService.delete(name)
      .pipe(first())
      .subscribe(() => this.loadAllUsers());
  }

  private loadAllUsers() {
    this.authenticationService.getAll()
      .pipe(first())
      .subscribe(users => this.users = users);
  }
}
