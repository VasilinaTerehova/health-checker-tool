import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

//Models
import { Cluster } from '../shared/cluster/cluster.model';
import { JobExample } from './health/job-example.model';
import { HdfsHealthReport } from './health/hdfs/hdfs-health-report.model';
import { ClusterSnapshot } from './cluster-snapshot.model';
//Services
import { ClusterService } from './cluster.service';
import { ClusterHealthCheckService } from './health/cluster-health-check.service';
import { RouteService } from '../shared/menu/side/route.service';

@Component({
  selector: 'cluster-info',
  templateUrl: './cluster.component.html',
})
export class ClusterComponent implements OnInit, OnDestroy {
  yarnAppsCount: number;
  clusterSnapshot: ClusterSnapshot;
  private _clusterName: string;
  //Reports
  private _hdfsHealthReport: HdfsHealthReport;
  private _yarnHealthReport: HdfsHealthReport;
  //Sub notification chanel
  private _sub: Subscription;

  constructor( private router: Router, private route: ActivatedRoute,
    private clusterHealthCheckService: ClusterHealthCheckService, private routeService: RouteService ) {
      this._sub = routeService.healthCheckMessage$.subscribe(
        clusterName => this.clusterName = clusterName
      );
  }

  ngOnInit() {
    this.route.paramMap.subscribe( (params: ParamMap) => {
      this.clusterName = params.get( 'id' );
    });
  }

  ngOnDestroy() {
    this._sub.unsubscribe();
  }

  onYarnAppsCountChange( newYarnAppsCount: number ) {
    this.yarnAppsCount = newYarnAppsCount;
  }

  onClusterSnapshotChange( clusterSnapshot: ClusterSnapshot ) {
    this.clusterSnapshot = clusterSnapshot;
  }

  set clusterName( clusterName: string ) {
    if ( clusterName ) {
      this._clusterName = clusterName;
      this.ascForHdfsAndYarnReports();
    }
  }

  get clusterName(): string {
    return this._clusterName;
  }

  get hdfsHealthReport(): HdfsHealthReport {
    return this._hdfsHealthReport;
  }

  get yarnHealthReport(): HdfsHealthReport {
    return this._yarnHealthReport;
  }

  private ascForHdfsAndYarnReports() {
    this.clusterHealthCheckService.getHdfsClusterState( this._clusterName ).subscribe(
      data => this._hdfsHealthReport = data
    );
    //Disabled for development
    // this.clusterHealthCheckService.getYarnClusterState( this._clusterName ).subscribe(
    //   data => this._yarnHealthReport = data
    // )
  }
}
