package com.epam.health.tool.facade.common.recap;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.facade.recap.IClusterHealthRecapFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClusterHealthRecapFacadeImpl implements IClusterHealthRecapFacade {
    @Autowired
    private List<IServiceHealthCheckRecapAction> serviceHealthCheckRecapActions;

    @Override
    public ClusterHealthValidationResult validateClusterHealth(HealthCheckResultsAccumulator healthCheckResultsAccumulator) {
        ClusterHealthValidationResult clusterHealthValidationResult = new ClusterHealthValidationResult( true );
        serviceHealthCheckRecapActions.forEach( serviceHealthCheckRecapAction -> serviceHealthCheckRecapAction
                .doRecapHealthCheck( healthCheckResultsAccumulator, clusterHealthValidationResult ) );

        return clusterHealthValidationResult;
    }
}
