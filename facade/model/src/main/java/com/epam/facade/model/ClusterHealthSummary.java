package com.epam.facade.model;

import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.facade.model.projection.ServiceStatusHolder;

import java.util.List;

public class ClusterHealthSummary {
    //todo: rename cluster - also on ui - to cluster snapshot, cluster name is not valid
    private ClusterSnapshotEntityProjection cluster;
    private List<ServiceStatusHolder> serviceStatusList;

    public ClusterHealthSummary(ClusterSnapshotEntityProjection cluster, List<ServiceStatusHolder> serviceProjectionsBy) {
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

    public List<ServiceStatusHolder> getServiceStatusList() {
        return serviceStatusList;
    }

    public void setServiceStatusList(List<ServiceStatusHolder> serviceStatusList) {
        this.serviceStatusList = serviceStatusList;
    }

}
