import { Component, Input } from '@angular/core';

import { NodeFs } from './node-fs.model';
import { HdfsUsage } from './hdfs.model';
import { Memory } from './memory.model';

@Component({
  selector: 'common-cluster-health',
  templateUrl: 'common-cluster-health.component.html',
})
export class CommonClusterHealthComponent {

  isCollapsed: boolean;
  @Input() nodes: NodeFs[];
  @Input() memory: Memory;
  @Input() hdfsUsage: HdfsUsage;
  @Input() yarnAppsCount: number;

  calcMoreUsedDisk(): any {
    return this.nodes ? this.nodes.map(node => {
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
}
