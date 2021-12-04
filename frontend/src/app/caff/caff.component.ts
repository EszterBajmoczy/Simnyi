import {Component, OnInit} from '@angular/core';

import {User} from '../_models/user';
import {AuthenticationService} from '../_services/authentication.service';
import {ActivatedRoute} from '@angular/router';
import {CaffService} from '../_services/caff.service';
import {Caff} from '../_models/caff';
import {first} from 'rxjs/operators';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CommentService} from '../_services/comment.service';
import {Comment} from '../_models/comment';

@Component({
  selector: 'app-caff',
  templateUrl: './caff.component.html',
  styleUrls: ['./caff.component.css']
})
export class CaffComponent implements OnInit {
  commentForm!: FormGroup;
  currentUser: User;
  id: string | undefined;
  bmp: Caff | undefined;
  error: any = {message: undefined};


  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private authenticationService: AuthenticationService,
    private caffService: CaffService,
    private commentService: CommentService,
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
    this.route.params.subscribe( params => this.id = params['id'] );
  }

  ngOnInit() {
    this.commentForm = this.formBuilder.group({
      comment: ['', Validators.required]
    });
    this.loadBmp()
  }

  get f() {
    return this.commentForm.controls;
  }

  private loadBmp() {
    this.caffService.getBmpByCaffId(this.id!!).subscribe(res => {this.bmp = res; console.log(res)});
  }

  comment() {
    if (this.commentForm.invalid || this.bmp == undefined) {
      return;
    }

    let comment = new Comment();
    comment.caffId = this.bmp.id;
    comment.nameOfUser = this.currentUser.username;
    comment.content = this.commentForm.value.comment;


    this.commentService.comment(comment)
      .pipe(first())
      .subscribe(
        data => {
          this.commentForm.reset();
          this.bmp?.comment.push(comment);
        },
        error => {
          console.log(error)
          this.error.message = error.message;
        });
  }
}
