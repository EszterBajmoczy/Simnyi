import {Component, OnInit} from '@angular/core';

import {User} from '../_models/user';
import {AuthenticationService} from '../_services/authentication.service';
import {ActivatedRoute, Router} from '@angular/router';
import {CaffService} from '../_services/caff.service';
import {Caff} from '../_models/caff';
import {first} from 'rxjs/operators';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CommentService} from '../_services/comment.service';
import {CommentDto} from '../_models/commentDto';
import {DomSanitizer} from '@angular/platform-browser';

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
  image: any;
  caff: any;
  error: any = {message: undefined};
  editingMode = false;


  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private caffService: CaffService,
    private commentService: CommentService,
    private sanitizer: DomSanitizer
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
    this.route.params.subscribe( params => this.id = params['id'] );
  }

  ngOnInit() {
    this.commentForm = this.formBuilder.group({
      comment: ['', Validators.required]
    });
    this.loadBmp()
    this.downloadCaff()
  }

  get f() {
    return this.commentForm.controls;
  }

  private loadBmp() {
    this.caffService.getBmpByCaffId(this.id!!).subscribe(res => {
      this.bmp = res;
      let objectURL = 'data:image/bmp;base64,' + this.bmp.content;
      this.image = this.sanitizer.bypassSecurityTrustUrl(objectURL);
    });
  }

  downloadCaff() {
    this.caffService.downloadCaffById(this.id!).subscribe(res => {
      let objectURL = 'data:application/octet-stream;base64,' + res;
      this.caff = this.sanitizer.bypassSecurityTrustUrl(objectURL);
    });
  }

  comment() {
    if (this.commentForm.invalid || this.bmp == undefined) {
      return;
    }

    let comment = new CommentDto();
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
          this.error.message = error;
        });
  }

  delete() {
    this.caffService.delete(this.id!).subscribe(res => {
      this.router.navigate(['/']);
    });
  }

  modify() {
    if(this.bmp!.name.length == 0)
      return;
    this.caffService.modify({id: this.bmp!.id, name: this.bmp!.name, comment: this.bmp!.comment}).subscribe(res => {
      this.editingMode = false;
    }, error =>{
      console.log(error)
    });
  }

  remove(comment: CommentDto) {
    const index =  this.bmp!.comment.indexOf(comment, 0);
    if (index > -1) {
      this.bmp!.comment.splice(index, 1);
    }
    this.modify();
  }
}
