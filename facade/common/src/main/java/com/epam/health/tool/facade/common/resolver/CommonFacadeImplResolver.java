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

package com.epam.health.tool.facade.common.resolver;

import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterTypeEnum;

import java.util.Map;

public abstract class CommonFacadeImplResolver<T> extends ClusterSpecificResolver<T> implements IFacadeImplResolver<T> {
    @Override
    public T resolveFacadeImpl( String clusterType ) throws ImplementationNotResolvedException {
        return resolveFacadeImpl( getClusterTypeFromString( clusterType ) );
    }

    public T resolveFacadeImpl( ClusterTypeEnum clusterType ) throws ImplementationNotResolvedException {
        return getFacadeImplBeansMap().entrySet().stream()
                .map( Map.Entry::getValue ).filter( facade -> isAvailableForClusterType( facade, clusterType ) ).findFirst()
                .orElseThrow(() -> new ImplementationNotResolvedException( "Can't find implementation for + " + clusterType ));
    }

    //Is map necessary, maybe list?
    protected abstract Map<String, T> getFacadeImplBeansMap();
}
