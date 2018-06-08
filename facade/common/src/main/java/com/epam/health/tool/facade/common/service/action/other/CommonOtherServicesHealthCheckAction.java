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

package com.epam.health.tool.facade.common.service.action.other;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.health.tool.dao.cluster.ClusterServiceDao;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.log.IServiceLogSearchFacade;
import com.epam.health.tool.facade.service.status.IServiceStatusReceiver;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public abstract class CommonOtherServicesHealthCheckAction extends CommonRestHealthCheckAction<List<ServiceStatusHolder>> {
    private final static Logger logger = Logger.getLogger(CommonOtherServicesHealthCheckAction.class);
    @Autowired
    private IFacadeImplResolver<IServiceStatusReceiver> serviceStatusReceiverIFacadeImplResolver;
    @Autowired
    private IFacadeImplResolver<IServiceLogSearchFacade> serviceLogSearchManagerImplResolver;
    @Autowired
    private ClusterServiceDao clusterServiceDao;

    protected List<ServiceStatusHolder> performHealthCheck(ClusterEntity clusterEntity) throws InvalidResponseException {
        try {
            return serviceStatusReceiverIFacadeImplResolver.resolveFacadeImpl(clusterEntity.getClusterTypeEnum()).getServiceStatusList(clusterEntity);
        } catch (ImplementationNotResolvedException e) {
            throw new InvalidResponseException("Can't find appropriate ServiceStatusReceiver implementation", e);
        }
    }

    @Override
    protected List<ServiceStatusHolder> performRestHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                               ClusterEntity clusterEntity) throws InvalidResponseException {
        return getServiceStatuses(healthCheckResultsAccumulator, clusterEntity);
    }

    //Don't clear existing data
    protected void saveClusterHealthSummaryToAccumulator(HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                         List<ServiceStatusHolder> healthCheckResult) {
        HealthCheckResultsAccumulator.HealthCheckResultsModifier.get(healthCheckResultsAccumulator)
                .setServiceStatusList(healthCheckResult).modify();
    }

    private List<ServiceStatusHolder> getServiceStatuses(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterEntity clusterEntity) throws InvalidResponseException {
        List<ServiceStatusHolder> serviceStatusList = performHealthCheck(clusterEntity).stream().filter(serviceStatusHolder ->
                !areExcludedFromOtherServices(serviceStatusHolder)).collect(Collectors.toList());
        return addLogsPathToService(healthCheckResultsAccumulator, serviceStatusList, clusterEntity);
    }

    private List<ServiceStatusHolder> addLogsPathToService(HealthCheckResultsAccumulator healthCheckResultsAccumulator, List<ServiceStatusHolder> serviceStatuses,

                                                           ClusterEntity clusterEntity) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(serviceStatuses.size());
        forkJoinPool.submit(() ->
                serviceStatuses.forEach(serviceStatus -> {
                    String clusterType = clusterEntity.getClusterTypeEnum().name();
                    try {
                        serviceLogSearchManagerImplResolver.resolveFacadeImpl(clusterType).
                                addLogsPathToService(healthCheckResultsAccumulator, serviceStatus, clusterEntity);
                    } catch (ImplementationNotResolvedException e) {
                        logger.error("can't find implementation for " + clusterType + " for log service", e);
                        throw new RuntimeException(e);
                    }
                })).join();

        return serviceStatuses;
    }

    private boolean areExcludedFromOtherServices(ServiceStatusHolder serviceStatusHolder) {
        return serviceStatusHolder.getType().equals(ServiceTypeEnum.HDFS) && serviceStatusHolder.getType().equals(ServiceTypeEnum.YARN);
    }


}
