/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.health.tool.facade.common.service.action.fs;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.fs.MemoryMetricsJson;
import com.epam.facade.model.projection.MemoryUsageEntityProjection;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.resolver.action.HealthCheckAction;
import com.epam.health.tool.facade.common.service.action.CommonActionNames;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
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
    protected MemoryUsageEntityProjection performRestHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterEntity clusterEntity) throws InvalidResponseException {
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
