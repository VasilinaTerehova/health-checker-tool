package com.epam.health_tool.controller;

/**
 * Created by Vasilina_Terehova on 3/3/2018.
 */
public class ClusterHealthStatus {
    private String clusterName;
    private boolean health;

    public ClusterHealthStatus() {
    }

    public ClusterHealthStatus(String clusterName, boolean health) {
        this.clusterName = clusterName;
        this.health = health;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public boolean isHealth() {
        return health;
    }

    public void setHealth(boolean health) {
        this.health = health;
    }
}
