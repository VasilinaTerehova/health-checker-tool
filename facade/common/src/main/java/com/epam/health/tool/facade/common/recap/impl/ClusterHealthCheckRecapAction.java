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
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.facade.model.validation.ClusterHealthValidationResult;
import com.epam.health.tool.facade.common.recap.IServiceHealthCheckRecapAction;
import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("cluster")
public class ClusterHealthCheckRecapAction implements IServiceHealthCheckRecapAction {
    @Override
    public void doRecapHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthValidationResult clusterHealthValidationResult) {
        if ( isClusterServicesUnHealthy( healthCheckResultsAccumulator.getServiceStatusList() ) ) {
            clusterHealthValidationResult.appendErrorSummary( getServiceBadList( healthCheckResultsAccumulator.getServiceStatusList() ) );
        }
    }

    private boolean isClusterServicesUnHealthy( List<? extends ServiceStatusHolder> serviceStatusProjections ) {
        return serviceStatusProjections.stream().map(ServiceStatusHolder::getHealthSummary).anyMatch( this::isServiceBad );
    }

    private String getServiceBadList( List<? extends ServiceStatusHolder> serviceStatusProjections ) {
        return serviceStatusProjections.stream().filter( this::isServiceBad )
                .map( ServiceStatusHolder::getDisplayName ).map( this::createServiceBadString )
                .collect( Collectors.joining( " " ) );
    }

    private boolean isServiceBad(ServiceStatusEnum serviceStatus) {
        return serviceStatus.equals( ServiceStatusEnum.BAD );
    }

    private boolean isServiceBad(ServiceStatusHolder serviceStatusHolder) {
        return serviceStatusHolder.getHealthSummary() != null && serviceStatusHolder.getHealthSummary().equals( ServiceStatusEnum.BAD );
    }

    private String createServiceBadString(ServiceTypeEnum serviceType) {
        return "Service " + serviceType.toString() + " is BAD";
    }
}
