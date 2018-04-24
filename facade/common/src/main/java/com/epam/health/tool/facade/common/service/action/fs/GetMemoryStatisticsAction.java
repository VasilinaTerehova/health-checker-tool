package com.epam.health.tool.facade.common.service.action.fs;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.fs.MemoryMetricsJson;
import com.epam.facade.model.projection.MemoryUsageEntityProjection;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Vasilina_Terehova on 4/9/2018.
 */
@Component( CommonActionNames.MEMORY_CHECK )
@HealthCheckAction( HealthCheckActionType.MEMORY )
public class GetMemoryStatisticsAction extends CommonRestHealthCheckAction<MemoryUsageEntityProjection> {
    @Autowired
    private IFacadeImplResolver<IRunningClusterParamReceiver> iRunningClusterParamReceiver;

    @Override
    protected MemoryUsageEntityProjection performRestHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        return getMemoryTotal(clusterEntity.getClusterName());
    }

    @Override
    protected void saveClusterHealthSummaryToAccumulator(HealthCheckResultsAccumulator healthCheckResultsAccumulator, MemoryUsageEntityProjection healthCheckResult) {
        HealthCheckResultsAccumulator.HealthCheckResultsModifier.get( healthCheckResultsAccumulator )
                .setMemoryUsage( healthCheckResult ).modify();
    }

    private MemoryUsageEntityProjection getMemoryTotal(String clusterName) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        try {
            String activeResourceManagerAddress = iRunningClusterParamReceiver.resolveFacadeImpl( clusterEntity.getClusterTypeEnum() )
                    .getActiveResourceManagerAddress(clusterEntity.getClusterName());
            String url = activeResourceManagerAddress + "/ws/v1/cluster/metrics";

            System.out.println(url);
            String answer = httpAuthenticationClient.makeAuthenticatedRequest(clusterEntity.getClusterName(), url);
            System.out.println(answer);
            MemoryMetricsJson memoryMetricsJson = CommonJsonHandler.get().getTypedValueFromInnerField(answer, MemoryMetricsJson.class, "clusterMetrics");
            System.out.println(memoryMetricsJson);

            return memoryMetricsJson;
        }
        catch ( CommonUtilException | AuthenticationRequestException | ImplementationNotResolvedException ex ) {
            throw new InvalidResponseException("Elements not found.", ex);
        }
    }
}
