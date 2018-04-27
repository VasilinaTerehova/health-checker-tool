package com.epam.health.tool.facade.common.service.action.fs;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.fs.HdfsNamenodeJson;
import com.epam.facade.model.projection.HdfsUsageEntityProjection;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.resolver.action.HealthCheckAction;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Vasilina_Terehova on 4/9/2018.
 */
@Component( CommonActionNames.HDFS_TOTAL_CHECK )
@HealthCheckAction( HealthCheckActionType.HDFS_MEMORY )
public class GetHdfsStatisticsAction extends CommonRestHealthCheckAction<HdfsUsageEntityProjection> {
    @Autowired
    private IFacadeImplResolver<IRunningClusterParamReceiver> runningClusterParamImplResolver;

    @Override
    protected HdfsUsageEntityProjection performRestHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterEntity clusterEntity) throws InvalidResponseException {
        return getAvailableDiskHdfs(clusterEntity.getClusterName());
    }

    @Override
    protected void saveClusterHealthSummaryToAccumulator(HealthCheckResultsAccumulator healthCheckResultsAccumulator, HdfsUsageEntityProjection healthCheckResult) {
        HealthCheckResultsAccumulator.HealthCheckResultsModifier.get( healthCheckResultsAccumulator )
                .setHdfsUsage( healthCheckResult ).modify();
    }

    private HdfsUsageEntityProjection getAvailableDiskHdfs(String clusterName) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        try {
            HdfsNamenodeJson hdfsUsageJson = runningClusterParamImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum().name())
                    .getHdfsNamenodeJson(clusterEntity.getClusterName());

            return hdfsUsageJson;
        } catch (ImplementationNotResolvedException ex) {
            throw new InvalidResponseException("Elements not found.", ex);
        }
    }


}
