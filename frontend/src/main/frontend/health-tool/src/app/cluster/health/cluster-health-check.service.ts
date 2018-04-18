import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Cluster } from '../../shared/cluster/cluster.model';
import { ClusterState } from '../cluster-state.model';
import {ClusterStateHistory} from "../cluster-history-state.model";
import { HealthCheckResult } from "../health/health-check-result.model";
import { HdfsHealthReport } from './hdfs/hdfs-health-report.model';
import { ClusterSnapshot } from '../cluster-snapshot.model';

@Injectable()
export class ClusterHealthCheckService {
  constructor(private http: HttpClient) {  }

  getServicesClusterState( clusterName: string ) {
    return this.http.get<HealthCheckResult>("http://localhost:8888/api/cluster/" + clusterName + "/status/services");
  }

  getYarnClusterState( clusterName: string ) {
    return this.http.get<HdfsHealthReport>("http://localhost:8888/api/cluster/" + clusterName + "/status/yarn");
  }

  getHdfsClusterState( clusterName: string ) {
    return this.http.get<HdfsHealthReport>("http://localhost:8888/api/cluster/" + clusterName + "/status/hdfs");
  }

  getFsClusterState( clusterName: string ) {
    return this.http.get<ClusterSnapshot>("http://localhost:8888/api/cluster/" + clusterName + "/status/fs");
  }

  getAllClusterState( clusterName: string ) {
    return this.http.get<HealthCheckResult>("http://localhost:8888/api/cluster/" + clusterName + "/status/all");
  }

  getClusterStateHistory( clusterName: string ) {
    return this.http.get<Array<ClusterState>>("http://localhost:8888/getClusterStatusHistory", { params: { "clusterName": clusterName } });
  }
}
