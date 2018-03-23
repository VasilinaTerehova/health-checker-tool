package com.epam.health.tool.controller.cluster;

import com.epam.facade.model.projection.impl.ClusterEntityProjectionImpl;

public class Wrapper {
    private ClusterEntityProjectionImpl cluster;

    public ClusterEntityProjectionImpl getCluster() {
        return cluster;
    }

    public void setCluster(ClusterEntityProjectionImpl cluster) {
        this.cluster = cluster;
    }
}
