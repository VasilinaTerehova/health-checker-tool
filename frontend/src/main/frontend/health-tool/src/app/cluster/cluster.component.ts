import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { ClusterService } from './cluster.service';
import { Cluster } from '../shared/cluster/cluster.model';

@Component({
  selector: 'cluster-info',
  templateUrl: './cluster.component.html',
})
export class ClusterComponent {
  cluster: Cluster;
  yarnAppsCount: number;

  constructor( private router: Router, private route: ActivatedRoute ) {}

  onYarnAppsCountChange( newYarnAppsCount: number ) {
    this.yarnAppsCount = newYarnAppsCount;
  }

  onClusterChange( newCluster: Cluster ) {
    this.cluster = newCluster;
  }
}
