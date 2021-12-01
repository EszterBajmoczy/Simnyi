import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first} from 'rxjs/operators';

import {AlertService} from '../_services/alert.service';
import {AuthenticationService} from '../_services/authentication.service';
import {MatNativeDateModule} from '@angular/material/core';
import {MatSidenav} from '@angular/material/sidenav';

//Zoli things
export interface CAFFTableTemplate {
  name: string;
  commentCount: number;
}

export interface CAFFTemplate {
  name: string;
  imgUrl: string;
  comments: string[];
}

const tmpData: CAFFTableTemplate[] = [
  {name: 'abc1', commentCount: 2},
  {name: 'asd$3', commentCount: 12}
];

const testCaffData: CAFFTemplate[] = [
  {name: 'abc1', imgUrl: "https://us.123rf.com/450wm/derplan/derplan1801/derplan180100031/94537767-continuous-one-line-drawing-light-bulb-symbol-idea-the-inscription-idea-.jpg?ver=6", comments: ['Gut', 'Very good']}
]

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {
  //Zoli things
  displayedColumns: string[] = ['name', 'comment count'];
  dataSource = tmpData;
  otherDataSource = testCaffData;

  // @ts-ignore
  loginForm: FormGroup;
  loading = false;
  registerLoading = false;
  submitted = false;
  returnUrl: string | undefined;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private alertService: AlertService
  ) {
    if (this.authenticationService.currentUserValue) {
      this.router.navigate(['/']);
    }
  }

  get f() {
    return this.loginForm.controls;
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  login() {
    this.submitted = true;

    this.alertService.clear();

    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authenticationService.login(this.loginForm.value)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.alertService.error(error);
          this.loading = false;
        });
  }

  register() {
    this.submitted = true;

    this.alertService.clear();

    if (this.loginForm.invalid) {
      return;
    }

    this.registerLoading = true;
    this.authenticationService.register(this.loginForm.value)
      .pipe(first())
      .subscribe(
        data => {
          this.alertService.success('Registration successful', true);
          this.router.navigate(['/login']);
        },
        error => {
          this.alertService.error(error);
          this.registerLoading = false;
        });
  }

}
