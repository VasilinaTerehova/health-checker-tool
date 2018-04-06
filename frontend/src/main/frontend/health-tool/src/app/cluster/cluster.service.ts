import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Cluster } from '../shared/cluster/cluster.model';
import { ClusterState } from './cluster-state.model';
import {ClusterStateHistory} from "./cluster-history-state.model";
import { HealthCheckResult } from "./health/health-check-result.model";

@Injectable()
export class ClusterService {
  constructor(private http: HttpClient) {  }

  getClusters() {
    return this.http.get<Array<Cluster>>("http://localhost:8888/clusters");
  }

  getClusterState( clusterName: string ) {
    return this.http.get<HealthCheckResult>("http://localhost:8888/getClusterStatus", { params: { "clusterName": clusterName } });
  }

  getClusterStateHistory( clusterName: string ) {
    return this.http.get<Array<ClusterState>>("http://localhost:8888/getClusterStatusHistory", { params: { "clusterName": clusterName } });
  }

  updateCluster( cluster: Cluster ) {
    return this.http.post<Cluster>("http://localhost:8888/cluster", { "cluster": cluster } )
  }

  deleteCluster( clusterName: string ) {
    return this.http.delete<Cluster>("http://localhost:8888/cluster", { params: { "clusterName": clusterName } })
  }

  saveCluster( cluster: Cluster ) {
    return this.http.put<Cluster>("http://localhost:8888/cluster", { "cluster": cluster } )
  }
}
