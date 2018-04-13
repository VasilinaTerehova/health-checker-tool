package com.epam.health.tool.controller;

import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseFacadeResolvingController {
    @Autowired
    private IClusterFacade clusterFacade;

    protected  <T> T resolveClusterSnapshotFacade( String clusterName, IFacadeImplResolver<T> facadeImplResolver ) throws ImplementationNotResolvedException {
        return facadeImplResolver.resolveFacadeImpl( clusterFacade.getCluster( clusterName ).getClusterType().name() );
    }
}
