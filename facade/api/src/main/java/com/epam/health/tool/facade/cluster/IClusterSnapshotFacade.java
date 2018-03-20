package com.epam.health.tool.facade.cluster;

import com.epam.health.tool.model.ClusterShapshotEntity;

public interface IClusterSnapshotFacade {
    ClusterShapshotEntity getLastClusterSnapshot(String clusterName );
    ClusterShapshotEntity askForCurrentClusterSnapshot( String clusterName );
}
