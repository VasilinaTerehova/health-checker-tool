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
