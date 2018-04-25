package com.epam.health.tool.facade.common.recap.impl;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.facade.model.projection.ServiceStatusProjection;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.facade.common.recap.IServiceHealthCheckRecapAction;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component("yarn")
public class YarnServiceHealthCheckRecapAction implements IServiceHealthCheckRecapAction {
    @Override
    public void doRecapHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthValidationResult clusterHealthValidationResult) {
        ServiceStatusProjection serviceHealthCheckResult = healthCheckResultsAccumulator.getServiceHealthCheckResult(ServiceTypeEnum.YARN);
        if (!isYarnCheckSuccess(serviceHealthCheckResult)) {
            clusterHealthValidationResult.setClusterHealthy(false);
            clusterHealthValidationResult.appendErrorSummary(getYarnCheckErrorsAsString(serviceHealthCheckResult));
        }
    }

    private boolean isYarnCheckSuccess(ServiceStatusProjection yarnHealthCheckResult) {
        return yarnHealthCheckResult.getJobResults().stream().anyMatch(JobResultProjection::isSuccess);
    }

    private String getYarnCheckErrorsAsString(ServiceStatusProjection yarnHealthCheckResult) {
        return yarnHealthCheckResult.getJobResults().stream().map(JobResultProjection::getAlerts)
                .flatMap(Collection::stream).filter(alert -> !alert.isEmpty()).collect(Collectors.joining("\n"));
    }
}
