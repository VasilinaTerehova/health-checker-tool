package com.epam.health.tool.facade.cluster;

import java.util.List;

public interface IClusterFacade {
    List<Object> getClusterList();
    Object getCluster( String name );
    void saveCluster( String name );
    void updateCluster( String name );
    void deleteCluster( String name );
}
