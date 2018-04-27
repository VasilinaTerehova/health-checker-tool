import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { Subject } from 'rxjs/Subject';

//Models
import { Credentials } from '../../../../../shared/cluster/credentials.model';

@Component({
  selector: 'confirm-automatic-fix',
  templateUrl: 'confirm-automatic-fix.component.html',
})
export class ConfirmAutomaticFixComponent implements OnInit {
  ssh: Credentials = new Credentials();
  operationName: string;
  sshCredentialsChannel: Subject<Credentials>;

  constructor( public bsModalRef: BsModalRef ) {  }

  ngOnInit() {

  }

  decline() {
    this.bsModalRef.hide();
  }

  confirm() {
    if ( this.ssh.password && this.ssh.username ) {
      this.sshCredentialsChannel.next( this.ssh );
      this.bsModalRef.hide();
    }
  }
}
