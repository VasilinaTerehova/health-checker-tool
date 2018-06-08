import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Cluster } from '../shared/cluster/cluster.model';
import { ClusterState } from './cluster-state.model';
import {ClusterStateHistory} from './cluster-history-state.model';
import { HealthCheckResult } from './health/health-check-result.model';
import { environment } from '../../environments/environment';

@Injectable()
export class ClusterService {
  constructor(private http: HttpClient) {  }

  getClusters() {
    return this.http.get<Array<Cluster>>("http://"+environment.htServerHost+":"+environment.htServerPort+"/clusters");
  }

  getCluster( clusterName: string ) {
    return this.http.get<Cluster>("http://"+environment.htServerHost+":"+environment.htServerPort+"/cluster/" + clusterName);
  }

  updateCluster( cluster: Cluster ) {
    return this.http.post<Cluster>("http://"+environment.htServerHost+":"+environment.htServerPort+"/cluster", { "cluster": cluster } )
  }

  deleteCluster( clusterName: string ) {
    return this.http.delete<Cluster>("http://"+environment.htServerHost+":"+environment.htServerPort+"/cluster", { params: { "clusterName": clusterName } })
  }

  saveCluster( cluster: Cluster ) {
    return this.http.put<Cluster>("http://"+environment.htServerHost+":"+environment.htServerPort+"/cluster", { "cluster": cluster } )
  }
}
