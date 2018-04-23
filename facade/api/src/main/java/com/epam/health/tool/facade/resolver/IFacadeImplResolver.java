package com.epam.health.tool.facade.resolver;

import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.model.ClusterTypeEnum;

public interface IFacadeImplResolver<T> {
    T resolveFacadeImpl( String clusterType ) throws ImplementationNotResolvedException;
    T resolveFacadeImpl( ClusterTypeEnum clusterType ) throws ImplementationNotResolvedException;
}
