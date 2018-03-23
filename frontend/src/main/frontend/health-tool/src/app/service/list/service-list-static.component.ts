import {Component, OnInit, OnDestroy, Output, EventEmitter, Input} from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { ClusterService } from '../../cluster/cluster.service';
import { ClusterState } from '../../cluster/cluster-state.model';
import { Cluster } from '../../shared/cluster/cluster.model';
import { ErrorReportingService } from '../../shared/error/error-reporting.service';
import { RouteService } from '../../shared/menu/side/route.service';

@Component({
  selector: 'service-list-static',
  templateUrl: 'service-list-static.component.html',
})
export class ServiceListStaticComponent implements OnInit, OnDestroy {
  @Input() clusterState: ClusterState;

  isShowServiceActionAllow(name: string) {
    return false;
  }

  ngOnInit() {
  }

  ngOnDestroy() {
  }

}
