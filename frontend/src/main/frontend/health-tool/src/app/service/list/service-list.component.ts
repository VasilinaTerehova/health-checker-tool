import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

//Models
import { ClusterState } from '../../cluster/cluster-state.model';
import { Cluster } from '../../shared/cluster/cluster.model';
import { ClusterSnapshot } from '../../cluster/cluster-snapshot.model';
//Services
import { ClusterHealthCheckService } from '../../cluster/health/cluster-health-check.service';
import { ErrorReportingService } from '../../shared/error/error-reporting.service';
import { RouteService } from '../../shared/menu/side/route.service';

@Component({
  selector: 'service-list',
  templateUrl: 'service-list.component.html',
})
export class ServiceListComponent {
  private _clusterName: string;
  @Output() onClusterSnapshotChange = new EventEmitter<ClusterSnapshot>();
  clusterState: ClusterState;
  isAscSort: boolean = true;

  constructor( private clusterHealthCheckService: ClusterHealthCheckService, private errorReportingService: ErrorReportingService ) {}

  @Input()
  set clusterName( clusterName: string ) {
    if ( clusterName ) {
      this._clusterName = clusterName;
      this.ascForClusterState();
    }
  }

  get clusterName(): string {
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
          this.onClusterSnapshotChange.emit( this.clusterState.cluster );
        }
      },
      error => this.errorReportingService.reportHttpError( error )
    );
  }
}
