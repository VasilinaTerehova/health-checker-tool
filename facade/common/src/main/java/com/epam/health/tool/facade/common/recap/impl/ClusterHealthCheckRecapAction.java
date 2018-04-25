package com.epam.health.tool.facade.common.recap.impl;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.facade.common.recap.IServiceHealthCheckRecapAction;
import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("cluster")
public class ClusterHealthCheckRecapAction implements IServiceHealthCheckRecapAction {
    @Override
    public void doRecapHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthValidationResult clusterHealthValidationResult) {
        if ( isClusterServicesUnHealthy( healthCheckResultsAccumulator.getServiceStatusList() ) ) {
            clusterHealthValidationResult.appendErrorSummary( getServiceBadList( healthCheckResultsAccumulator.getServiceStatusList() ) );
        }
    }

    private boolean isClusterServicesUnHealthy( List<? extends ServiceStatusHolder> serviceStatusProjections ) {
        return serviceStatusProjections.stream().map(ServiceStatusHolder::getHealthSummary).anyMatch( this::isServiceBad );
    }

    private String getServiceBadList( List<? extends ServiceStatusHolder> serviceStatusProjections ) {
        return serviceStatusProjections.stream().filter( this::isServiceBad )
                .map( ServiceStatusHolder::getDisplayName ).map( this::createServiceBadString )
                .collect( Collectors.joining( " " ) );
    }

    private boolean isServiceBad(ServiceStatusEnum serviceStatus) {
        return serviceStatus.equals( ServiceStatusEnum.BAD );
    }

    private boolean isServiceBad(ServiceStatusHolder serviceStatusHolder) {
        return serviceStatusHolder.getHealthSummary() != null && serviceStatusHolder.getHealthSummary().equals( ServiceStatusEnum.BAD );
    }

    private String createServiceBadString(ServiceTypeEnum serviceType) {
        return "Service " + serviceType.toString() + " is BAD";
    }
}
