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
