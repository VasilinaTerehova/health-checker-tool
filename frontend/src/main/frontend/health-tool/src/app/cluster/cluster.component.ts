import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { ClusterService } from './cluster.service';
import { Cluster } from '../shared/cluster/cluster.model';
import { JobExample } from './health/job-example.model';
import { HdfsHealthReport } from './health/hdfs/hdfs-health-report.model';

@Component({
  selector: 'cluster-info',
  templateUrl: './cluster.component.html',
})
export class ClusterComponent {
  cluster: Cluster;
  yarnAppsCount: number;
  hdfsHealthReport: HdfsHealthReport;
  yarnHealthReport: HdfsHealthReport;

  constructor( private router: Router, private route: ActivatedRoute ) {
    this.hdfsHealthReport = new HdfsHealthReport( "BAD", "/nm/yarn", [
      new JobExample( "test", true, "" ),
      new JobExample( "more test", false, "Output path already exists" ),
      new JobExample( "fail test", false, "Kinit failed" ),
      new JobExample( "last", true, "" )
    ] );
    this.yarnHealthReport = new HdfsHealthReport( "GOOD", "/nm/yarn/logs", [
      new JobExample( "test", true, "" ),
      new JobExample( "more test", true, "" ),
      new JobExample( "fail test", true, "" ),
      new JobExample( "last", true, "" )
    ] );
  }

  onYarnAppsCountChange( newYarnAppsCount: number ) {
    this.yarnAppsCount = newYarnAppsCount;
  }

  onClusterChange( newCluster: Cluster ) {
    this.cluster = newCluster;
  }
}
