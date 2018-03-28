import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { ClusterService } from '../../cluster/cluster.service';
import { ClusterState } from '../../cluster/cluster-state.model';
import { Cluster } from '../../shared/cluster/cluster.model';
import { ErrorReportingService } from '../../shared/error/error-reporting.service';
import { RouteService } from '../../shared/menu/side/route.service';

@Component({
  selector: 'service-status-history',
  templateUrl: 'service-status-history.component.html',
})
export class ServiceListHistoryComponent implements OnInit, OnDestroy {
  private sub: Subscription;
  @Output() onClusterChange  = new EventEmitter<Cluster>();
  clusterStateHistory: ClusterState[];

  constructor( private clusterService: ClusterService, private route: ActivatedRoute,
    private errorReportingService: ErrorReportingService, private routeService: RouteService ) {
    this.sub = routeService.healthCheckMessage$.subscribe(
      clusterName => this.ascForClusterState( clusterName )
    );
  }

  ngOnInit() {
    this.clusterStateHistory = [];
    this.route.paramMap.subscribe( (params: ParamMap) => {
      this.ascForClusterState( params.get( 'id' ) );
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  private ascForClusterState( clusterName: string ) {
    this.errorReportingService.clearError();
    this.clusterService.getClusterStateHistory( clusterName ).subscribe(
      data => {
        this.clusterStateHistory = data;
        //this.onClusterChange.emit( this.clusterStateHistory.cluster );
      },
      error => this.errorReportingService.reportHttpError( error )
    );
  }
}
