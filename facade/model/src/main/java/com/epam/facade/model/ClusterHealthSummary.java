package com.epam.facade.model;

import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.facade.model.projection.ServiceStatusProjection;

import java.util.List;

public class ClusterHealthSummary {
    //todo: rename cluster - also on ui - to cluster snapshot, cluster name is not valid
    private ClusterSnapshotEntityProjection cluster;
    private List<ServiceStatusProjection> serviceStatusList;

    public ClusterHealthSummary(ClusterSnapshotEntityProjection cluster, List<ServiceStatusProjection> serviceProjectionsBy) {
        setCluster(cluster);
        //todo: this field can be accessed via onetomany field, now refresh can't be called normally, replace with refresh
        setServiceStatusList(serviceProjectionsBy);
    }

    public ClusterHealthSummary(ClusterSnapshotEntityProjection cluster) {
        setCluster(cluster);
        setServiceStatusList(cluster.getClusterServiceSnapshotEntityList());
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
