import {Component, Input} from '@angular/core';

import { ClusterState } from '../../../cluster/cluster-state.model';

@Component({
  selector: 'service-list-static',
  templateUrl: 'service-list-static.component.html',
})
export class ServiceListStaticComponent {
  @Input() clusterState: ClusterState;

  isShowLogsLocationAllow(state: string) {
    return state && (state.toUpperCase() == "BAD" || state.toUpperCase() == "DISABLED");
  }
}
