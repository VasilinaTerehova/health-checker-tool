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
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.health.tool.model.ClusterTypeEnum;

import java.util.Arrays;

public abstract class ClusterSpecificResolver<T> {
    protected boolean isAvailableForClusterType( T value, ClusterTypeEnum clusterType ) {
        return value.getClass().isAnnotationPresent(ClusterSpecificComponent.class)
                && value.getClass().getAnnotation( ClusterSpecificComponent.class ).value().equals( clusterType );
    }

    protected ClusterTypeEnum getClusterTypeFromString( String clusterType ) throws ImplementationNotResolvedException {
        return Arrays.stream( ClusterTypeEnum.values() ).filter(clusterTypeEnum -> clusterTypeEnum.name().equals( clusterType ) )
                .findFirst().orElseThrow( () -> new ImplementationNotResolvedException( "Invalid cluster type - ".concat( clusterType ) ) );
    }

    protected boolean isNoneClusterType( T value ) {
        return value.getClass().isAnnotationPresent(ClusterSpecificComponent.class)
                && value.getClass().getAnnotation( ClusterSpecificComponent.class ).value().equals( ClusterTypeEnum.NONE );
    }
}
