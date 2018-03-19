import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { YarnApplication } from './application.model';
import { YarnApplicationService } from './yarn-application.service';
import { ErrorReportingService } from '../../shared/error/error-reporting.service';
import { RouteService } from '../../shared/menu/side/route.service';

@Component({
  selector: 'yarn-application-list',
  templateUrl: 'yarn-application-list.component.html',
})
export class YarnApplicationListComponent implements OnInit, OnDestroy {
  private sub: Subscription;
  @Output() onYarnAppsChange = new EventEmitter<number>();
  yarnApps: YarnApplication[];

  constructor( private yarnApplicationService: YarnApplicationService, private route: ActivatedRoute,
     private errorReportingService: ErrorReportingService, private routeService: RouteService ) {
    this.sub = this.routeService.healthCheckMessage$.subscribe(
      clusterName => this.askForYarnAppsList( clusterName )
    );
  }

  ngOnInit() {
    this.yarnApps = new Array<YarnApplication>();
    this.route.paramMap.subscribe( (params: ParamMap) => {
      this.askForYarnAppsList( params.get( 'id' ) )
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  private askForYarnAppsList( clusterName: string ) {
    this.yarnApplicationService.getYarnApps( clusterName ).subscribe(
      data => {
         this.yarnApps = data;
         this.onYarnAppsChange.emit( this.yarnApps.length );
      },
      error => this.errorReportingService.reportHttpError( error )
    )
  }
}
