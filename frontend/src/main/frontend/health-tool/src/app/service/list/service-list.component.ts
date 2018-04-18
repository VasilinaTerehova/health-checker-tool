import { Component, Input, Output, EventEmitter } from '@angular/core';

//Models
import { ClusterState } from '../../cluster/cluster-state.model';
import { Cluster } from '../../shared/cluster/cluster.model';
import { ClusterSnapshot } from '../../cluster/cluster-snapshot.model';
//Services
import { ClusterHealthCheckService } from '../../cluster/health/cluster-health-check.service';
import { ErrorReportingService } from '../../shared/error/error-reporting.service';

@Component({
  selector: 'service-list',
  templateUrl: 'service-list.component.html',
})
export class ServiceListComponent {
  private _clusterName: string;
  clusterState: ClusterState;
  isAscSort: boolean = true;

  constructor( private clusterHealthCheckService: ClusterHealthCheckService, private errorReportingService: ErrorReportingService ) {}

  @Input()
  set clusterName( clusterName: String ) {
    if ( clusterName ) {
      this._clusterName = clusterName.toString();
      this.ascForClusterState();
    }
  }

  get clusterName(): String {
    return this._clusterName;
  }

  isShowServiceActionAllow(name: string) {
    return name && (name.toUpperCase() == "HBASE" || name.toUpperCase() == "HIVE");
  }

  isShowLogsLocationAllow(state: string) {
    return state && (state.toUpperCase() == "BAD" || state.toUpperCase() == "DISABLED");
  }

  restartService( serviceName: string ) {

  }

  changeSort() {
    this.isAscSort = !this.isAscSort;
  }

  private ascForClusterState() {
    this.errorReportingService.clearError();
    this.clusterHealthCheckService.getServicesClusterState( this._clusterName ).subscribe(
      data => {
        if ( data ) {
          this.clusterState = data.clusterHealthSummary;
        }
      },
      error => this.errorReportingService.reportHttpError( error )
    );
  }
}
