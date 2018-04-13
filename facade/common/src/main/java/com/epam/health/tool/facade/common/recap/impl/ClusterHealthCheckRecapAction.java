package com.epam.health.tool.facade.common.recap.impl;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.ServiceStatusProjection;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.facade.common.recap.IServiceHealthCheckRecapAction;
import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("cluster")
public class ClusterHealthCheckRecapAction implements IServiceHealthCheckRecapAction {
    @Override
    public void doRecapHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthValidationResult clusterHealthValidationResult) {
        if ( isClusterServicesUnHealthy( healthCheckResultsAccumulator.getClusterHealthSummary() ) ) {
            clusterHealthValidationResult.appendErrorSummary( getServiceBadList( healthCheckResultsAccumulator.getClusterHealthSummary() ) );
        }
    }

    private boolean isClusterServicesUnHealthy(ClusterHealthSummary clusterHealthSummary) {
        return clusterHealthSummary.getServiceStatusList().stream()
                .map(ServiceStatusProjection::getHealthSummary).anyMatch( this::isServiceBad );
    }

    private String getServiceBadList( ClusterHealthSummary clusterHealthSummary ) {
        return clusterHealthSummary.getServiceStatusList().stream().filter( this::isServiceBad )
                .map( ServiceStatusProjection::getDisplayName ).map( this::createServiceBadString )
                .collect( Collectors.joining( " " ) );
    }

    private boolean isServiceBad(ServiceStatusEnum serviceStatus) {
        return serviceStatus.equals( ServiceStatusEnum.BAD );
    }

    private boolean isServiceBad(ServiceStatusProjection serviceStatusProjection) {
        return serviceStatusProjection.getHealthSummary() != null && serviceStatusProjection.getHealthSummary().equals( ServiceStatusEnum.BAD );
    }

    private String createServiceBadString(ServiceTypeEnum serviceType) {
        return "Service " + serviceType.toString() + " is BAD";
    }
}
