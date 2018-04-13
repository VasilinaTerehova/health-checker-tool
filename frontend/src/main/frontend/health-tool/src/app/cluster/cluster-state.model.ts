import { ClusterSnapshot } from './cluster-snapshot.model';
import { ServiceStatus } from '../service/service-status.model';

export class ClusterState {
  cluster: ClusterSnapshot;
  serviceStatusList: ServiceStatus[];
}
