package com.epam.health.tool.facade.common.service.action;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonRestHealthCheckAction<T> implements IServiceHealthCheckAction {
    @Autowired
    protected HttpAuthenticationClient httpAuthenticationClient;

    @Autowired
    protected ClusterDao clusterDao;

    @Override
    public void performHealthCheck(String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        try {
            saveClusterHealthSummaryToAccumulator( healthCheckResultsAccumulator, performRestHealthCheck( healthCheckResultsAccumulator, clusterDao.findByClusterName(clusterName) ) );
        } catch (ImplementationNotResolvedException | RuntimeException ex) {
            throw new InvalidResponseException(ex);
        }
    }

    //Use for FS actions
    protected abstract T performRestHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterEntity clusterEntity) throws InvalidResponseException, ImplementationNotResolvedException;

    protected abstract void saveClusterHealthSummaryToAccumulator( HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                                   T healthCheckResult );

    //Mb with Transferer
    protected ClusterEntityProjection recreateClusterEntityProjection(ClusterSnapshotEntityProjection clusterSnapshotEntityProjection) {
        ClusterEntityProjectionImpl clusterEntityProjection = new ClusterEntityProjectionImpl();
        //Hack for now, should be changed
        if ( clusterSnapshotEntityProjection.getClusterServiceSnapshotEntityList() != null ) {
            clusterEntityProjection.setId( clusterSnapshotEntityProjection.getId() );
            clusterEntityProjection.setClusterType( clusterSnapshotEntityProjection.getClusterType() );
            clusterEntityProjection.setHost( clusterSnapshotEntityProjection.getHost() );
            clusterEntityProjection.setName( clusterSnapshotEntityProjection.getName() );
            clusterEntityProjection.setSecured( clusterEntityProjection.isSecured() );
        }

        return clusterEntityProjection;
    }
}
