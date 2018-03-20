package com.epam.health.tool.facade.common.service;

import com.epam.health.tool.facade.service.IServiceSnapshotFacade;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;

public abstract class CommonServiceSnapshotFacadeImpl implements IServiceSnapshotFacade {
    public ClusterServiceShapshotEntity getLastServiceSnapshot(String clusterName, String serviceName) {
        return null;
    }
}
