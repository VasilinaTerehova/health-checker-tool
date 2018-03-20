package com.epam.health.tool.facade.cluster;

import com.epam.health.tool.model.ClusterEntity;

import java.util.List;

public interface IClusterFacade {
    List<ClusterEntity> getClusterList();
    ClusterEntity getCluster( String name );
    void saveCluster( String name );
    void updateCluster( String name );
    void deleteCluster( String name );
}
