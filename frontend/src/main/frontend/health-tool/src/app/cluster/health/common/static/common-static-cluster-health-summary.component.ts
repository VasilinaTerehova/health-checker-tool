import { Component, Input } from '@angular/core';

import { ClusterSnapshot } from '../../../../cluster/cluster-snapshot.model';

@Component({
  selector: 'common-static-cluster-health-summary',
  templateUrl: 'common-static-cluster-health-summary.component.html',
})
export class CommonStaticClusterHealthSummaryComponent {

  isCollapsed: boolean;

  @Input() clusterSnapshot: ClusterSnapshot;

  get cluster(): ClusterSnapshot {
    return this.clusterSnapshot;
  }

  calcMoreUsedDisk(): any {
    return this.isClusterSnapshotValid() ? this.clusterSnapshot.nodes.map(node => {
      return {
        "used": (node.usedGb * 100 / node.totalGb).toPrecision(2),
        "host": node.node
      }
    }).sort((one, two) => {
      if (one.used > two.used) {
        return 1;
      }

      return -1;
    }).pop() : 0;
  }

  private isClusterSnapshotValid(): boolean {
    if (this.clusterSnapshot && this.clusterSnapshot.nodes) {
      return true;
    }
    else {
      return false;
    }
  }

}
