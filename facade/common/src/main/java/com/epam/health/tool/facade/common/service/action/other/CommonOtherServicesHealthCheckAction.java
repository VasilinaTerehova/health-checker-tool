package com.epam.health.tool.facade.common.service.action.other;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.LogLocation;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.health.tool.dao.cluster.ClusterServiceDao;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.log.IServiceLogSearchFacade;
import com.epam.health.tool.facade.service.status.IServiceStatusReceiver;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterServiceEntity;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import com.epam.util.common.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class CommonOtherServicesHealthCheckAction extends CommonRestHealthCheckAction<List<ServiceStatusHolder>> {
    private final static Logger logger = Logger.getLogger( CommonOtherServicesHealthCheckAction.class );
    @Autowired
    protected SVTransfererManager svTransfererManager;
    @Autowired
    protected IFacadeImplResolver<IServiceStatusReceiver> serviceStatusReceiverIFacadeImplResolver;
    @Autowired
    private IFacadeImplResolver<IServiceLogSearchFacade> serviceLogSearchManagerImplResolver;
    @Autowired
    private ClusterServiceDao clusterServiceDao;

    protected List<ServiceStatusHolder> performHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        try {
            return serviceStatusReceiverIFacadeImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum()).getServiceStatusList(clusterEntity);
        } catch (ImplementationNotResolvedException e) {
            throw new InvalidResponseException("Can't find appropriate ServiceStatusReceiver implementation", e);
        }
    }

    @Override
    protected List<ServiceStatusHolder> performRestHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterEntity clusterEntity) throws InvalidResponseException {
        return getServiceStatuses( healthCheckResultsAccumulator, clusterEntity );
    }

    //Don't clear existing data
    protected void saveClusterHealthSummaryToAccumulator( HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                          List<ServiceStatusHolder> healthCheckResult ) {
        HealthCheckResultsAccumulator.HealthCheckResultsModifier.get( healthCheckResultsAccumulator )
                .setServiceStatusList( healthCheckResult ).modify();
    }

    private List<ServiceStatusHolder> getServiceStatuses(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterEntity clusterEntity) throws InvalidResponseException {
        List<ServiceStatusHolder> serviceStatusList = performHealthCheck(clusterEntity);
        return addLogsPathToService(healthCheckResultsAccumulator, serviceStatusList, clusterEntity);
    }

    private List<ServiceStatusHolder> addLogsPathToService(HealthCheckResultsAccumulator healthCheckResultsAccumulator, List<ServiceStatusHolder> serviceStatuses, ClusterEntity clusterEntity) {
        serviceStatuses.forEach(serviceStatus -> addLogsPathToService(healthCheckResultsAccumulator, serviceStatus, clusterEntity));

        return serviceStatuses;
    }

    private void addLogsPathToService(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ServiceStatusHolder serviceStatus, ClusterEntity clusterEntity) {
        try {
            ClusterServiceEntity byClusterIdAndServiceType = clusterServiceDao.findByClusterIdAndServiceType(clusterEntity.getId(), serviceStatus.getType());
            String logPath = null;
            if (byClusterIdAndServiceType == null) {
                logger.error("Can't find cluster service entity for cluster: " + clusterEntity.getClusterName() + " service: " + serviceStatus.getType());
            } else {
                logPath = byClusterIdAndServiceType.getLogPath();
            }
            if (healthCheckResultsAccumulator.isFullCheck()) {
                LogLocation logLocation = serviceLogSearchManagerImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum().name())
                        .searchLogs(clusterEntity.getClusterName(), serviceStatus.getType());
                String logDirectory = logLocation.getLogPath();
                logger.info("Logs for service " + serviceStatus.getDisplayName() + logDirectory);
                serviceStatus.setLogDirectory(logDirectory);
                serviceStatus.setClusterNode(logLocation.getClusterNode());
            }
        } catch (ImplementationNotResolvedException e) {
            logger.error(e.getMessage());
        }
    }

}
