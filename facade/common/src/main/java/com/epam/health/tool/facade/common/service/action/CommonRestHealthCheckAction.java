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

package com.epam.health.tool.facade.common.service.action;

import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonRestHealthCheckAction<T> implements IServiceHealthCheckAction {
    @Autowired
    protected HttpAuthenticationClient httpAuthenticationClient;

    @Autowired
    protected ClusterDao clusterDao;

    @Override
    public void performHealthCheck(String clusterName, HealthCheckResultsAccumulator healthCheckResultsAccumulator) throws InvalidResponseException {
        try {
            saveClusterHealthSummaryToAccumulator( healthCheckResultsAccumulator, performRestHealthCheck( healthCheckResultsAccumulator, clusterDao.findByClusterName(clusterName) ) );
        } catch (ImplementationNotResolvedException | RuntimeException ex) {
            throw new InvalidResponseException(ex);
        }
    }

    //Use for FS actions
    protected abstract T performRestHealthCheck(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterEntity clusterEntity) throws InvalidResponseException, ImplementationNotResolvedException;

    protected abstract void saveClusterHealthSummaryToAccumulator( HealthCheckResultsAccumulator healthCheckResultsAccumulator,
                                                                   T healthCheckResult );
}
