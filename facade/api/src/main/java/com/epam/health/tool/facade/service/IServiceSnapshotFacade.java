package com.epam.health.tool.facade.service;

import com.epam.health.tool.model.ClusterServiceSnapshotEntity;

public interface IServiceSnapshotFacade {

    ClusterServiceSnapshotEntity getLastServiceSnapshot(String clusterName, String serviceName );

    ClusterServiceSnapshotEntity askForCurrentServiceSnapshot(String clusterName, String serviceName );
}
