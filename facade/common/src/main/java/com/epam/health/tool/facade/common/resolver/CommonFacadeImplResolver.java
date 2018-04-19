package com.epam.health.tool.facade.common.resolver;

import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;

import java.util.Map;

public abstract class CommonFacadeImplResolver<T> implements IFacadeImplResolver<T> {
    @Override
    public T resolveFacadeImpl(String clusterType) throws ImplementationNotResolvedException {
        return getFacadeImplBeansMap().entrySet().stream().filter( entry -> entry.getKey().contains( clusterType ) )
                .map(Map.Entry::getValue ).findFirst()
                .orElseThrow(() -> new ImplementationNotResolvedException( "Can't find implementation for + " + clusterType ));
    }

    protected abstract Map<String, T> getFacadeImplBeansMap();
}
