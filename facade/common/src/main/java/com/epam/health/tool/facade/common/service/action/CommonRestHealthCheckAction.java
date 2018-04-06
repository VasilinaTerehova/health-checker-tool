package com.epam.health.tool.facade.common.service.action;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ClusterSnapshotEntityProjectionImpl;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class CommonRestHealthCheckAction implements IServiceHealthCheckAction {
    @Autowired
    private HttpAuthenticationClient httpAuthenticationClient;
    @Autowired
    protected SVTransfererManager svTransfererManager;

    @Override
    public void performHealthCheck(ClusterEntity clusterEntity, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        try {
            //Can be Flux.just( ServiceTypeEnum.values() ).#operations...
            healthCheckResultsAccumulator.setClusterHealthSummary(
                    new ClusterHealthSummary(
                            new ClusterSnapshotEntityProjectionImpl( mapEntityToProjection( clusterEntity ), performHealthCheck( clusterEntity ) )));
        } catch (RuntimeException ex) {
            throw new InvalidResponseException(ex);
        }
    }

    protected abstract List<ServiceStatus> performHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException;

    private ClusterEntityProjection mapEntityToProjection(ClusterEntity clusterEntity ) {
        return svTransfererManager.<ClusterEntity, ClusterEntityProjectionImpl>getTransferer( ClusterEntity.class, ClusterEntityProjectionImpl.class )
                .transfer( clusterEntity, ClusterEntityProjectionImpl.class );
    }
}
