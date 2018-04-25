package com.epam.health.tool.facade.common.service.action.hdfs;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonSshHealthCheckAction;
import com.epam.health.tool.facade.common.service.action.yarn.CommonYarnServiceHealthCheckActionImpl;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.status.IServiceStatusReceiver;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component(CommonActionNames.HDFS_CHECK)
@HealthCheckAction(HealthCheckActionType.HDFS_SERVICE)
public class CommonHdfsServiceHealthCheck extends CommonSshHealthCheckAction {
    @Autowired
    protected ClusterDao clusterDao;
    @Autowired
    private List<IHdfsOperation> hdfsOperations;
    @Autowired
    private IFacadeImplResolver<IServiceStatusReceiver> serviceStatusReceiverIFacadeImplResolver;

    @Override
    public void performHealthCheck(String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        try {
            ServiceStatusHolder serviceStatus = getServiceStatus(clusterEntity, healthCheckResultsAccumulator);
            serviceStatus.setJobResults(performHdfsOperations(clusterEntity));
            serviceStatus.setHealthSummary(CommonYarnServiceHealthCheckActionImpl.mergeJobResultsWithRestStatus(serviceStatus.getHealthSummary(), getHdfsServiceStatus(serviceStatus)));
            healthCheckResultsAccumulator.addServiceStatus(serviceStatus);
        } catch (ImplementationNotResolvedException e) {
            throw new InvalidResponseException("Can't find according implementation for vendor " + clusterEntity.getClusterTypeEnum(), e);
        }
    }

    private ServiceStatusHolder getServiceStatus(ClusterEntity clusterEntity, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException, ImplementationNotResolvedException {
        Optional<ServiceStatusHolder> serviceHealthCheckResultIfExists = healthCheckResultsAccumulator.getServiceHealthCheckResultIfExists(ServiceTypeEnum.HDFS);
        return serviceHealthCheckResultIfExists.orElse(serviceStatusReceiverIFacadeImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum()).getServiceStatus(clusterEntity, ServiceTypeEnum.HDFS));
    }

    private List<JobResultProjection> performHdfsOperations(ClusterEntity clusterEntity) {
        kinitOnClusterIfNecessary(clusterEntity);

        return hdfsOperations.stream().map(hdfsOperation -> hdfsOperation.perform(clusterEntity)).collect(Collectors.toList());
    }

    private boolean isAllHdfsCheckSuccess(ServiceStatusHolder hdfsHealthCheckResult) {
        return hdfsHealthCheckResult.getJobResults().stream().allMatch(JobResultProjection::isSuccess);
    }

    private boolean isAnyHdfsCheckSuccess(ServiceStatusHolder hdfsHealthCheckResult) {
        return hdfsHealthCheckResult.getJobResults().stream().anyMatch(JobResultProjection::isSuccess);
    }

    private boolean isNoneHdfsCheckSuccess(ServiceStatusHolder hdfsHealthCheckResult) {
        return hdfsHealthCheckResult.getJobResults().stream().noneMatch(JobResultProjection::isSuccess);
    }

    private ServiceStatusEnum getHdfsServiceStatus(ServiceStatusHolder hdfsHealthCheckResult) {
        return isAllHdfsCheckSuccess(hdfsHealthCheckResult) ? ServiceStatusEnum.GOOD
                : isNoneHdfsCheckSuccess(hdfsHealthCheckResult) ? ServiceStatusEnum.BAD
                : ServiceStatusEnum.CONCERNING;
    }
}
