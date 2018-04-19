package com.epam.health.tool.connector;

public class HealthCheckResult {
    private boolean clusterHealthy;
    private String errorSummary;

    public boolean isClusterHealthy() {
        return clusterHealthy;
    }

    public void setClusterHealthy(boolean clusterHealthy) {
        this.clusterHealthy = clusterHealthy;
    }

    public String getErrorSummary() {
        return errorSummary;
    }

    public void setErrorSummary(String errorSummary) {
        this.errorSummary = errorSummary;
    }
}
