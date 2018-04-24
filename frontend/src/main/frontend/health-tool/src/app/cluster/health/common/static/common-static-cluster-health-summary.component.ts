import { Component, Input } from '@angular/core';

import { ClusterState } from '../../../../cluster/cluster-state.model';
import { NodeFs } from '../node-fs.model';
import { HdfsUsage } from '../hdfs.model';
import { Memory } from '../memory.model';

@Component({
  selector: 'common-static-cluster-health-summary',
  templateUrl: 'common-static-cluster-health-summary.component.html',
})
export class CommonStaticClusterHealthSummaryComponent {

  @Input() clusterState: ClusterState;

  get nodes(): NodeFs[] {
    return this.clusterState.cluster.nodes;
  }

  get hdfsUsage(): HdfsUsage {
    return this.clusterState.cluster.hdfsUsage;
  }

  get memory(): Memory {
    return this.clusterState.cluster.memoryUsage;
  }
}
