package com.epam.health.tool.facade.service.action;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.exception.InvalidResponseException;

public interface IServiceHealthCheckAction {
    void performHealthCheck(String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException;
}
