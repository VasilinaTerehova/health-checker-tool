package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.exception.InvalidResponseException;

import java.util.List;

public interface IClusterSnapshotFacade {
    List<ClusterHealthSummary> getClusterSnapshotHistory(String clusterName, int count) throws InvalidResponseException;

    //make fresh snapshot
    HealthCheckResultsAccumulator makeClusterSnapshot(ClusterAccumulatorToken clusterAccumulatorToken) throws InvalidResponseException;

    //from db
    HealthCheckResultsAccumulator getLatestClusterSnapshot(ClusterAccumulatorToken clusterAccumulatorToken) throws InvalidResponseException;
}
