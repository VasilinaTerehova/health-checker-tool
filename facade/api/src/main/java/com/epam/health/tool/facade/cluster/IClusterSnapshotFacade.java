package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;

import java.util.List;

public interface IClusterSnapshotFacade {
    ClusterHealthSummary getLastClusterSnapshot(String clusterName );
    List<ClusterHealthSummary> getClusterSnapshotHistory(String clusterName ) throws InvalidResponseException;
    ClusterHealthSummary askForCurrentClusterSnapshot(String clusterName ) throws InvalidResponseException;
    default HealthCheckResultsAccumulator askForCurrentClusterSnapshotTemp(String clusterName ) throws InvalidResponseException {
        return null;
    }
    List<ServiceStatus> askForCurrentServicesSnapshot(String clusterName ) throws InvalidResponseException;
    ClusterShapshotEntity receiveAndSaveClusterSnapshot(ClusterEntity clusterEntity);
}
