package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.common.resolver.ImplementationNotResolvedException;
import com.epam.health.tool.facade.service.IServiceSnapshotFacade;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ServiceSnapshotFacadeImplResolver implements IFacadeImplResolver<IServiceSnapshotFacade> {
    @Autowired
    private Map<String, IServiceSnapshotFacade> serviceSnapshotFacadeMap;

    @Override
    public IServiceSnapshotFacade resolveFacadeImpl(String clusterType) {
        return serviceSnapshotFacadeMap.getOrDefault(clusterType, new IServiceSnapshotFacade() {
            @Override
            public ClusterServiceShapshotEntity getLastServiceSnapshot(String clusterName, String serviceName) {
                throw new ImplementationNotResolvedException();
            }

            @Override
            public ClusterServiceShapshotEntity askForCurrentServiceSnapshot(String clusterName, String serviceName) {
                throw new ImplementationNotResolvedException();
            }
        });
    }
}
