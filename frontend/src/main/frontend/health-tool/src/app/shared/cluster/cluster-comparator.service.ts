import { Injectable } from '@angular/core';

import { Cluster } from './cluster.model';
import { Credentials } from './credentials.model';

@Injectable()
export class ClusterComparatorService {
  constructor() {  }

  compare( first: Cluster, second: Cluster ): boolean {
    if ( !first || !second ) {
      return false;
    }

    if ( !first && !second ) {
      return true;
    }

    return first.id == second.id && first.name == second.name && first.clusterType == second.clusterType && first.host == second.host && first.secured == second.secured
     && this.credentialsCompare( first.http, second.http ) && this.credentialsCompare( first.ssh, second.ssh ) && this.credentialsCompare( first.kerberos, second.kerberos );
  }

  private credentialsCompare( first: Credentials, second: Credentials ): boolean {
    if ( !first || !second ) {
      return false;
    }

    if ( !first && !second ) {
      return true;
    }

    return first.username == second.username && first.password == second.password;
  }
}
