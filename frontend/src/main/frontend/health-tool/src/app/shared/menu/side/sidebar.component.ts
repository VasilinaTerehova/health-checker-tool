import {Component} from '@angular/core';
import { OnInit } from '@angular/core';

import { Cluster } from '../../cluster/cluster.model';
import { ClusterService } from '../../../cluster/cluster.service';
import { RouteService } from './route.service';
import { ErrorReportingService } from '../../error/error-reporting.service';

@Component({
  selector: 'sidebar',
  templateUrl: './sidebar.component.html'
})
export class SideBarComponent implements OnInit{
  clusters: Cluster[];
  tempCluster: Cluster;
  clusterSearch: string;

  constructor( private clusterService: ClusterService, private routeService: RouteService, private errorReportingService: ErrorReportingService ) {}

  ngOnInit() {
    this.errorReportingService.clearError();
    this.clusterService.getClusters().subscribe(
      data => this.clusters = data,
      error => this.errorReportingService.reportHttpError( error )
    );
    this.tempCluster = new Cluster();
  }

  checkClusterHealth( clusterName: string ) {
    this.routeService.routeToHealthCheck( clusterName );
  }
}
