package com.epam.health.tool.model;

import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/6/2018.
 */
public class ClusterServicesStatus {
    Cluster cluster;
    List<ServiceStatus> serviceStatusList;

    public ClusterServicesStatus() {
    }

    public ClusterServicesStatus(Cluster cluster, List<ServiceStatus> serviceStatusList) {
        this.cluster = cluster;
        this.serviceStatusList = serviceStatusList;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public List<ServiceStatus> getServiceStatusList() {
        return serviceStatusList;
    }

    public void setServiceStatusList(List<ServiceStatus> serviceStatusList) {
        this.serviceStatusList = serviceStatusList;
    }
}
