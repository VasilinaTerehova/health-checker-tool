package com.epam.facade.model;

import com.epam.facade.model.projection.ClusterEntityProjection;

import java.util.List;

public class ClusterHealthSummary {
    ClusterEntityProjection cluster;
    List<ServiceStatus> serviceStatusList;

    public ClusterHealthSummary(ClusterEntityProjection cluster, List<ServiceStatus> serviceStatusList) {
        this.cluster = cluster;
        this.serviceStatusList = serviceStatusList;
    }

    public ClusterEntityProjection getCluster() {
        return cluster;
    }

    public void setCluster(ClusterEntityProjection cluster) {
        this.cluster = cluster;
    }

    public List<ServiceStatus> getServiceStatusList() {
        return serviceStatusList;
    }

    public void setServiceStatusList(List<ServiceStatus> serviceStatusList) {
        this.serviceStatusList = serviceStatusList;
    }
}
