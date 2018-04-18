package com.epam.health.tool.facade.common.service.action;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class CommonRestHealthCheckAction implements IServiceHealthCheckAction {
    @Autowired
    protected HttpAuthenticationClient httpAuthenticationClient;

    @Autowired
    protected ClusterDao clusterDao;

    @Override
    public void performHealthCheck(String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        try {
            saveClusterHealthSummaryToAccumulator( healthCheckResultsAccumulator, performRestHealthCheck( clusterDao.findByClusterName(clusterName) ) );
        } catch (ImplementationNotResolvedException | RuntimeException ex) {
            throw new InvalidResponseException(ex);
        }
    }

    //Use for FS actions
    protected abstract ClusterHealthSummary performRestHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException, ImplementationNotResolvedException;

    protected abstract void saveClusterHealthSummaryToAccumulator( HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                        ClusterHealthSummary clusterHealthSummary );

    //Mb with Transferer
    protected ClusterEntityProjection recreateClusterEntityProjection(ClusterSnapshotEntityProjection clusterSnapshotEntityProjection) {
        ClusterEntityProjectionImpl clusterEntityProjection = new ClusterEntityProjectionImpl();
        //Hack for now, should be changed
        if ( clusterSnapshotEntityProjection.getClusterServiceShapshotEntityList() != null ) {
            clusterEntityProjection.setId( clusterSnapshotEntityProjection.getId() );
            clusterEntityProjection.setClusterType( clusterSnapshotEntityProjection.getClusterType() );
            clusterEntityProjection.setHost( clusterSnapshotEntityProjection.getHost() );
            clusterEntityProjection.setName( clusterSnapshotEntityProjection.getName() );
            clusterEntityProjection.setSecured( clusterEntityProjection.isSecured() );
        }

        return clusterEntityProjection;
    }
}
