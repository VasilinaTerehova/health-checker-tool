import { Component, Input, Output, EventEmitter } from '@angular/core';

//Models
import { YarnApplication } from './application.model';
import { CheckHealthToken } from '../../cluster/health/check-health-token.model';
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
  set checkHealthToken( checkHealthToken: CheckHealthToken ) {
    if ( checkHealthToken ) {
      this.askForYarnAppsList( checkHealthToken );
    }
  }

  private askForYarnAppsList( checkHealthToken: CheckHealthToken ) {
    this.yarnApplicationService.getYarnApps( checkHealthToken.clusterName, checkHealthToken.token ).subscribe(
      data => {
         this.yarnApps = data;
         this.onYarnAppsChange.emit( this.yarnApps.length );
      },
      error => this.errorReportingService.reportHttpError( error )
    )
  }
}
