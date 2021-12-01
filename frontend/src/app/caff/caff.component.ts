import {Component, OnInit} from '@angular/core';

import {User} from '../_models/user';
import {AuthenticationService} from '../_services/authentication.service';

@Component({
  selector: 'app-caff',
  templateUrl: './caff.component.html',
  styleUrls: ['./caff.component.css']
})
export class CaffComponent implements OnInit {
  currentUser: User;

  constructor(
    private authenticationService: AuthenticationService,
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
  }

  ngOnInit() {
  }

}
