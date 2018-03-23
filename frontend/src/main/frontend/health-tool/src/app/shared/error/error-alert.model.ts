import { AlertType } from './alert-type.model';

export class ErrorAlert {
  constructor( public message: string, public type: AlertType ) {}
}
