package com.epam.health.tool.facade.common.service;

import com.epam.health.tool.facade.service.IServiceSnapshotFacade;
import com.epam.health.tool.model.ClusterServiceSnapshotEntity;

public abstract class CommonServiceSnapshotFacadeImpl implements IServiceSnapshotFacade {

    public ClusterServiceSnapshotEntity getLastServiceSnapshot(String clusterName, String serviceName) {
        return null;
    }

}
