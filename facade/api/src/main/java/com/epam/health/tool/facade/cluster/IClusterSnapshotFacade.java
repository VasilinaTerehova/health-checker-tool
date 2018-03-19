package com.epam.health.tool.facade.cluster;

public interface IClusterSnapshotFacade {
    Object getLastClusterSnapshot( String clusterName );
    Object askForCurrentClusterSnapshot( String clusterName );
}
