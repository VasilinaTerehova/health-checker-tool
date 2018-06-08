import { Component, Input, Output, EventEmitter } from '@angular/core';

//Models
import { ClusterState } from '../../cluster/cluster-state.model';
import { Cluster } from '../../shared/cluster/cluster.model';
import { ClusterSnapshot } from '../../cluster/cluster-snapshot.model';
import { CheckHealthToken } from '../../cluster/health/check-health-token.model';
import { ServiceStatus } from '../../service/service-status.model';
//Services
import { ClusterHealthCheckService } from '../../cluster/health/cluster-health-check.service';
import { ErrorReportingService } from '../../shared/error/error-reporting.service';

@Component({
  selector: 'service-list',
  templateUrl: 'service-list.component.html',
})
export class ServiceListComponent {
  private _checkHealthToken: CheckHealthToken;
  isLoading: Boolean;
  serviceStatusList: ServiceStatus[];
  isAscSort: boolean = true;

  constructor( private clusterHealthCheckService: ClusterHealthCheckService, private errorReportingService: ErrorReportingService ) {}

  @Input()
  set checkHealthToken( checkHealthToken: CheckHealthToken ) {
    if ( checkHealthToken ) {
      this.isLoading = true;
      this._checkHealthToken = checkHealthToken;
      this.ascForClusterState();
    }
  }

  get clusterName(): String {
    return this._checkHealthToken.clusterName;
  }

  isShowServiceActionAllow(name: string) {
    return name && (name.toUpperCase() == "HBASE" || name.toUpperCase() == "HIVE");
  }

  restartService( serviceName: string ) {
    alert("Will be implemented using ssh strategy");
  }

  changeSort() {
    this.isAscSort = !this.isAscSort;
  }

  checkClusterHealth() {
    this.ascForClusterState();
  }

  private ascForClusterState() {
    this.errorReportingService.clearError();
    this.clusterHealthCheckService.getServicesClusterState( this._checkHealthToken.clusterName, this._checkHealthToken.token ).subscribe(
      data => {
        if ( data ) {
          this.serviceStatusList = data.filter( status => this.isNotYarnOrHDFS( status ) );
          this.isLoading = false;
        }
      },
      error => this.errorReportingService.reportHttpError( error )
    );
  }

  private isNotYarnOrHDFS( serviceStatus: ServiceStatus ): boolean {
    return serviceStatus && serviceStatus.displayName.toUpperCase().search("HDFS") == -1 && serviceStatus.displayName.toUpperCase().search("YARN") == -1;
  }
}
