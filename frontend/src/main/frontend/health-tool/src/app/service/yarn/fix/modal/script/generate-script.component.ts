import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

//Services
import { ServiceFixService } from '../../../../../service/service-fix.service';
import { ErrorReportingService } from '../../../../../shared/error/error-reporting.service';

@Component({
  selector: 'generate-script',
  templateUrl: 'generate-script.component.html',
})
export class GenerateScriptComponent implements OnInit {
  operationName: String;
  clusterName: String;
  serviceName: String;
  isGenerating: boolean;
  private _bashScriptLink: String;

  constructor( public bsModalRef: BsModalRef, private _serviceFixService: ServiceFixService, private _errorReportingService: ErrorReportingService ) {  }

  ngOnInit() {
    this.isGenerating = true;
    this._serviceFixService.generateFixBashScript( this.clusterName.toString(), this.serviceName.toString() ).subscribe(
      data => this.bashScriptLink = data.value,
      error => this.reportError( error )
    )
  }

  set bashScriptLink( bashScriptLink: String ) {
    this.isGenerating = false;
    this._bashScriptLink = bashScriptLink;
  }

  get bashScriptLink(): String {
    return this._bashScriptLink;
  }

  close() {
    this.bsModalRef.hide();
  }

  private reportError( error: any ) {
    this._errorReportingService.reportHttpError( error );
    this.bsModalRef.hide();
  }
}
