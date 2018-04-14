package com.epam.health.tool.facade.service.action;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;

public interface IServiceHealthCheckAction {
    void performHealthCheck(String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException;
}
