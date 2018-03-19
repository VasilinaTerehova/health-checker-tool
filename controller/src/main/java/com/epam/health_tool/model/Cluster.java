package com.epam.health_tool.model;

import com.epam.health_tool.authenticate.impl.ClusterType;

/**
 * Created by Vasilina_Terehova on 3/6/2018.
 */
public class Cluster {
    //@JsonProperty("name")
    String name;
    //@JsonProperty("host")
    String host;
    ClusterType clusterType;
    boolean secured;

    public Cluster() {
    }

    public Cluster(String name, String host, ClusterType clusterType) {
        this.name = name;
        this.host = host;
        this.clusterType = clusterType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ClusterType getClusterType() {
        return clusterType;
    }

    public void setClusterType(ClusterType clusterType) {
        this.clusterType = clusterType;
    }

    public boolean isSecured() {
        return secured;
    }

    public void setSecured(boolean secured) {
        this.secured = secured;
    }
}
