import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User} from '../_models/user';
import {AuthenticationService} from '../_services/authentication.service';
import {BehaviorSubject} from 'rxjs';
import {AlertService} from '../_services/alert.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: User;
  userForm!: FormGroup;
  error: any = {message: undefined};
  private currentUserSubject: BehaviorSubject<User>;
  username!: string;

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private alertService: AlertService
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(<string> localStorage.getItem('currentUser')));
  }

  ngOnInit(): void {
    this.userForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.minLength(10)]
    });
    this.username = this.currentUser.username;
  }

  get f() {
    return this.userForm.controls;
  }

  updateUser() {
    if (this.userForm.invalid) {
      return;
    }
    this.currentUser.username = this.userForm.value.username;
    if(this.userForm.value.password.length > 0)
       this.currentUser.password = this.userForm.value.password;
    else
      this.currentUser.password = null;
    this.authenticationService.userUpdate(this.currentUser).subscribe(res => {
      localStorage.setItem('currentUser', JSON.stringify(this.currentUser));
      this.currentUserSubject.next(this.currentUser);
      this.alertService.success('Profile data successfully changed!', true);
    }, error => {
      this.error.message = error;
    });
  }
}
