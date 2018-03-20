package com.epam.health.tool.facade.common.cluster;

import com.epam.health.tool.facade.cluster.IClusterSnapshotFacade;
import com.epam.health.tool.model.ClusterShapshotEntity;

public abstract class CommonClusterSnapshotFacadeImpl implements IClusterSnapshotFacade {
    public ClusterShapshotEntity getLastClusterSnapshot(String clusterName) {
        return null;
    }
}
