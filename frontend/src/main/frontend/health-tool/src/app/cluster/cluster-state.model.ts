import { Cluster } from '../shared/cluster/cluster.model';
import { ServiceStatus } from '../service/service-status.model';

export class ClusterState {
  cluster: Cluster;
  serviceStatusList: ServiceStatus[];
}
