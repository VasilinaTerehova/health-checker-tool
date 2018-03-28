package com.epam.facade.model;

import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.facade.model.projection.ServiceStatusProjection;

import java.util.ArrayList;
import java.util.List;

public class ClusterHealthSummary {
    private ClusterSnapshotEntityProjection cluster;
    private List<ServiceStatusProjection> serviceStatusList;

    public ClusterHealthSummary(ClusterSnapshotEntityProjection cluster, List<ServiceStatusProjection> serviceProjectionsBy) {
        this.cluster = cluster;
        //todo: this field can be accessed via onetomany field, now refresh can't be called normally, replace with refresh
        this.serviceStatusList = serviceProjectionsBy;
    }

    public ClusterHealthSummary(ClusterSnapshotEntityProjection cluster) {
        this.cluster = cluster;
        this.serviceStatusList = cluster.getClusterServiceShapshotEntityList();
    }

    public ClusterSnapshotEntityProjection getCluster() {
        return cluster;
    }

    public void setCluster(ClusterSnapshotEntityProjection cluster) {
        this.cluster = cluster;
    }

    public List<ServiceStatusProjection> getServiceStatusList() {
        return serviceStatusList;
    }

    public void setServiceStatusList(List<ServiceStatusProjection> serviceStatusList) {
        this.serviceStatusList = serviceStatusList;
    }
}
