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

package com.epam.health.tool.controller;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.ClusterAccumulatorToken;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseFacadeResolvingController {
    @Autowired
    private IClusterFacade clusterFacade;

    protected ClusterAccumulatorToken buildAccumulatorToken(String clusterName, String token, HealthCheckActionType type, boolean useSave ) {
        return ClusterAccumulatorToken.Builder.get().withClusterName( clusterName )
                .withToken( token ).withType( type ).useSave( useSave ).buildClusterAccumulatorToken();
    }

    //Short form, develop only
    protected ClusterAccumulatorToken buildAccumulatorToken( String clusterName, HealthCheckActionType type ) {
        return ClusterAccumulatorToken.Builder.get().withClusterName( clusterName )
                .withType( type ).buildClusterAccumulatorToken();
    }

    protected  <T> T resolveClusterSnapshotFacade( String clusterName, IFacadeImplResolver<T> facadeImplResolver ) throws ImplementationNotResolvedException {
        return facadeImplResolver.resolveFacadeImpl( clusterFacade.getCluster( clusterName ).getClusterType().name() );
    }
}
