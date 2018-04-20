package com.epam.facade.model;

import com.epam.facade.model.projection.*;
import com.epam.health.tool.model.ClusterTypeEnum;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Vasilina_Terehova on 3/30/2018.
 */
public class ClusterSnapshotEntityProjectionImpl implements ClusterSnapshotEntityProjection {
    private final List<? extends ServiceStatusProjection> serviceStatusProjectionList;
    private final ClusterEntityProjection clusterEntityProjection;
    private final MemoryUsageEntityProjection memoryUsageEntityProjection;
    private final HdfsUsageEntityProjection hdfsUsageEntityProjection;
    private final List<? extends NodeSnapshotEntityProjection> nodeSnapshotEntityProjectionList;

    public ClusterSnapshotEntityProjectionImpl(ClusterEntityProjection clusterEntityProjection, List<? extends ServiceStatusProjection> serviceStatusProjectionList,
                                               MemoryUsageEntityProjection memoryUsageEntityProjection, HdfsUsageEntityProjection hdfsUsageEntityProjection,
                                               List<? extends NodeSnapshotEntityProjection> nodeSnapshotEntityProjectionList) {
        this.clusterEntityProjection = clusterEntityProjection;
        this.serviceStatusProjectionList = serviceStatusProjectionList;
        this.memoryUsageEntityProjection = memoryUsageEntityProjection;
        this.hdfsUsageEntityProjection = hdfsUsageEntityProjection;
        this.nodeSnapshotEntityProjectionList = nodeSnapshotEntityProjectionList;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getName() {
        return clusterEntityProjection.getName();
    }

    @Override
    public ClusterTypeEnum getClusterType() {
        return clusterEntityProjection.getClusterType();
    }

    @Override
    public String getHost() {
        return clusterEntityProjection.getHost();
    }

    @Override
    public boolean isSecured() {
        return clusterEntityProjection.isSecured();
    }

    @Override
    public Date getDateOfSnapshot() {
        return new Date();
    }

    @Override
    public List<? extends ServiceStatusProjection> getClusterServiceShapshotEntityList() {
        return serviceStatusProjectionList;
    }

    @Override
    public MemoryUsageEntityProjection getMemoryUsage() {
        return memoryUsageEntityProjection;
    }

    @Override
    public HdfsUsageEntityProjection getHdfsUsage() {
        return hdfsUsageEntityProjection;
    }

    @Override
    public List<? extends NodeSnapshotEntityProjection> getNodes() {
        return nodeSnapshotEntityProjectionList;
    }

}
