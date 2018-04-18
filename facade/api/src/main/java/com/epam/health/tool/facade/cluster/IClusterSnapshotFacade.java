package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;

import java.util.List;

public interface IClusterSnapshotFacade {
    List<ClusterHealthSummary> getClusterSnapshotHistory(String clusterName ) throws InvalidResponseException;
    HealthCheckResultsAccumulator askForClusterSnapshot( ClusterAccumulatorToken clusterAccumulatorToken ) throws InvalidResponseException;
    ClusterShapshotEntity receiveAndSaveClusterSnapshot(ClusterEntity clusterEntity);
}
