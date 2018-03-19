import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Cluster } from '../shared/cluster/cluster.model';
import { ClusterState } from './cluster-state.model';

@Injectable()
export class ClusterService {
  constructor(private http: HttpClient) {  }

  getClusters() {
    return this.http.get<Array<Cluster>>("/clusters");
  }

  getClusterState( clusterName: string ) {
    return this.http.get<ClusterState>("/getClusterStatus", { params: { "clusterName": clusterName } });
  }
}
