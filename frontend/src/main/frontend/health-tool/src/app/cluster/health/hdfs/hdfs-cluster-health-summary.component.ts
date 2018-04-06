import { Component, Input } from '@angular/core';

import { Cluster } from '../../../shared/cluster/cluster.model';
import { JobExample } from '../job-example.model';
import { HdfsHealthReport } from './hdfs-health-report.model';

@Component({
  selector: 'hdfs-cluster-health-summary',
  templateUrl: 'hdfs-cluster-health-summary.component.html',
})
export class HdfsClusterHealthSummaryComponent {
  @Input() serviceName: string;
  @Input() hdfsHealthReport: HdfsHealthReport;
  isCollapsed: boolean;

  constructor() {
    this.hdfsHealthReport = new HdfsHealthReport( "BAD", "/nm/yarn", [
      new JobExample( "test", true, "" ),
      new JobExample( "more test", false, "Output path already exists" ),
      new JobExample( "fail test", false, "Kinit failed" ),
      new JobExample( "last", true, "" )
    ] );
  }

  isJobRunSuccessfully(): boolean {
    return this.hdfsHealthReport.jobResults.map( jobResult => jobResult.result ).filter( jobResult => !jobResult ).pop() == null;
  }

  getSuccessfullyRunJobsCount(): number {
    return this.hdfsHealthReport.jobResults.filter( jobResult => jobResult.result ).length;
  }

  getAlertsCount(): number {
    return this.getAlerts().length;
  }

  getAlerts(): string[] {
    return this.hdfsHealthReport.jobResults.filter( jobResult => !jobResult.result ).map( jobResult => jobResult.alert );
  }
}
