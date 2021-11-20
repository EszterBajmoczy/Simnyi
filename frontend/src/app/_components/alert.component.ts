import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';

import {AlertService} from '../_services/alert.service';

@Component({selector: 'alert', templateUrl: 'alert.component.html'})
export class AlertComponent implements OnInit, OnDestroy {
  message: any;
  private subscription: Subscription;

  constructor(private alertService: AlertService) {
    this.subscription = this.alertService.getAlert()
      .subscribe(message => {
        switch (message && message.type) {
          case 'success':
            message.class = 'alert alert-success';
            break;
          case 'error':
            message.class = 'alert alert-error';
            break;
        }

        this.message = message;
      });
  }

  ngOnInit() {

  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
