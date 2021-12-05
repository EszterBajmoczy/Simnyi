import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first} from 'rxjs/operators';

import {AlertService} from '../_services/alert.service';
import {AuthenticationService} from '../_services/authentication.service';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  registerLoading = false;
  submitted = false;
  returnUrl: string | undefined;
  error: any = {loginInvalid: false, usernameConflict: false, notSecurePassword: false, message: undefined};

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
    this.resetErrors();

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
          this.error.message = error;
          this.loading = false;
        });
  }

  register() {
    this.submitted = true;
    this.resetErrors();

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
          if(error.startsWith("409"))
            this.error.usernameConflict = true;
          else if(error.startsWith("400"))
            this.error.notSecurePassword = true;
          else
            this.error.message = error;
          this.registerLoading = false;
        });
  }

  private resetErrors() {
    this.error.message = undefined;
    this.error.loginInvalid = false;
    this.error.usernameConflict = false;
    this.error.notSecurePassword = false;
  }

}
