package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.IServiceSnapshotFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ServiceSnapshotFacadeImplResolver implements IFacadeImplResolver<IServiceSnapshotFacade> {
    @Autowired
    private Map<String, IServiceSnapshotFacade> serviceSnapshotFacadeMap;

    @Override
    public IServiceSnapshotFacade resolveFacadeImpl(String clusterType) throws ImplementationNotResolvedException {
        return serviceSnapshotFacadeMap.entrySet().stream().filter( entry -> entry.getKey().contains( clusterType ) )
                .map(Map.Entry::getValue ).findFirst()
                .orElseThrow(() -> new ImplementationNotResolvedException( "Can't find implementation for + " + clusterType ));
    }
}
