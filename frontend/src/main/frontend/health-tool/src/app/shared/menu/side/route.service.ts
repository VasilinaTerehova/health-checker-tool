import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class RouteService {
  private _healthCheckMessage = new Subject<string>();
  private _clusterName: string;
  healthCheckMessage$ = this._healthCheckMessage.asObservable();

  constructor( private router: Router ) {  }

  routeToHealthCheck( clusterName: string, makeCheck: boolean = false ) {
    if ( makeCheck ) {
      this._clusterName = clusterName;
    }
    else {
      this._clusterName = null;
    }

    if ( this.router.url.indexOf( "cluster/" + clusterName ) == -1 ) {
      this.router.navigate( ['/cluster/' + clusterName] );
    }
    else {
      this._healthCheckMessage.next( clusterName );
    }
  }

  routeToHealthCheckIfDestIsDiffer( clusterName: string, makeCheck: boolean = false ) {
    if ( this.router.url.indexOf( "cluster/" + clusterName ) == -1 ) {
      this.routeToHealthCheck( clusterName, makeCheck );
    }
  }

  get clusterName(): string {
    return this._clusterName;
  }
}
