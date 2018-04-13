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
  private _hdfsHealthReport: HdfsHealthReport;
  isCollapsed: boolean;

  @Input()
  set hdfsHealthReport( hdfsHealthReport: HdfsHealthReport ) {
    if ( hdfsHealthReport ) {
      this._hdfsHealthReport = hdfsHealthReport;
    }
    else {
      this._hdfsHealthReport = new HdfsHealthReport( "", "", new Array<JobExample>());
    }
  }

  get hdfsHealthReport(): HdfsHealthReport {
    return this._hdfsHealthReport;
  }

  isJobRunSuccessfully(): boolean {
    return this.hdfsHealthReport.jobResults.map( jobResult => jobResult.success ).filter( jobResult => !jobResult ).pop() == null;
  }

  getSuccessfullyRunJobsCount(): number {
    return this.hdfsHealthReport.jobResults.filter( jobResult => jobResult.success ).length;
  }

  getAlertsCount(): number {
    return this.getAlerts().length;
  }

  getAlerts(): string[] {
    return this.hdfsHealthReport.jobResults.filter( jobResult => !jobResult.success ).map( jobResult => jobResult.alert );
  }
}
