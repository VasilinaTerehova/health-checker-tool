import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

//Models
import { ErrorAlert } from './error-alert.model';
//Services
import { ErrorReportingService } from './error-reporting.service';

@Component({
  selector: 'error-report',
  templateUrl: 'error-reporting.component.html',
})
export class ErrorReportingComponent implements OnDestroy, OnInit {
  private errorMessageSub: Subscription;
  private clearErrorSub: Subscription;
  errorAlerts: ErrorAlert[];

  constructor( private errorReportingService: ErrorReportingService ) {
    this.errorMessageSub = errorReportingService.errorMessage$.subscribe(
      errorMessage => this.errorAlerts.push( errorMessage )
    );
    this.clearErrorSub = errorReportingService.isClearError$.subscribe(
      isClearError => this.clearAlertArray()
    )
  }

  ngOnInit() {
    this.clearAlertArray();
  }

  ngOnDestroy() {
    this.errorMessageSub.unsubscribe();
    this.clearErrorSub.unsubscribe();
  }

  isErrorPresent(): boolean {
    return this.errorAlerts.length > 0;
  }

  onAlertClosed( dismissedAlert: ErrorAlert ) {
    this.errorAlerts = this.errorAlerts.filter( alert => alert !== dismissedAlert );
  }

  private clearAlertArray() {
    this.errorAlerts = new Array<ErrorAlert>();
  }
}
