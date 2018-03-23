package com.epam.health.tool.facade.service;

import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;

import java.util.Date;
import java.util.List;

public interface IServiceSnapshotFacade {
    ClusterServiceShapshotEntity getLastServiceSnapshot(String clusterName, String serviceName );
    ClusterServiceShapshotEntity askForCurrentServiceSnapshot( String clusterName, String serviceName );
}
