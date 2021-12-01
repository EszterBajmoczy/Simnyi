import { Component, OnInit } from '@angular/core';
import {User} from '../_models/user';
import {CaffDto} from '../_models/caffDto';
import {AuthenticationService} from '../_services/authentication.service';
import {CaffService} from '../_services/caff.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  currentUser: User;
  bmps: CaffDto[] | undefined;

  constructor(
    private authenticationService: AuthenticationService,
    private caffService: CaffService,
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
  }

  ngOnInit() {
    this.loadAllBmp();
  }

  private loadAllBmp() {
    this.caffService.getTmpAllBmp().subscribe(res => this.bmps = res);
    console.log(this.bmps)
  }
}
