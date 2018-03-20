package com.epam.health.tool.facade.resolver;

public interface IFacadeImplResolver<T> {
    T resolveFacadeImpl( String clusterType );
}
