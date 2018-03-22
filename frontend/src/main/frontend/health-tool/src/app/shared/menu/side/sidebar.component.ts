import {Component} from '@angular/core';
import { OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';

import { Cluster } from '../../cluster/cluster.model';
import { ClusterService } from '../../../cluster/cluster.service';
import { RouteService } from './route.service';
import { ErrorReportingService } from '../../error/error-reporting.service';
import { ClusterEditComponent } from '../../cluster/edit/cluster-edit.component';

@Component({
  selector: 'sidebar',
  templateUrl: './sidebar.component.html'
})
export class SideBarComponent implements OnInit{
  clusters: Cluster[];
  tempCluster: Cluster;
  clusterSearch: string;
  bsModalRef: BsModalRef;

  constructor( private clusterService: ClusterService, private routeService: RouteService, private errorReportingService: ErrorReportingService, private modalService: BsModalService ) {}

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

  updateCluster( clusterName: string ) {
    this.showEditCluster( this.clusters.find( cluster => cluster.name == clusterName ), this.updateClusterAction );
  }

  addCluster() {
    this.showEditCluster( new Cluster(), this.saveClusterAction );
  }

  deleteCluster( clusterName: string ) {
    this.clusterService.deleteCluster( clusterName ).subscribe(
      data => console.log( data ),
      error => this.errorReportingService.reportHttpError( error )
    )
  }

  private saveClusterAction( cluster: Cluster ) {
    this.clusterService.saveCluster( cluster ).subscribe(
      result => this.addClusterToList( result ),
      error => this.errorReportingService.reportHttpError( error )
    );
  }

  private updateClusterAction( cluster: Cluster ) {
    this.clusterService.updateCluster( cluster ).subscribe(
      result => this.addClusterToList( result ),
      error => this.errorReportingService.reportHttpError( error )
    );
  }

  private addClusterToList( cluster: Cluster ) {
    this.clusters = this.clusters.filter( item => item.name != cluster.name );
    this.clusters.push( cluster );
  }

  private showEditCluster( cluster: Cluster, next?: (this, value: Cluster) => void ) {
    var initialState = {
      savedCluster: cluster,
      changedCluster: new Subject<Cluster>()
    };
    var changedSubscription: Subscription = initialState.changedCluster.asObservable().subscribe(
      result => {
        next.call(this, result);
        changedSubscription.unsubscribe();
      }
    );
    this.bsModalRef = this.modalService.show( ClusterEditComponent, {initialState});
  }
}
