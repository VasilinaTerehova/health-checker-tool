package com.epam.facade.model.accumulator.results.impl;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.results.BaseActionResult;
import com.epam.facade.model.projection.HdfsUsageEntityProjection;
import com.epam.facade.model.projection.MemoryUsageEntityProjection;
import com.epam.facade.model.projection.NodeSnapshotEntityProjection;

import java.util.List;

public class FsHealthCheckResult implements BaseActionResult {
    //Fs check result
    private MemoryUsageEntityProjection memoryUsageEntityProjection;
    private HdfsUsageEntityProjection hdfsUsageEntityProjection;
    private List<? extends NodeSnapshotEntityProjection> nodeSnapshotEntityProjections;

    public MemoryUsageEntityProjection getMemoryUsageEntityProjection() {
        return memoryUsageEntityProjection;
    }

    public void setMemoryUsageEntityProjection(MemoryUsageEntityProjection memoryUsageEntityProjection) {
        this.memoryUsageEntityProjection = memoryUsageEntityProjection;
    }

    public HdfsUsageEntityProjection getHdfsUsageEntityProjection() {
        return hdfsUsageEntityProjection;
    }

    public void setHdfsUsageEntityProjection(HdfsUsageEntityProjection hdfsUsageEntityProjection) {
        this.hdfsUsageEntityProjection = hdfsUsageEntityProjection;
    }

    public List<? extends NodeSnapshotEntityProjection> getNodeSnapshotEntityProjections() {
        return nodeSnapshotEntityProjections;
    }

    public void setNodeSnapshotEntityProjections(List<? extends NodeSnapshotEntityProjection> nodeSnapshotEntityProjections) {
        this.nodeSnapshotEntityProjections = nodeSnapshotEntityProjections;
    }

    @Override
    public HealthCheckActionType getHealthActionType() {
        return HealthCheckActionType.FS;
    }
}
