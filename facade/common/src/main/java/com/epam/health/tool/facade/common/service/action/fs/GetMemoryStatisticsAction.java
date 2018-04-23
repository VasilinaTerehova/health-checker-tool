package com.epam.health.tool.facade.common.service.action.fs;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ClusterSnapshotEntityProjectionImpl;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.fs.MemoryMetricsJson;
import com.epam.facade.model.projection.MemoryUsageEntityProjection;
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

import static com.epam.facade.model.service.DownloadableFileConstants.YarnProperties.YARN_RESOURCEMANAGER_WEBAPP_ADDRESS;

/**
 * Created by Vasilina_Terehova on 4/9/2018.
 */
@Component( CommonActionNames.MEMORY_CHECK )
@HealthCheckAction( HealthCheckActionType.MEMORY )
public class GetMemoryStatisticsAction extends CommonRestHealthCheckAction {
    @Autowired
    private IFacadeImplResolver<IRunningClusterParamReceiver> iRunningClusterParamReceiver;

    @Override
    protected ClusterHealthSummary performRestHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        return new ClusterHealthSummary(
                new ClusterSnapshotEntityProjectionImpl(null, null,
                        getMemoryTotal(clusterEntity.getClusterName()), null, null));
    }

    @Override
    protected void saveClusterHealthSummaryToAccumulator(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthSummary clusterHealthSummary) {
        HealthCheckResultsAccumulator.HealthCheckResultsModifier.get( healthCheckResultsAccumulator )
                .setMemoryUsage( clusterHealthSummary.getCluster().getMemoryUsage() ).modify();
    }

    private MemoryUsageEntityProjection getMemoryTotal(String clusterName) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        //todo: get active rm
        try {
            String activeResourceManagerAddress = getActiveResourceManagerAddress(clusterEntity.getClusterName());
            String url = "http://" + activeResourceManagerAddress + "/ws/v1/cluster/metrics";

            System.out.println(url);
            String answer = httpAuthenticationClient.makeAuthenticatedRequest(clusterEntity.getClusterName(), url);
            System.out.println(answer);
            MemoryMetricsJson memoryMetricsJson = CommonJsonHandler.get().getTypedValueFromInnerField(answer, MemoryMetricsJson.class, "clusterMetrics");
            System.out.println(memoryMetricsJson);

            return memoryMetricsJson;
        } catch (CommonUtilException ex) {
            throw new InvalidResponseException("Elements not found.", ex);
        }
    }

    public String getActiveResourceManagerAddress(String clusterName) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        String rmAddress = null;
        try {
            IRunningClusterParamReceiver iRunningClusterParamReceiver = this.iRunningClusterParamReceiver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum().name());
            rmAddress = iRunningClusterParamReceiver.getPropertySiteXml(clusterEntity, DownloadableFileConstants.ServiceFileName.YARN, YARN_RESOURCEMANAGER_WEBAPP_ADDRESS);

            if (CheckingParamsUtil.isParamsNullOrEmpty(rmAddress)) {
                //possibly ha mode for rm
                String haIds = iRunningClusterParamReceiver.getPropertySiteXml(clusterEntity, DownloadableFileConstants.ServiceFileName.YARN, "yarn.resourcemanager.ha.rm-ids");
                //Optional.of(haIds).ifPresent(s -> Optional.of(s.split(",")).ifPresent(strings -> ));
                String[] split = haIds.split(",");
                if (split.length > 0) {
                    String rmId = split[0];
                    rmAddress = iRunningClusterParamReceiver.getPropertySiteXml(clusterEntity, DownloadableFileConstants.ServiceFileName.YARN, YARN_RESOURCEMANAGER_WEBAPP_ADDRESS + "." + rmId);
                }
            }

            System.out.println("rm address: " + rmAddress);
        } catch (ImplementationNotResolvedException e) {
            throw new InvalidResponseException(e);
        }
        return rmAddress;
    }
}
