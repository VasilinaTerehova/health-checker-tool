import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { Subscription } from 'rxjs/Subscription';
import { Subject } from 'rxjs/Subject';

//Components
import { ConfirmAutomaticFixComponent } from './modal/confirm/confirm-automatic-fix.component';
import { GenerateScriptComponent } from './modal/script/generate-script.component';
//Models
import { Credentials } from '../../../shared/cluster/credentials.model';
import { ServiceFixResult } from '../../../service/service-fix-result.model';
import { ErrorAlert } from '../../../shared/error/error-alert.model';
import { AlertType } from '../../../shared/error/alert-type.model';
//Services
import { ServiceFixService } from '../../../service/service-fix.service';
import { ErrorReportingService } from '../../../shared/error/error-reporting.service';

@Component({
  selector: 'service-fix',
  templateUrl: './service-fix.component.html',
})
export class ServiceFixComponent implements OnInit {
  @Input("service-name") serviceName: String;
  @Input("operation-name") operationName: String;
  isPerforming: Boolean;
  bsModalRef: BsModalRef;
  private _clusterName: String;
  private _yarnServiceFixSteps: String[];
  private _serviceFixResult: ServiceFixResult;

  constructor( private modalService: BsModalService, private _serviceFixService: ServiceFixService, private _errorReportingService: ErrorReportingService ) {  }

  ngOnInit() {
    //Request when service and cluster names are set
    this.requestFixSteps( this._clusterName );
  }

  @Input("cluster-name")
  set clusterName( clusterName: String ) {
    if ( clusterName ) {
      //Don't request two times
      if ( this._clusterName ) {
        this.requestFixSteps( clusterName );
      }
      this._clusterName = clusterName;
    }
  }

  set serviceFixResult( serviceFixResult: ServiceFixResult ) {
    this.isPerforming = false;
    if ( serviceFixResult ) {
      this._serviceFixResult = serviceFixResult;
      this._errorReportingService.reportError( this.createAlert( serviceFixResult ) );
    }
  }

  get yarnServiceFixSteps(): Array<String> {
    return this._yarnServiceFixSteps;
  }

  get clusterName(): String {
    return this._clusterName;
  }

  performAutomaticFix() {
    if ( !this.isPerforming ) {
      var initialState = {
        "operationName": this.operationName,
        "sshCredentialsChannel": new Subject<Credentials>()
      };

      var changedSubscription: Subscription = initialState.sshCredentialsChannel.asObservable().subscribe(
        result => {
          if ( result ) {
            this.performFix( result );
          }

          changedSubscription.unsubscribe();
        }
      );
      this.bsModalRef = this.modalService.show( ConfirmAutomaticFixComponent, {initialState});
    }
  }

  generateScript() {
    if ( !this.isPerforming ) {
      var initialState = {
        "operationName": this.operationName,
        "clusterName": this.clusterName,
        "serviceName": this.serviceName
      };

      this.bsModalRef = this.modalService.show( GenerateScriptComponent, {initialState});
    }
  }

  private requestFixSteps( clusterName: String ) {
    if ( clusterName && this.serviceName ) {
      this._serviceFixService.getFixstepList( clusterName.toString(), this.serviceName.toString() ).subscribe(
        data => this._yarnServiceFixSteps = data,
        error => this._errorReportingService.reportHttpError( error )
      )
    }
  }

  private performFix( sshCredentials: Credentials ) {
    if ( this.clusterName && this.serviceName && sshCredentials && sshCredentials.username && sshCredentials.password ) {
      this.isPerforming = true;
      this._serviceFixService.fixService( this.clusterName.toString(), this.serviceName.toString(), sshCredentials.username.toString(), sshCredentials.password.toString() ).subscribe(
        data => this.serviceFixResult = data,
        error => {
          this.isPerforming = false;
          this._errorReportingService.reportHttpError( error );
        }
      )
    }
  }

  private createAlert( serviceFixResult: ServiceFixResult ): ErrorAlert {
    return serviceFixResult.fixed ? new ErrorAlert( "Operation " + this.operationName + " performed successfully!", AlertType.SUCCESS )
      : new ErrorAlert( "Operation " + this.operationName + " failed! Error - " + serviceFixResult.alert, AlertType.DANGER );
  }
}
