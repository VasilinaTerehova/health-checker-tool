package com.epam.facade.model.accumulator;

/**
 * Created by Vasilina_Terehova on 4/27/2018.
 */
public class LogLocation {
    private String logPath;
    private String clusterNode;

    public LogLocation() {
    }

    public LogLocation(String clusterNode, String logPath) {
        this.logPath = logPath;
        this.clusterNode = clusterNode;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getClusterNode() {
        return clusterNode;
    }

    public void setClusterNode(String clusterNode) {
        this.clusterNode = clusterNode;
    }
}
