package com.epam.facade.model;

import java.util.Set;

public class ClusterNodes {
    private Set<String> liveNodes;
    private String clusterName;

    public ClusterNodes(Set<String> liveNodes, String clusterName) {
        this.liveNodes = liveNodes;
        this.clusterName = clusterName;
    }

    public Set<String> getLiveNodes() {
        return liveNodes;
    }

    public void setLiveNodes(Set<String> liveNodes) {
        this.liveNodes = liveNodes;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
