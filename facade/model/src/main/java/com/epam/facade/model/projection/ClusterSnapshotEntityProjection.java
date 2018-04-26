package com.epam.facade.model.projection;

import com.epam.health.tool.model.ClusterTypeEnum;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

public interface ClusterSnapshotEntityProjection {

    @Value("#{target.id}")
    Long getId();

    @Value("#{target.clusterEntity.clusterName}")
    String getName();

    @Value("#{target.clusterEntity.clusterTypeEnum}")
    ClusterTypeEnum getClusterType();

    @Value("#{target.clusterEntity.host}")
    String getHost();

    @Value("#{target.clusterEntity.secured}")
    boolean isSecured();

    @Value("#{target.dateOfSnapshot}")
    Date getDateOfSnapshot();

    @Value("#{target.clusterServiceSnapshotEntityList}")
    List<ServiceStatusHolder> getClusterServiceSnapshotEntityList();

    @Value("#{target.memoryUsageEntity}")
    MemoryUsageEntityProjection getMemoryUsage();

    @Value("#{target.hdfsUsageEntity}")
    HdfsUsageEntityProjection getHdfsUsage();

    @Value("#{target.nodeSnapshotEntities}")
    List<? extends NodeSnapshotEntityProjection> getNodes();

}
