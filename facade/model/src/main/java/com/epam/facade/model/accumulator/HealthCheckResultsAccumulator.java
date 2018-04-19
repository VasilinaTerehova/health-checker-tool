package com.epam.facade.model.accumulator;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.projection.ServiceStatusProjection;

import java.util.HashSet;
import java.util.Set;

public class HealthCheckResultsAccumulator {
    Set<HealthCheckActionType> passedActionTypes = new HashSet<>();
    private YarnHealthCheckResult yarnHealthCheckResult;
    private ClusterHealthSummary clusterHealthSummary;
    private HdfsHealthCheckResult hdfsHealthCheckResult;

    public YarnHealthCheckResult getYarnHealthCheckResult() {
        return yarnHealthCheckResult;
    }

    public void setYarnHealthCheckResult(YarnHealthCheckResult yarnHealthCheckResult) {
        this.yarnHealthCheckResult = yarnHealthCheckResult;
        passedActionTypes.add(yarnHealthCheckResult.getHealthActionType());
    }

    public ClusterHealthSummary getClusterHealthSummary() {
        return clusterHealthSummary;
    }

    public void setClusterHealthSummary(ClusterHealthSummary clusterHealthSummary) {
        this.clusterHealthSummary = clusterHealthSummary;
        passedActionTypes.addAll(clusterHealthSummary.getPassedActionTypes());
    }

    public HdfsHealthCheckResult getHdfsHealthCheckResult() {
        return hdfsHealthCheckResult;
    }

    public void setHdfsHealthCheckResult(HdfsHealthCheckResult hdfsHealthCheckResult) {
        this.hdfsHealthCheckResult = hdfsHealthCheckResult;
        passedActionTypes.add(hdfsHealthCheckResult.getHealthActionType());
    }

    public Set<HealthCheckActionType> getPassedActionTypes() {
        return passedActionTypes;
    }

    public boolean areAllChecksPassed() {
        return HealthCheckActionType.containAllActionTypes(passedActionTypes);
    }
}
