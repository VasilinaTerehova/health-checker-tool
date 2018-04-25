package com.epam.health.tool.facade.common.service.action.hdfs;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonSshHealthCheckAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ServiceStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component(CommonActionNames.HDFS_CHECK)
@HealthCheckAction(HealthCheckActionType.HDFS_SERVICE)
public class CommonHdfsServiceHealthCheck extends CommonSshHealthCheckAction {
    @Autowired
    private List<IHdfsOperation> hdfsOperations;

    @Override
    public void performHealthCheck(String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        ServiceStatus hdfsHealthCheckResult = new ServiceStatus();

        hdfsHealthCheckResult.setJobResults(performHdfsOperations(clusterDao.findByClusterName(clusterName)));
    }

    private List<JobResultProjection> performHdfsOperations(ClusterEntity clusterEntity) {
        kinitOnClusterIfNecessary(clusterEntity);

        return hdfsOperations.stream().map(hdfsOperation -> hdfsOperation.perform(clusterEntity)).collect(Collectors.toList());
    }

    private boolean isAllHdfsCheckSuccess(ServiceStatus hdfsHealthCheckResult) {
        return hdfsHealthCheckResult.getJobResults().stream().allMatch(JobResultProjection::isSuccess);
    }

    private boolean isAnyHdfsCheckSuccess(ServiceStatus hdfsHealthCheckResult) {
        return hdfsHealthCheckResult.getJobResults().stream().anyMatch(JobResultProjection::isSuccess);
    }

    private boolean isNoneHdfsCheckSuccess(ServiceStatus hdfsHealthCheckResult) {
        return hdfsHealthCheckResult.getJobResults().stream().noneMatch(JobResultProjection::isSuccess);
    }

    private ServiceStatusEnum getHdfsServiceStatus(ServiceStatus hdfsHealthCheckResult) {
        return isAllHdfsCheckSuccess(hdfsHealthCheckResult) ? ServiceStatusEnum.GOOD
                : isNoneHdfsCheckSuccess(hdfsHealthCheckResult) ? ServiceStatusEnum.BAD
                : ServiceStatusEnum.CONCERNING;
    }
}
