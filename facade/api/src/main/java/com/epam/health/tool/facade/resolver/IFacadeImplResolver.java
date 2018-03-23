package com.epam.health.tool.facade.resolver;

import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;

public interface IFacadeImplResolver<T> {
    T resolveFacadeImpl( String clusterType ) throws ImplementationNotResolvedException;
}
