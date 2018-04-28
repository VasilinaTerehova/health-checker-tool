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
