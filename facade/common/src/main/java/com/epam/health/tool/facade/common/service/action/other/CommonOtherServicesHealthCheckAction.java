package com.epam.health.tool.facade.common.service.action.other;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ClusterSnapshotEntityProjectionImpl;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class CommonOtherServicesHealthCheckAction extends CommonRestHealthCheckAction {
    @Autowired
    protected SVTransfererManager svTransfererManager;

    protected abstract List<ServiceStatus> performHealthCheck( ClusterEntity clusterEntity ) throws InvalidResponseException;

    @Override
    protected ClusterHealthSummary performRestHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        return new ClusterHealthSummary(
                new ClusterSnapshotEntityProjectionImpl( mapEntityToProjection( clusterEntity ), performHealthCheck( clusterEntity ),
                        null, null, null));
    }

    //Don't clear existing data
    protected void saveClusterHealthSummaryToAccumulator( HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                          ClusterHealthSummary clusterHealthSummary ) {
        ClusterHealthSummary tempClusterHealthSummary = healthCheckResultsAccumulator.getClusterHealthSummary();

        if ( tempClusterHealthSummary == null ) {
            tempClusterHealthSummary = clusterHealthSummary;
        }
        else {
            tempClusterHealthSummary = new ClusterHealthSummary(
                    new ClusterSnapshotEntityProjectionImpl( recreateClusterEntityProjection( clusterHealthSummary.getCluster() ),
                            clusterHealthSummary.getServiceStatusList(), tempClusterHealthSummary.getCluster().getMemoryUsage(),
                            tempClusterHealthSummary.getCluster().getHdfsUsage(), tempClusterHealthSummary.getCluster().getNodes()) );
        }

        healthCheckResultsAccumulator.setClusterHealthSummary( tempClusterHealthSummary );
    }

    //Mock for now
    @Override
    protected String getPropertySiteXml(ClusterEntity clusterEntity, String siteName, String propertyName) throws InvalidResponseException {
        return null;
    }

    private ClusterEntityProjection mapEntityToProjection(ClusterEntity clusterEntity ) {
        return svTransfererManager.<ClusterEntity, ClusterEntityProjectionImpl>getTransferer( ClusterEntity.class, ClusterEntityProjectionImpl.class )
                .transfer( clusterEntity, ClusterEntityProjectionImpl.class );
    }
}
