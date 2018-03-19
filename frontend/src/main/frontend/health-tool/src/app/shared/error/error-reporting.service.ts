import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class ErrorReportingService {
  private errorMessage = new Subject<string>();
  private isClearError = new Subject<boolean>();
  //Chanels
  errorMessage$ = this.errorMessage.asObservable();
  isClearError$ = this.isClearError.asObservable();

  constructor() {  }

  reportError( errorMessage: string ) {
    this.errorMessage.next( errorMessage );
  }

  reportHttpError( error: HttpErrorResponse ) {
    if ( error.error instanceof ErrorEvent ) {
      this.errorMessage.next( error.error.message );
    }
    else {
      this.errorMessage.next( error.message );
    }
  }

  clearError() {
    this.isClearError.next( true );
  }
}
