import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { ClusterService } from '../../cluster/cluster.service';
import { ClusterState } from '../../cluster/cluster-state.model';
import { Cluster } from '../../shared/cluster/cluster.model';
import { ErrorReportingService } from '../../shared/error/error-reporting.service';
import { RouteService } from '../../shared/menu/side/route.service';

@Component({
  selector: 'service-list',
  templateUrl: 'service-list.component.html',
})
export class ServiceListComponent implements OnInit, OnDestroy {
  private sub: Subscription;
  @Output() onClusterChange  = new EventEmitter<Cluster>();
  clusterState: ClusterState;

  constructor( private clusterService: ClusterService, private route: ActivatedRoute,
    private errorReportingService: ErrorReportingService, private routeService: RouteService ) {
    this.sub = routeService.healthCheckMessage$.subscribe(
      clusterName => this.ascForClusterState( clusterName )
    );
  }

  isShowServiceActionAllow(name: string) {
    return name && (name.toUpperCase() == "HBASE" || name.toUpperCase() == "HIVE");
  }

  restartService( serviceName: string ) {

  }

  ngOnInit() {
    this.clusterState = new ClusterState();
    this.route.paramMap.subscribe( (params: ParamMap) => {
      this.ascForClusterState( params.get( 'id' ) );
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  private ascForClusterState( clusterName: string ) {
    this.errorReportingService.clearError();
    this.clusterService.getClusterState( clusterName ).subscribe(
      data => {
        this.clusterState = data;
        this.onClusterChange.emit( this.clusterState.cluster );
      },
      error => this.errorReportingService.reportHttpError( error )
    );
  }
}
