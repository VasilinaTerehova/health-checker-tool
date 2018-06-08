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

package com.epam.health.tool.facade.common.recap.impl;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.facade.common.recap.IServiceHealthCheckRecapAction;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("hdfs")
public class HdfsServiceHealthCheckRecapAction implements IServiceHealthCheckRecapAction {
    @Override
    public void doRecapHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthValidationResult clusterHealthValidationResult) {
        try {
            ServiceStatusHolder serviceHealthCheckResult = healthCheckResultsAccumulator.getServiceHealthCheckResult(ServiceTypeEnum.HDFS);
            if (!isHdfsCheckSuccess(serviceHealthCheckResult)) {
                clusterHealthValidationResult.setClusterHealthy(false);
                clusterHealthValidationResult.appendErrorSummary(getHdfsCheckErrorsAsString(serviceHealthCheckResult));
            }
        }
        catch ( InvalidResponseException ex ) {
            //logging here
        }
    }

    private boolean isHdfsCheckSuccess(ServiceStatusHolder hdfsHealthCheckResult) {
        return hdfsHealthCheckResult.getJobResults().stream().anyMatch(JobResultProjection::isSuccess);
    }

    private String getHdfsCheckErrorsAsString(ServiceStatusHolder hdfsHealthCheckResult) {
        return hdfsHealthCheckResult.getJobResults().stream().filter(hdfsOperationResult -> !hdfsOperationResult.isSuccess())
                .map(this::getHdfsAlertString).collect(Collectors.joining("\n"));
    }

    private String getHdfsAlertString(JobResultProjection jobResultProjection) {
        return jobResultProjection.getName().concat(" - ").concat(String.join(" ", jobResultProjection.getAlerts()));
    }
}
