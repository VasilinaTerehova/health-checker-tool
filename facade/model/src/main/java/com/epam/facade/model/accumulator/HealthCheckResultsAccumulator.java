package com.epam.facade.model.accumulator;

import com.epam.facade.model.accumulator.results.impl.FsHealthCheckResult;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.projection.*;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.epam.util.common.CheckingParamsUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HealthCheckResultsAccumulator {
    //Separate services
    //Other services
    private List<ServiceStatusProjection> serviceStatusList = new ArrayList<>();
    //Fs check result
    private FsHealthCheckResult fsHealthCheckResult;
    //Snapshot info
    private ClusterSnapshotAccumulator clusterSnapshotAccumulator;

    public List<ServiceStatusProjection> getServiceStatusList() {
        return serviceStatusList;
    }

    public FsHealthCheckResult getFsHealthCheckResult() {
        return fsHealthCheckResult;
    }

    public ClusterSnapshotAccumulator getClusterSnapshotAccumulator() {
        return clusterSnapshotAccumulator;
    }

    public ServiceStatusProjection getServiceHealthCheckResult(ServiceTypeEnum serviceTypeEnum) throws InvalidResponseException {
        if ( !CheckingParamsUtil.isParamListNotNullOrEmpty( this.serviceStatusList ) ) {
            throw new InvalidResponseException( "Can't find service health check result for service type. Service status list is empty" );
        }

        return findServiceHealthCheckResult( serviceTypeEnum );
    }

    public ServiceStatusProjection getServiceHealthCheckResultIfExists(ServiceTypeEnum serviceTypeEnum) throws InvalidResponseException {
        if ( !CheckingParamsUtil.isParamListNotNullOrEmpty( this.serviceStatusList ) ) {
            return null;
        }

        return findServiceHealthCheckResult( serviceTypeEnum );
    }

    private ServiceStatusProjection findServiceHealthCheckResult(ServiceTypeEnum serviceTypeEnum) throws InvalidResponseException {
        return serviceStatusList.stream().filter(o -> o.getDisplayName().equals(serviceTypeEnum)).findFirst()
                .orElseThrow( () -> new InvalidResponseException( "Can't find service health check result for service type - ".concat( serviceTypeEnum.name() ) ));
    }

    public static class HealthCheckResultsModifier {
        private HealthCheckResultsAccumulator healthCheckResultsAccumulator;

        private HealthCheckResultsModifier(HealthCheckResultsAccumulator healthCheckResultsAccumulator) {
            this.healthCheckResultsAccumulator = healthCheckResultsAccumulator;
        }

        public static HealthCheckResultsModifier get(HealthCheckResultsAccumulator healthCheckResultsAccumulator) {
            return new HealthCheckResultsModifier(healthCheckResultsAccumulator);
        }

        public static HealthCheckResultsModifier get() {
            return get(new HealthCheckResultsAccumulator());
        }

        public HealthCheckResultsModifier setServiceStatusList(List<ServiceStatusProjection> serviceStatusList) {
            this.healthCheckResultsAccumulator.serviceStatusList = serviceStatusList;

            return this;
        }

        public HealthCheckResultsModifier setMemoryUsage(MemoryUsageEntityProjection memoryUsageEntityProjection) {
            verifyAndSetFsResult(() -> this.healthCheckResultsAccumulator.fsHealthCheckResult.setMemoryUsageEntityProjection(memoryUsageEntityProjection));

            return this;
        }

        public HealthCheckResultsModifier setHdfsUsage(HdfsUsageEntityProjection hdfsUsageEntityProjection) {
            verifyAndSetFsResult(() -> this.healthCheckResultsAccumulator.fsHealthCheckResult.setHdfsUsageEntityProjection(hdfsUsageEntityProjection));

            return this;
        }

        public HealthCheckResultsModifier setNodeSnapshot(List<? extends NodeSnapshotEntityProjection> nodeSnapshotEntityProjections) {
            verifyAndSetFsResult(() -> this.healthCheckResultsAccumulator.fsHealthCheckResult.setNodeSnapshotEntityProjections(nodeSnapshotEntityProjections));

            return this;
        }

        public HealthCheckResultsModifier setId(Long id) {
            verifyAndSetClusterInfo(() -> this.healthCheckResultsAccumulator.clusterSnapshotAccumulator.setId(id));

            return this;
        }

        public HealthCheckResultsModifier setToken(String token) {
            verifyAndSetClusterInfo(() -> this.healthCheckResultsAccumulator.clusterSnapshotAccumulator.setToken(token));

            return this;
        }

        public HealthCheckResultsModifier setDate(Date date) {
            verifyAndSetClusterInfo(() -> this.healthCheckResultsAccumulator.clusterSnapshotAccumulator.setDateOfSnapshot(date));

            return this;
        }

        public HealthCheckResultsModifier setClusterName(String clusterName) {
            this.verifyAndSetClusterInfo(() -> this.healthCheckResultsAccumulator.clusterSnapshotAccumulator.setClusterName(clusterName));

            return this;
        }

        public HealthCheckResultsModifier setFsResultFromClusterSnapshot(ClusterSnapshotEntityProjection clusterSnapshotEntityProjection) {
            if (clusterSnapshotEntityProjection != null) {
                this.setHdfsUsage(clusterSnapshotEntityProjection.getHdfsUsage()).setMemoryUsage(clusterSnapshotEntityProjection.getMemoryUsage())
                        .setNodeSnapshot(clusterSnapshotEntityProjection.getNodes());
            }

            return this;
        }

        public HealthCheckResultsModifier setClusterInfoFromClusterSnapshot(ClusterSnapshotEntityProjection clusterSnapshotEntityProjection) {
            if (clusterSnapshotEntityProjection != null) {
                this.setClusterName(clusterSnapshotEntityProjection.getName()).setDate(clusterSnapshotEntityProjection.getDateOfSnapshot())
                        .setId(clusterSnapshotEntityProjection.getId());
            }

            return this;
        }

        public HealthCheckResultsAccumulator modify() {
            return healthCheckResultsAccumulator;
        }

        private void verifyAndSetFsResult(Runnable fsResultAction) {
            if (this.healthCheckResultsAccumulator.fsHealthCheckResult == null) {
                this.healthCheckResultsAccumulator.fsHealthCheckResult = new FsHealthCheckResult();
            }

            fsResultAction.run();
        }

        private void verifyAndSetClusterInfo(Runnable fsResultAction) {
            if (this.healthCheckResultsAccumulator.clusterSnapshotAccumulator == null) {
                this.healthCheckResultsAccumulator.clusterSnapshotAccumulator = new ClusterSnapshotAccumulator();
            }

            fsResultAction.run();
        }
    }
}
