import { Component } from '@angular/core';
import {HttpEventType, HttpResponse} from '@angular/common/http';
import {CaffService} from '../_services/caff.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent {
  title = 'File-Upload-Save';
  selectedFiles?: FileList;
  currentFileUpload?: File;
  progress: { percentage: number } = { percentage: 0 };
  selectedFile = null;
  changeImage = false;
  name: string = "";

  constructor(private caffService: CaffService){}

 /* downloadFile(){
    const link = document.createElement('a');
    link.setAttribute('target', '_blank');
    link.setAttribute('href', '_File_Saved_Path');
    link.setAttribute('download', 'file_name.pdf');
    document.body.appendChild(link);
    link.click();
    link.remove();
  }*/
  change($event : any) {
    this.changeImage = true;
  }
  changedImage(event: any) {
    this.selectedFile = event.target.files[0];
  }
  upload() {
    this.progress.percentage = 0;
    // @ts-ignore
    this.currentFileUpload = this.selectedFiles.item(0);
    this.caffService.upload(this.name, this.currentFileUpload!!).subscribe(resp => {
        if (resp.type === HttpEventType.UploadProgress) {
          this.progress.percentage = Math.round(100 * resp.loaded / 100);
        } else if (resp instanceof HttpResponse) {
          alert('File Successfully Uploaded');
        }
        this.selectedFiles = undefined;
      }
    );
  }
  selectFile(event: any) {
    this.selectedFiles = event.target.files;
  }

}
