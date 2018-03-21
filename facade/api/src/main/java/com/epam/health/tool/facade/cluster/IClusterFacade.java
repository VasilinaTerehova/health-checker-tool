package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.projection.ClusterEntityProjection;

import java.util.List;

public interface IClusterFacade {
    List<ClusterEntityProjection> getClusterList();
    ClusterEntityProjection getCluster( String name );
    void saveCluster( String name );
    void updateCluster( String name );
    void deleteCluster( String name );
}
