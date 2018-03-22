import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { Subject } from 'rxjs/Subject';

import { Cluster } from '../cluster.model';
import { ClusterType } from '../cluster-type.model';
import { Credentials } from '../credentials.model';
import { ClusterTypeExService } from '../cluster-type-ex.service';
import { ClusterComparatorService } from '../cluster-comparator.service';

@Component({
  selector: 'cluster-edit',
  templateUrl: './cluster-edit.component.html',
})
export class ClusterEditComponent implements OnInit {
  clusterTypes = ClusterType;
  tempCluster: Cluster;
  savedCluster: Cluster;
  changedCluster: Subject<Cluster>;

  constructor( public bsModalRef: BsModalRef, private clusterTypeExService: ClusterTypeExService,
    private clusterComparatorService: ClusterComparatorService ) {  }

  ngOnInit() {
    if ( !this.savedCluster ) {
      this.savedCluster = new Cluster();
    }

    if ( this.savedCluster.kerberos == null ) {
      this.savedCluster.kerberos = new Credentials();
    }

    this.tempCluster = new Cluster();
    this.saveClusterSnapshot( this.savedCluster, this.tempCluster );
  }

  getClusterTypes(): string[] {
    return this.clusterTypeExService.etClusterTypes();
  }

  closeModal() {
    this.bsModalRef.hide();
  }

  saveChanges() {
    if ( this.isChangesDetected() && this.changedCluster ) {
      this.changedCluster.next( this.tempCluster );
    }

    this.bsModalRef.hide();
  }

  private isChangesDetected(): boolean {
    return !this.clusterComparatorService.compare( this.tempCluster, this.savedCluster );
  }

  private saveClusterSnapshot( currentState: Cluster, snapshotHolder: Cluster ) {
    //Copy core info
    snapshotHolder.host = currentState.host;
    snapshotHolder.id = currentState.id;
    snapshotHolder.clusterType = currentState.clusterType;
    snapshotHolder.name = currentState.name;
    snapshotHolder.secured = currentState.secured;
    //Copy credentials info
    //Http
    snapshotHolder.http.username = currentState.http.username;
    snapshotHolder.http.password = currentState.http.password;
    //Ssh
    snapshotHolder.ssh.username = currentState.ssh.username;
    snapshotHolder.ssh.password = currentState.ssh.password;
    //Kerberos
    snapshotHolder.kerberos.username = currentState.kerberos.username;
    snapshotHolder.kerberos.password = currentState.kerberos.password;
  }
}
