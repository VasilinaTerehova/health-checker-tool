package com.epam.health.tool.facade.common.service.action.other;

import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
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

public abstract class CommonOtherServicesHealthCheckAction extends CommonRestHealthCheckAction<List<ServiceStatus>> {
    @Autowired
    protected SVTransfererManager svTransfererManager;
    @Autowired
    private IFacadeImplResolver<IServiceLogSearchFacade> serviceLogSearchManagerImplResolver;
    private final static Logger logger = Logger.getLogger( CommonOtherServicesHealthCheckAction.class );

    protected abstract List<ServiceStatus> performHealthCheck( ClusterEntity clusterEntity ) throws InvalidResponseException;

    @Override
    protected List<ServiceStatus> performRestHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        return getServiceStatuses( clusterEntity );
    }

    //Don't clear existing data
    protected void saveClusterHealthSummaryToAccumulator( HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                          List<ServiceStatus> healthCheckResult ) {
        HealthCheckResultsAccumulator.HealthCheckResultsModifier.get( healthCheckResultsAccumulator )
                .setServiceStatusList( healthCheckResult ).modify();
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
}
