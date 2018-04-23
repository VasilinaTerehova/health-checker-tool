package com.epam.health.tool.facade.common.service.action.other;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ClusterSnapshotEntityProjectionImpl;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.log.IServiceLogSearchFacade;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class CommonOtherServicesHealthCheckAction extends CommonRestHealthCheckAction {
    @Autowired
    protected SVTransfererManager svTransfererManager;
    @Autowired
    private IFacadeImplResolver<IServiceLogSearchFacade> serviceLogSearchManagerImplResolver;
    private final static Logger logger = Logger.getLogger( CommonOtherServicesHealthCheckAction.class );

    protected abstract List<ServiceStatus> performHealthCheck( ClusterEntity clusterEntity ) throws InvalidResponseException;

    @Override
    protected ClusterHealthSummary performRestHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        return new ClusterHealthSummary(
                new ClusterSnapshotEntityProjectionImpl( mapEntityToProjection( clusterEntity ), getServiceStatuses( clusterEntity ),
                        null, null, null));
    }

    //Don't clear existing data
    protected void saveClusterHealthSummaryToAccumulator( HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                          ClusterHealthSummary clusterHealthSummary ) {
        HealthCheckResultsAccumulator.HealthCheckResultsModifier.get( healthCheckResultsAccumulator )
                .setServiceStatusList( clusterHealthSummary.getServiceStatusList() ).modify();
    }

    private List<ServiceStatus> getServiceStatuses( ClusterEntity clusterEntity ) throws InvalidResponseException {
        return addLogsPathToService( performHealthCheck( clusterEntity ), clusterEntity );
    }

    private List<ServiceStatus> addLogsPathToService( List<ServiceStatus> serviceStatuses, ClusterEntity clusterEntity ) {
        serviceStatuses.forEach( serviceStatus -> addLogsPathToService( serviceStatus, clusterEntity ) );

        return serviceStatuses;
    }

    private void addLogsPathToService(ServiceStatus serviceStatus, ClusterEntity clusterEntity) {
        try {
            logger.info( "Logs for service " + serviceStatus.getDisplayName() + serviceLogSearchManagerImplResolver.resolveFacadeImpl( clusterEntity.getClusterTypeEnum().name() )
                    .searchLogs( clusterEntity.getClusterName(), serviceStatus.getType() ) );
        } catch (ImplementationNotResolvedException e) {
            logger.error( e.getMessage() );
        }
    }

    private ClusterEntityProjection mapEntityToProjection(ClusterEntity clusterEntity ) {
        return svTransfererManager.<ClusterEntity, ClusterEntityProjectionImpl>getTransferer( ClusterEntity.class, ClusterEntityProjectionImpl.class )
                .transfer( clusterEntity, ClusterEntityProjectionImpl.class );
    }
}
