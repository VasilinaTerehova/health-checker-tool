import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class RouteService {
  private healthCheckMessage = new Subject<string>();
  healthCheckMessage$ = this.healthCheckMessage.asObservable();

  constructor( private router: Router ) {  }

  routeToHealthCheck( clusterName: string ) {
    if ( this.router.url.indexOf( "cluster/" + clusterName ) == -1 ) {
      this.router.navigate( ['/cluster/' + clusterName] );
    }
    else {
      this.healthCheckMessage.next( clusterName );
    }
  }
}
