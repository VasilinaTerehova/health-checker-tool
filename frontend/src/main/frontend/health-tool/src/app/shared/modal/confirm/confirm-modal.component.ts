import { Component } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { Subject } from 'rxjs/Subject';

@Component({
  selector: 'confirm-modal',
  templateUrl: './confirm-modal.component.html',
})
export class ConfirmModalComponent {
  isConfirmed: Subject<Boolean>;
  clusterName: String;

  constructor( public bsModalRef: BsModalRef ) {  }

  confirm() {
    this.isConfirmed.next( true );
    this.bsModalRef.hide();
  }

  decline() {
    this.isConfirmed.next( false );
    this.bsModalRef.hide();
  }
}
