package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.facade.exception.InvalidResponseException;

import java.util.List;

public interface IHealthCheckFacade {
    HealthCheckResultsAccumulator askForClusterSnapshot(ClusterAccumulatorToken clusterAccumulatorToken ) throws InvalidResponseException;
}
