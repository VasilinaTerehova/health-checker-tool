package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.health.tool.model.ClusterShapshotEntity;

public interface IClusterSnapshotFacade {
    ClusterShapshotEntity getLastClusterSnapshot(String clusterName );
    ClusterHealthSummary askForCurrentClusterSnapshot(String clusterName );
}
