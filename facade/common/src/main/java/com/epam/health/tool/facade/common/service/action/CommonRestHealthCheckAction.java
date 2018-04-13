package com.epam.health.tool.facade.common.service.action;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ClusterSnapshotEntityProjectionImpl;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class CommonRestHealthCheckAction implements IServiceHealthCheckAction {
    @Autowired
    protected HttpAuthenticationClient httpAuthenticationClient;

    @Override
    public void performHealthCheck(ClusterEntity clusterEntity, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        try {
            saveClusterHealthSummaryToAccumulator( healthCheckResultsAccumulator, performRestHealthCheck( clusterEntity ) );
        } catch (RuntimeException ex) {
            throw new InvalidResponseException(ex);
        }
    }

    //Use for FS actions
    protected abstract String getPropertySiteXml( ClusterEntity clusterEntity, String siteName, String propertyName ) throws InvalidResponseException;
    protected abstract ClusterHealthSummary performRestHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException;

    protected abstract void saveClusterHealthSummaryToAccumulator( HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                        ClusterHealthSummary clusterHealthSummary );

    //Mb with Transferer
    protected ClusterEntityProjection recreateClusterEntityProjection(ClusterSnapshotEntityProjection clusterSnapshotEntityProjection) {
        //Hack for now, should be changed
        if ( clusterSnapshotEntityProjection.getClusterServiceShapshotEntityList() != null ) {
            ClusterEntityProjectionImpl clusterEntityProjection = new ClusterEntityProjectionImpl();

            clusterEntityProjection.setId( clusterSnapshotEntityProjection.getId() );
            clusterEntityProjection.setClusterType( clusterSnapshotEntityProjection.getClusterType() );
            clusterEntityProjection.setHost( clusterSnapshotEntityProjection.getHost() );
            clusterEntityProjection.setName( clusterSnapshotEntityProjection.getName() );
            clusterEntityProjection.setSecured( clusterEntityProjection.isSecured() );

            return clusterEntityProjection;
        }

        return null;
    }
}
