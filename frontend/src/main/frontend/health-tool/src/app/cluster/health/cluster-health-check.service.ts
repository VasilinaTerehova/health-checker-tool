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

  getServicesClusterState( clusterName: string, token: string = "empty", useSave: boolean = false ) {
    return this.http.get<HealthCheckResult>("http://localhost:8888/api/cluster/" + clusterName + "/status/services", { params: { "token": token, "useSave": useSave.toString() } });
  }

  getYarnClusterState( clusterName: string, token: string = "empty", useSave: boolean = false ) {
    return this.http.get<HdfsHealthReport>("http://localhost:8888/api/cluster/" + clusterName + "/status/yarn", { params: { "token": token, "useSave": useSave.toString() } });
  }

  getHdfsClusterState( clusterName: string, token: string = "empty", useSave: boolean = false ) {
    return this.http.get<HdfsHealthReport>("http://localhost:8888/api/cluster/" + clusterName + "/status/hdfs", { params: { "token": token, "useSave": useSave.toString() } });
  }

  getFsClusterState( clusterName: string, token: string = "empty", useSave: boolean = false ) {
    return this.http.get<ClusterSnapshot>("http://localhost:8888/api/cluster/" + clusterName + "/status/fs", { params: { "token": token, "useSave": useSave.toString() } });
  }

  getAllClusterState( clusterName: string, token: string = "empty", useSave: boolean = false ) {
    return this.http.get<HealthCheckResult>("http://localhost:8888/api/cluster/" + clusterName + "/status/all", { params: { "token": token, "useSave": useSave.toString() } });
  }

  getClusterStateHistory( clusterName: string ) {
    return this.http.get<Array<ClusterState>>("http://localhost:8888/getClusterStatusHistory", { params: { "clusterName": clusterName } });
  }
}
