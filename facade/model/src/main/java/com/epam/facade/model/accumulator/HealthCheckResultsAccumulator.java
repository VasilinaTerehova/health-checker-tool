package com.epam.facade.model.accumulator;

import com.epam.facade.model.ClusterHealthSummary;

public class HealthCheckResultsAccumulator {
    private YarnHealthCheckResult yarnHealthCheckResult;
    private ClusterHealthSummary clusterHealthSummary;

    public YarnHealthCheckResult getYarnHealthCheckResult() {
        return yarnHealthCheckResult;
    }

    public void setYarnHealthCheckResult(YarnHealthCheckResult yarnHealthCheckResult) {
        this.yarnHealthCheckResult = yarnHealthCheckResult;
    }

    public ClusterHealthSummary getClusterHealthSummary() {
        return clusterHealthSummary;
    }

    public void setClusterHealthSummary(ClusterHealthSummary clusterHealthSummary) {
        this.clusterHealthSummary = clusterHealthSummary;
    }
}
