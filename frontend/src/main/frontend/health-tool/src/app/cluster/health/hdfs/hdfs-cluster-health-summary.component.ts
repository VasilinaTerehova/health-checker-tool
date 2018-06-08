import { Component, Input, Output, EventEmitter } from '@angular/core';

//Models
import { Cluster } from '../../../shared/cluster/cluster.model';
import { JobExample } from '../job-example.model';
import { ServiceStatus } from '../../../service/service-status.model';

@Component({
  selector: 'hdfs-cluster-health-summary',
  templateUrl: 'hdfs-cluster-health-summary.component.html',
})
export class HdfsClusterHealthSummaryComponent {
  @Input() serviceName: string;
  //Trigger refresh action to parent
  @Output() onHealthCheckRefresh = new EventEmitter<boolean>();
  private _hdfsHealthReportList: ServiceStatus[];
  private _hdfsHealthReport: ServiceStatus;
  isCollapsed: boolean;
  isLoading: Boolean;

  @Input()
  set hdfsHealthReportList(hdfsHealthReportList: ServiceStatus[]) {
    if (hdfsHealthReportList) {
      this._hdfsHealthReportList = hdfsHealthReportList;
      this.isLoading = false;
      this.findAppropriateServiceByName();
    }
  }

  get hdfsHealthReportList(): ServiceStatus[] {
    return this._hdfsHealthReportList;
  }

  private findAppropriateServiceByName() {
    for (var i = 0; i < this._hdfsHealthReportList.length; i++) {
      if (this._hdfsHealthReportList[i].displayName === this.serviceName) {
        this._hdfsHealthReport = this._hdfsHealthReportList[i];
        break;
      }
    }
  }

  @Input()
  set hdfsHealthReport(hdfsHealthReport: ServiceStatus) {
    if (hdfsHealthReport) {
      this._hdfsHealthReport = hdfsHealthReport;
      this.isLoading = false;
    }
    else {
      this._hdfsHealthReport = new ServiceStatus();
      this.isLoading = true;
    }
  }

  get hdfsHealthReport(): ServiceStatus {
    return this._hdfsHealthReport;
  }

  isJobRunSuccessfully(): boolean {
    return this.hdfsHealthReport.jobResults.map(jobResult => jobResult.success).filter(jobResult => !jobResult).pop() == null;
  }

  getSuccessfullyRunJobsCount(): number {
    return this.hdfsHealthReport.jobResults.filter(jobResult => jobResult.success).length;
  }

  getAlertsCount(): number {
    return this.getAlerts().length;
  }

  getAlerts(): string[] {
    return this.hdfsHealthReport.jobResults.filter(jobResult => !jobResult.success)
      .filter(jobResult => jobResult.alerts).map(jobResult => jobResult.alerts.join("\n"));
  }

  checkClusterHealth() {
    this.onHealthCheckRefresh.next(true);
  }
}
