package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ServiceStatus;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;

import java.util.List;

public interface IClusterSnapshotFacade {
    ClusterHealthSummary getLastClusterSnapshot(String clusterName );
    List<ClusterHealthSummary> getClusterSnapshotHistory(String clusterName ) throws InvalidResponseException;
    List<ServiceStatus> askForCurrentClusterSnapshot(String clusterName ) throws InvalidResponseException;
    void checkClustersHealth();
    ClusterShapshotEntity receiveAndSaveClusterSnapshot(ClusterEntity clusterEntity);
}
