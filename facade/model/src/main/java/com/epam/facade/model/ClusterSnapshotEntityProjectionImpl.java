package com.epam.facade.model;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.facade.model.projection.ServiceStatusProjection;
import com.epam.health.tool.model.ClusterTypeEnum;

import java.util.Date;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/30/2018.
 */
public class ClusterSnapshotEntityProjectionImpl implements ClusterSnapshotEntityProjection {
    private final List<? extends ServiceStatusProjection> serviceStatusProjectionList;
    private final ClusterEntityProjection clusterEntityProjection;

    public ClusterSnapshotEntityProjectionImpl(ClusterEntityProjection clusterEntityProjection, List<? extends ServiceStatusProjection> serviceStatusProjectionList) {
        this.clusterEntityProjection = clusterEntityProjection;
        this.serviceStatusProjectionList = serviceStatusProjectionList;
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
}
