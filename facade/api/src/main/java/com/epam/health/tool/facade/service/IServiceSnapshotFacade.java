package com.epam.health.tool.facade.service;

import com.epam.health.tool.model.ClusterServiceShapshotEntity;

public interface IServiceSnapshotFacade {
    ClusterServiceShapshotEntity getLastServiceSnapshot(String clusterName, String serviceName );
    ClusterServiceShapshotEntity askForCurrentServiceSnapshot( String clusterName, String serviceName );
}
