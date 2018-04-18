import {Component, OnInit} from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';

//Models
import { Cluster } from '../../cluster/cluster.model';
import { ErrorAlert } from '../../error/error-alert.model';
import { AlertType } from '../..//error/alert-type.model';
//Services
import { ClusterService } from '../../../cluster/cluster.service';
import { RouteService } from './route.service';
import { ErrorReportingService } from '../../error/error-reporting.service';
//Components
import { ClusterEditComponent } from '../../cluster/edit/cluster-edit.component';
import { ConfirmModalComponent } from '../../modal/confirm/confirm-modal.component';

@Component({
  selector: 'sidebar',
  templateUrl: './sidebar.component.html'
})
export class SideBarComponent implements OnInit{
  clusters: Cluster[];
  tempCluster: Cluster;
  clusterSearch: string;
  bsModalRef: BsModalRef;

  constructor( private clusterService: ClusterService, private routeService: RouteService,
    private errorReportingService: ErrorReportingService, private modalService: BsModalService ) {}

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
    this.showConfirmDeleteCluster( clusterName );
  }

  private deleteClusterAction( clusterName: string ) {
    this.clusterService.deleteCluster( clusterName ).subscribe(
      data => {
        this.clusters = this.clusters.filter( item => item.name != clusterName );
        this.errorReportingService.reportError( new ErrorAlert( "Cluster " + clusterName + " successfully deleted!", AlertType.SUCCESS ) );
      },
      error => this.errorReportingService.reportHttpError( error )
    )
  }

  private saveClusterAction( cluster: Cluster ) {
    this.clusterService.saveCluster( cluster ).subscribe(
      result => {
        this.addClusterToList( result );
        this.errorReportingService.reportError( new ErrorAlert( "Cluster " + result.name + " successfully added!", AlertType.SUCCESS ) );
      },
      error => this.errorReportingService.reportHttpError( error )
    );
  }

  private updateClusterAction( cluster: Cluster ) {
    this.clusterService.updateCluster( cluster ).subscribe(
      result => {
        this.addClusterToList( result );
        this.errorReportingService.reportError( new ErrorAlert( "Cluster " + result.name + " successfully updated!", AlertType.SUCCESS ) );
      },
      error => this.errorReportingService.reportHttpError( error )
    );
  }

  private addClusterToList( cluster: Cluster ) {
    this.clusters = this.clusters.filter( item => item.id != cluster.id );
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

  private showConfirmDeleteCluster( clusterName: string ) {
    var initialState = {
      clusterName: clusterName,
      isConfirmed: new Subject<Boolean>()
    };

    var changedSubscription: Subscription = initialState.isConfirmed.asObservable().subscribe(
      result => {
        if ( result ) {
          this.deleteClusterAction( clusterName );
        }

        changedSubscription.unsubscribe();
      }
    );
    this.bsModalRef = this.modalService.show( ConfirmModalComponent, {initialState});
  }
}
