package com.epam.health.tool.facade.common.recap.impl;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.facade.common.recap.IServiceHealthCheckRecapAction;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component("yarn")
public class YarnServiceHealthCheckRecapAction implements IServiceHealthCheckRecapAction {
    @Override
    public void doRecapHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthValidationResult clusterHealthValidationResult) {
        if ( !isYarnCheckSuccess( healthCheckResultsAccumulator.getYarnHealthCheckResult() ) ) {
            clusterHealthValidationResult.setClusterHealthy( false );
            clusterHealthValidationResult.appendErrorSummary( getYarnCheckErrorsAsString( healthCheckResultsAccumulator.getYarnHealthCheckResult() ) );
        }
    }

    private boolean isYarnCheckSuccess(YarnHealthCheckResult yarnHealthCheckResult ) {
        return yarnHealthCheckResult.getJobResults().stream().anyMatch(YarnHealthCheckResult.YarnJob::isSuccess);
    }

    private String getYarnCheckErrorsAsString( YarnHealthCheckResult yarnHealthCheckResult ) {
        return yarnHealthCheckResult.getJobResults().stream().map( YarnHealthCheckResult.YarnJob::getAlerts )
                .flatMap( Collection::stream ).filter(alert -> !alert.isEmpty() ).collect( Collectors.joining("\n") );
    }
}
