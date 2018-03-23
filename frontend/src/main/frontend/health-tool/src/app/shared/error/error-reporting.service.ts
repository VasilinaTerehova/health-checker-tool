import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { HttpErrorResponse } from '@angular/common/http';

import { ErrorAlert } from './error-alert.model';
import { AlertType } from './alert-type.model';

@Injectable()
export class ErrorReportingService {
  private errorMessage = new Subject<ErrorAlert>();
  private isClearError = new Subject<boolean>();
  //Chanels
  errorMessage$ = this.errorMessage.asObservable();
  isClearError$ = this.isClearError.asObservable();

  constructor() {  }

  reportError( errorMessage: ErrorAlert ) {
    this.errorMessage.next( errorMessage );
  }

  reportHttpError( error: HttpErrorResponse ) {
    if ( error.error instanceof ErrorEvent ) {
      this.errorMessage.next( new ErrorAlert( error.error.message, AlertType.DANGER ) );
    }
    else {
      this.errorMessage.next( new ErrorAlert( error.message, AlertType.DANGER ) );
    }
  }

  clearError() {
    this.isClearError.next( true );
  }
}
