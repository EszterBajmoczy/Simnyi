import {Component, OnInit} from '@angular/core';

import {User} from '../_models/user';
import {AuthenticationService} from '../_services/authentication.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-caff',
  templateUrl: './caff.component.html',
  styleUrls: ['./caff.component.css']
})
export class CaffComponent implements OnInit {
  currentUser: User;
  id: string | undefined;


  constructor(
    private route: ActivatedRoute,
    private authenticationService: AuthenticationService,
  ) {
    this.route.params.subscribe( params => this.id = params['id'] );
    this.currentUser = this.authenticationService.currentUserValue;
  }

  ngOnInit() {
  }

}
