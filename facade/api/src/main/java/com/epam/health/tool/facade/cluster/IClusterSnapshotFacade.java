package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.HdfsHealthCheckResult;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;

import java.util.List;

public interface IClusterSnapshotFacade {
    ClusterHealthSummary getLastClusterSnapshot(String clusterName );
    List<ClusterHealthSummary> getClusterSnapshotHistory(String clusterName ) throws InvalidResponseException;
    HealthCheckResultsAccumulator askForCurrentClusterSnapshot(String clusterName ) throws InvalidResponseException;
    default YarnHealthCheckResult askForCurrentYarnHealthCheck(String clusterName) throws InvalidResponseException {
        return null;
    }
    default HealthCheckResultsAccumulator askForCurrentFullHealthCheck(String clusterName ) throws InvalidResponseException {
        return null;
    }
    List<ServiceStatus> askForCurrentServicesSnapshot(String clusterName ) throws InvalidResponseException;
    HdfsHealthCheckResult askForCurrentHdfsHealthCheck(String clusterName) throws InvalidResponseException;
    ClusterShapshotEntity receiveAndSaveClusterSnapshot(ClusterEntity clusterEntity);
}
