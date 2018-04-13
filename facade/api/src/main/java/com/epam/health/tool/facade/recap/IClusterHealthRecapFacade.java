package com.epam.health.tool.facade.recap;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.validation.ClusterHealthValidationResult;

public interface IClusterHealthRecapFacade {
    ClusterHealthValidationResult validateClusterHealth(HealthCheckResultsAccumulator healthCheckResultsAccumulator);
}
