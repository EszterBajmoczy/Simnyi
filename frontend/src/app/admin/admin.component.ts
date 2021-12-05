import {Component, OnInit, ViewChild} from '@angular/core';
import {MatAccordion} from '@angular/material/expansion';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../_services/authentication.service';
import {AlertService} from '../_services/alert.service';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  // @ts-ignore
  @ViewChild(MatAccordion) accordion: MatAccordion;
  registerForm!: FormGroup;
  removeForm!: FormGroup;
  error: any = {usernameConflict: false, notSecurePassword: false, message: {register: undefined, remove: undefined}};

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
    this.removeForm = this.formBuilder.group({
      username: ['', Validators.required]
    });
  }

  get freg() {
    return this.registerForm.controls;
  }

  get frem() {
    return this.removeForm.controls;
  }

  registerAdmin() {
    this.resetErrors();

    if (this.registerForm.invalid) {
      return;
    }

    this.authenticationService.registerAdmin(this.registerForm.value)
      .pipe(first())
      .subscribe(
        data => {
          this.alertService.success('Registration successful', true);
          this.registerForm.reset();
          Object.keys(this.removeForm.controls).forEach(key => {
            this.registerForm.get(key)?.setErrors(null) ;
          });
        },
        error => {
          if(error.startsWith("409"))
            this.error.usernameConflict = true;
          else if(error.startsWith("400"))
            this.error.notSecurePassword = true;
          else
            this.error.message.register = error;
        });
  }

  removeUser() {
    this.resetErrors();

    if (this.removeForm.invalid) {
      return;
    }

    this.authenticationService.delete(this.removeForm.value.username)
      .pipe(first())
      .subscribe(
        data => {
          this.alertService.success('User successfully removed!', true);
          this.removeForm.reset();
          Object.keys(this.removeForm.controls).forEach(key => {
            this.removeForm.get(key)?.setErrors(null) ;
          });
        },
        error => {
            this.error.message.remove = error;
        });
  }

  private resetErrors() {
    this.error.message.register = undefined;
    this.error.message.remove = undefined;
    this.error.usernameConflict = false;
    this.error.notSecurePassword = false;
  }


}
