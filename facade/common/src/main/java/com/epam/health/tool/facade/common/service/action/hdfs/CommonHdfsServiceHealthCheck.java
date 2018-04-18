package com.epam.health.tool.facade.common.service.action.hdfs;

import com.epam.facade.model.accumulator.HdfsHealthCheckResult;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonSshHealthCheckAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ServiceStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component( CommonActionNames.HDFS_CHECK )
@HealthCheckAction( HealthCheckActionType.HDFS_SERVICE )
public class CommonHdfsServiceHealthCheck extends CommonSshHealthCheckAction {
    @Autowired
    private List<IHdfsOperation> hdfsOperations;

    @Override
    public void performHealthCheck(String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        HdfsHealthCheckResult hdfsHealthCheckResult = new HdfsHealthCheckResult();

        hdfsHealthCheckResult.setJobResults( performHdfsOperations( clusterDao.findByClusterName(clusterName) ) );
        hdfsHealthCheckResult.setStatus( getHdfsServiceStatus( hdfsHealthCheckResult ) );

        healthCheckResultsAccumulator.setHdfsHealthCheckResult( hdfsHealthCheckResult );
    }

    private List<HdfsHealthCheckResult.HdfsOperationResult> performHdfsOperations(ClusterEntity clusterEntity ) {
        kinitOnClusterIfNecessary( clusterEntity );

        return hdfsOperations.stream().map( hdfsOperation -> hdfsOperation.perform( clusterEntity ) ).collect(Collectors.toList());
    }

    private boolean isAllHdfsCheckSuccess( HdfsHealthCheckResult hdfsHealthCheckResult ) {
        return hdfsHealthCheckResult.getJobResults().stream().allMatch( HdfsHealthCheckResult.HdfsOperationResult::isSuccess );
    }

    private boolean isAnyHdfsCheckSuccess( HdfsHealthCheckResult hdfsHealthCheckResult ) {
        return hdfsHealthCheckResult.getJobResults().stream().anyMatch( HdfsHealthCheckResult.HdfsOperationResult::isSuccess );
    }

    private boolean isNoneHdfsCheckSuccess( HdfsHealthCheckResult hdfsHealthCheckResult ) {
        return hdfsHealthCheckResult.getJobResults().stream().noneMatch( HdfsHealthCheckResult.HdfsOperationResult::isSuccess );
    }

    private ServiceStatusEnum getHdfsServiceStatus( HdfsHealthCheckResult hdfsHealthCheckResult ) {
        return isAllHdfsCheckSuccess( hdfsHealthCheckResult ) ? ServiceStatusEnum.GOOD
                : isNoneHdfsCheckSuccess( hdfsHealthCheckResult ) ? ServiceStatusEnum.BAD
                : ServiceStatusEnum.CONCERNING;
    }
}
