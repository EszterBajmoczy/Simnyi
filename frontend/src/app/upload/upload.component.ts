import {Component, OnInit} from '@angular/core';
import {HttpEventType, HttpResponse} from '@angular/common/http';
import {CaffService} from '../_services/caff.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  uploadForm!: FormGroup;
  selectedFile?: File;
  fileContent?: string;
  progress: { percentage: number } = { percentage: 0 };
  error: any = {message: undefined};

  fileName = '';

  constructor(private formBuilder: FormBuilder, private caffService: CaffService){}

  ngOnInit() {
    this.uploadForm = this.formBuilder.group({
      imageName: ['', Validators.required],
      file: ['', Validators.required]
    });
  }

  upload() {
    if(!this.uploadForm.valid){
      return
    }

    this.caffService.upload(this.uploadForm.value.imageName, this.fileContent!).subscribe(resp => {
        this.selectedFile = undefined;
        this.fileName = '';
        this.uploadForm.reset();
      }, error => {
        this.error.message = error;
      });

  }

  selectFile(event: any) {
    this.selectedFile = event.target.files[0];
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      this.fileContent = <string> reader.result;
      console.log(this.fileContent);
    };
    if (this.selectedFile)
      this.fileName = this.selectedFile.name;
  }

}
