import { Component, Input } from '@angular/core';

import { Cluster } from '../../../shared/cluster/cluster.model';
import { NodeSummary } from '../node-summary.model';
import { NodeMemory } from '../node-memory-summary.model';

@Component({
  selector: 'common-cluster-health-summary',
  templateUrl: 'common-cluster-health-summary.component.html',
})
export class CommonClusterHealthSummaryComponent {
  @Input() cluster: Cluster;
  @Input() yarnAppsCount: number;
  nodes: NodeSummary[];

  constructor() {
    this.nodes = [
      new NodeSummary( "ontest1.com", new NodeMemory( 10000, 12000 ) ),
      new NodeSummary( "notest.com", new NodeMemory( 9000, 12000 ) ),
      new NodeSummary( "failtest.com", new NodeMemory( 7300, 12000 ) )
    ];
  }

  calcMoreUsedDisk(): any {
    return this.nodes.map( node => {
      return {
        "used": (node.memory.used * 100 / node.memory.total).toPrecision(2),
        "host": node.host
      }
    } ).sort( ( one, two  ) => {
      if ( one.used > two.used ) {
        return 1;
      }

      return -1;
    } ).pop();
  }
}
