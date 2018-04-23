package com.epam.facade.model.accumulator;

import com.epam.facade.model.accumulator.results.impl.FsHealthCheckResult;
import com.epam.facade.model.accumulator.results.impl.HdfsHealthCheckResult;
import com.epam.facade.model.accumulator.results.impl.YarnHealthCheckResult;
import com.epam.facade.model.projection.*;

import java.util.Date;
import java.util.List;

public class HealthCheckResultsAccumulator {
    //Separate services
    private YarnHealthCheckResult yarnHealthCheckResult;
    private HdfsHealthCheckResult hdfsHealthCheckResult;
    //Other services
    private List<? extends ServiceStatusProjection> serviceStatusList;
    //Fs check result
    private FsHealthCheckResult fsHealthCheckResult;
    //Snapshot info
    private ClusterSnapshotAccumulator clusterSnapshotAccumulator;

    public YarnHealthCheckResult getYarnHealthCheckResult() {
        return yarnHealthCheckResult;
    }

    public List<? extends ServiceStatusProjection> getServiceStatusList() {
        return serviceStatusList;
    }

    public FsHealthCheckResult getFsHealthCheckResult() {
        return fsHealthCheckResult;
    }

    public HdfsHealthCheckResult getHdfsHealthCheckResult() {
        return hdfsHealthCheckResult;
    }

    public ClusterSnapshotAccumulator getClusterSnapshotAccumulator() {
        return clusterSnapshotAccumulator;
    }

    public static class HealthCheckResultsModifier {
        private HealthCheckResultsAccumulator healthCheckResultsAccumulator;

        private HealthCheckResultsModifier( HealthCheckResultsAccumulator healthCheckResultsAccumulator ) {
            this.healthCheckResultsAccumulator = healthCheckResultsAccumulator;
        }

        public static HealthCheckResultsModifier get( HealthCheckResultsAccumulator healthCheckResultsAccumulator ) {
            return new HealthCheckResultsModifier( healthCheckResultsAccumulator );
        }

        public static HealthCheckResultsModifier get() {
            return get( new HealthCheckResultsAccumulator() );
        }

        public HealthCheckResultsModifier setYarnHealthCheck( YarnHealthCheckResult yarnHealthCheck ) {
            this.healthCheckResultsAccumulator.yarnHealthCheckResult = yarnHealthCheck;

            return this;
        }

        public HealthCheckResultsModifier setServiceStatusList( List<? extends ServiceStatusProjection> serviceStatusList ) {
            this.healthCheckResultsAccumulator.serviceStatusList = serviceStatusList;

            return this;
        }

        public HealthCheckResultsModifier setMemoryUsage( MemoryUsageEntityProjection memoryUsageEntityProjection ) {
            verifyAndSetFsResult( () -> this.healthCheckResultsAccumulator.fsHealthCheckResult.setMemoryUsageEntityProjection( memoryUsageEntityProjection ) );

            return this;
        }

        public HealthCheckResultsModifier setHdfsUsage( HdfsUsageEntityProjection hdfsUsageEntityProjection ) {
            verifyAndSetFsResult( () -> this.healthCheckResultsAccumulator.fsHealthCheckResult.setHdfsUsageEntityProjection( hdfsUsageEntityProjection ) );

            return this;
        }

        public HealthCheckResultsModifier setNodeSnapshot( List<? extends NodeSnapshotEntityProjection> nodeSnapshotEntityProjections ) {
            verifyAndSetFsResult( () -> this.healthCheckResultsAccumulator.fsHealthCheckResult.setNodeSnapshotEntityProjections( nodeSnapshotEntityProjections ));

            return this;
        }

        public HealthCheckResultsModifier setHdfsHealthCheckResult( HdfsHealthCheckResult hdfsHealthCheckResult ) {
            this.healthCheckResultsAccumulator.hdfsHealthCheckResult = hdfsHealthCheckResult;

            return this;
        }

        public HealthCheckResultsModifier setId( Long id ) {
            this.healthCheckResultsAccumulator.clusterSnapshotAccumulator.setId( id );

            return this;
        }

        public HealthCheckResultsModifier setToken( String token ) {
            this.healthCheckResultsAccumulator.clusterSnapshotAccumulator.setToken( token );

            return this;
        }

        public HealthCheckResultsModifier setDate( Date date ) {
            this.healthCheckResultsAccumulator.clusterSnapshotAccumulator.setDateOfSnapshot( date );

            return this;
        }

        public HealthCheckResultsModifier setClusterName( String clusterName ) {
            this.healthCheckResultsAccumulator.clusterSnapshotAccumulator.setClusterName( clusterName );

            return this;
        }

        public HealthCheckResultsModifier setFsResultFromClusterSnapshot(ClusterSnapshotEntityProjection clusterSnapshotEntityProjection) {
            if ( clusterSnapshotEntityProjection != null ) {
                this.setHdfsUsage( clusterSnapshotEntityProjection.getHdfsUsage() ).setMemoryUsage( clusterSnapshotEntityProjection.getMemoryUsage() )
                        .setNodeSnapshot( clusterSnapshotEntityProjection.getNodes() );
            }

            return this;
        }

        public HealthCheckResultsModifier setClusterInfoFromClusterSnapshot(ClusterSnapshotEntityProjection clusterSnapshotEntityProjection) {
            if ( clusterSnapshotEntityProjection != null ) {
                this.setClusterName( clusterSnapshotEntityProjection.getName() ).setDate( clusterSnapshotEntityProjection.getDateOfSnapshot() )
                        .setId( clusterSnapshotEntityProjection.getId() );
            }

            return this;
        }

        public HealthCheckResultsAccumulator modify() {
            return healthCheckResultsAccumulator;
        }

        private void verifyAndSetFsResult( Runnable fsResultAction ) {
            if ( this.healthCheckResultsAccumulator.fsHealthCheckResult == null ) {
                this.healthCheckResultsAccumulator.fsHealthCheckResult = new FsHealthCheckResult();
            }

            fsResultAction.run();
        }
    }
}
