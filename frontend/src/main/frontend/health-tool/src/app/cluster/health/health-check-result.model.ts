import { ClusterState } from '../cluster-state.model';
import { ServiceStatus } from '../../service/service-status.model';

export class HealthCheckResult {
  clusterHealthSummary: ClusterState;
  serviceStatusList: ServiceStatus[];
}
