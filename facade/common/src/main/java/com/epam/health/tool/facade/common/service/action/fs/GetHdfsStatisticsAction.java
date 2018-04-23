package com.epam.health.tool.facade.common.service.action.fs;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ClusterSnapshotEntityProjectionImpl;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.fs.HdfsNamenodeJson;
import com.epam.facade.model.projection.HdfsUsageEntityProjection;
import com.epam.facade.model.service.DownloadableFileConstants;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.facade.model.service.DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTP_ADDRESS;

/**
 * Created by Vasilina_Terehova on 4/9/2018.
 */
@Component( CommonActionNames.HDFS_TOTAL_CHECK )
@HealthCheckAction( HealthCheckActionType.FS )
public class GetHdfsStatisticsAction extends CommonRestHealthCheckAction {
    @Autowired
    private IFacadeImplResolver<IRunningClusterParamReceiver> runningClusterParamImplResolver;

    @Override
    protected ClusterHealthSummary performRestHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        return new ClusterHealthSummary(
                new ClusterSnapshotEntityProjectionImpl(null, null,
                        null, getAvailableDiskHdfs(clusterEntity.getClusterName()), null));
    }

    @Override
    protected void saveClusterHealthSummaryToAccumulator(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthSummary clusterHealthSummary) {
        HealthCheckResultsAccumulator.HealthCheckResultsModifier.get( healthCheckResultsAccumulator )
                .setHdfsUsage( clusterHealthSummary.getCluster().getHdfsUsage() ).modify();
    }

    private HdfsUsageEntityProjection getAvailableDiskHdfs(String clusterName) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        try {
            HdfsNamenodeJson hdfsUsageJson = runningClusterParamImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum().name()).getHdfsNamenodeJson(clusterEntity);

            return hdfsUsageJson;
        } catch (ImplementationNotResolvedException | CommonUtilException ex) {
            throw new InvalidResponseException("Elements not found.", ex);
        }
    }


}
