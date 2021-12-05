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
  displayedColumns: string[] = ['name', 'comment count'];
  currentUser: User;
  allBmps: CaffDto[] | undefined;
  bmps: CaffDto[] | undefined;
  searchText: string = "";

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
    this.caffService.getAllBmp().subscribe(res => {
      this.allBmps = res;
      this.bmps = this.allBmps;
    });
  }

  search() {
    this.bmps = this.allBmps!.filter(item => item.name.includes(this.searchText))
  }
}
