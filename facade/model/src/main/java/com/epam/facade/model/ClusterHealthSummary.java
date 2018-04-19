package com.epam.facade.model;

import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.facade.model.projection.ServiceStatusProjection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClusterHealthSummary {
    //todo: rename cluster - also on ui - to cluster snapshot, cluster name is not valid
    private ClusterSnapshotEntityProjection cluster;
    private List<? extends ServiceStatusProjection> serviceStatusList;
    Set<HealthCheckActionType> passedActionTypes = new HashSet<>();

    public ClusterHealthSummary(ClusterSnapshotEntityProjection cluster, List<ServiceStatusProjection> serviceProjectionsBy) {
        setCluster(cluster);
        //todo: this field can be accessed via onetomany field, now refresh can't be called normally, replace with refresh
        setServiceStatusList(serviceProjectionsBy);
    }

    public ClusterHealthSummary(ClusterSnapshotEntityProjection cluster) {
        setCluster(cluster);
        setServiceStatusList(cluster.getClusterServiceShapshotEntityList());
    }

    public ClusterSnapshotEntityProjection getCluster() {
        return cluster;
    }

    public void setCluster(ClusterSnapshotEntityProjection cluster) {
        this.cluster = cluster;
        passedActionTypes.addAll(cluster.getPassedActionTypes());
    }

    public List<? extends ServiceStatusProjection> getServiceStatusList() {
        return serviceStatusList;
    }

    public void setServiceStatusList(List<? extends ServiceStatusProjection> serviceStatusList) {
        this.serviceStatusList = serviceStatusList;
        passedActionTypes.add(HealthCheckActionType.OTHER_SERVICES);
    }

    public Set<HealthCheckActionType> getPassedActionTypes() {
        return passedActionTypes;
    }
}
