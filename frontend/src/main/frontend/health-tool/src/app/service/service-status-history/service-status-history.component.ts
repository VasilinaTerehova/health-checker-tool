import { Component, Input } from '@angular/core';

//Models
import { ClusterState } from '../../cluster/cluster-state.model';
import { Cluster } from '../../shared/cluster/cluster.model';
//Services
import { ClusterHealthCheckService } from '../../cluster/health/cluster-health-check.service';
import { ErrorReportingService } from '../../shared/error/error-reporting.service';

@Component({
  selector: 'service-status-history',
  templateUrl: 'service-status-history.component.html',
})
export class ServiceListHistoryComponent {
  clusterStateHistory: ClusterState[];

  constructor( private clusterHealthCheckService: ClusterHealthCheckService, private errorReportingService: ErrorReportingService ) {}

  @Input()
  set clusterName( clusterName: String ) {
    if ( clusterName ) {
      this.ascForClusterState( clusterName.toString() );
    }
  }

  private ascForClusterState( clusterName: string ) {
    this.errorReportingService.clearError();
    this.clusterHealthCheckService.getClusterStateHistory( clusterName ).subscribe(
      data => this.clusterStateHistory = data,
      error => this.errorReportingService.reportHttpError( error )
    );
  }
}
