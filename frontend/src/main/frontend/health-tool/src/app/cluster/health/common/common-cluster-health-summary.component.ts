import { Component, Input } from '@angular/core';

//Models
import { ClusterSnapshot } from '../../cluster-snapshot.model';
import { NodeSummary } from '../node-summary.model';
import { NodeMemory } from '../node-memory-summary.model';
import { CheckHealthToken } from '../check-health-token.model';
//Services
import { ClusterHealthCheckService } from '../cluster-health-check.service';
import { ErrorReportingService } from '../../../shared/error/error-reporting.service';

@Component({
  selector: 'common-cluster-health-summary',
  templateUrl: 'common-cluster-health-summary.component.html',
})
export class CommonClusterHealthSummaryComponent {
  private _cluster: ClusterSnapshot;
  @Input() yarnAppsCount: number;
  isCollapsed: boolean;
  isLoading: Boolean;

  constructor( private clusterHealthCheckService: ClusterHealthCheckService, private errorReportingService: ErrorReportingService ) {}

  @Input()
  set checkHealthToken( checkHealthToken: CheckHealthToken ) {
    if ( checkHealthToken ) {
      this.isLoading = true;
      this.askForFsClusterSnapshot( checkHealthToken );
    }
  }

  set cluster( clusterSnapshot: ClusterSnapshot ) {
    this.isLoading = false;
    this._cluster = clusterSnapshot;
  }

  get cluster(): ClusterSnapshot {
    return this._cluster;
  }

  calcMoreUsedDisk(): any {
    return this.isClusterSnapshotValid() ? this._cluster.nodes.map( node => {
      return {
        "used": (node.usedGb * 100 / node.totalGb).toPrecision(2),
        "host": node.node
      }
    } ).sort( ( one, two  ) => {
      if ( one.used > two.used ) {
        return 1;
      }

      return -1;
    } ).pop() : 0;
  }

  private isClusterSnapshotValid(): boolean {
    if ( this._cluster && this._cluster.nodes ) {
      return true;
    }
    else {
      return false;
    }
  }

  private askForFsClusterSnapshot( checkHealthToken: CheckHealthToken ) {
    this.clusterHealthCheckService.getFsClusterState( checkHealthToken.clusterName, checkHealthToken.token ).subscribe(
      data => this.cluster = data,
      error => this.errorReportingService.reportHttpError( error )
    )
  }
}
