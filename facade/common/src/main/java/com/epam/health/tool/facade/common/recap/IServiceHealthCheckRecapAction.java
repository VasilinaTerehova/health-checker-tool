package com.epam.health.tool.facade.common.recap;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.validation.ClusterHealthValidationResult;

public interface IServiceHealthCheckRecapAction {
    void doRecapHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                            ClusterHealthValidationResult clusterHealthValidationResult);
}
