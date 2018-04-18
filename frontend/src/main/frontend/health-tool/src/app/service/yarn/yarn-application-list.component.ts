import { Component, Input, Output, EventEmitter } from '@angular/core';

//Models
import { YarnApplication } from './application.model';
//Services
import { YarnApplicationService } from './yarn-application.service';
import { ErrorReportingService } from '../../shared/error/error-reporting.service';

@Component({
  selector: 'yarn-application-list',
  templateUrl: 'yarn-application-list.component.html',
})
export class YarnApplicationListComponent {
  @Output() onYarnAppsChange = new EventEmitter<number>();
  yarnApps: YarnApplication[];

  constructor( private yarnApplicationService: YarnApplicationService, private errorReportingService: ErrorReportingService ) {}

  @Input()
  set clusterName( clusterName: String ) {
    if ( clusterName ) {
      this.askForYarnAppsList( clusterName.toString() );
    }
  }

  private askForYarnAppsList( clusterName: string ) {
    this.yarnApplicationService.getYarnApps( clusterName ).subscribe(
      data => {
         this.yarnApps = data;
         this.onYarnAppsChange.emit( this.yarnApps.length );
      },
      error => this.errorReportingService.reportHttpError( error )
    )
  }
}
