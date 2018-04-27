package com.epam.health.tool.facade.common.resolver;

import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.model.ClusterTypeEnum;

import java.util.Arrays;
import java.util.Map;

public abstract class CommonFacadeImplResolver<T> implements IFacadeImplResolver<T> {
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

    private boolean isAvailableForClusterType( T value, ClusterTypeEnum clusterType ) {
        return value.getClass().isAnnotationPresent(ClusterSpecificComponent.class)
                && value.getClass().getAnnotation( ClusterSpecificComponent.class ).value().equals( clusterType );
    }

    private ClusterTypeEnum getClusterTypeFromString( String clusterType ) throws ImplementationNotResolvedException {
        return Arrays.stream( ClusterTypeEnum.values() ).filter( clusterTypeEnum -> clusterTypeEnum.name().equals( clusterType ) )
                .findFirst().orElseThrow( () -> new ImplementationNotResolvedException( "Invalid cluster type - ".concat( clusterType ) ) );
    }
}
