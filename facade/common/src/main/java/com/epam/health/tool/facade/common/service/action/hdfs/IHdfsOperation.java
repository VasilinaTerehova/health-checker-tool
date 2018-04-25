package com.epam.health.tool.facade.common.service.action.hdfs;

import com.epam.facade.model.projection.JobResultProjection;
import com.epam.health.tool.model.ClusterEntity;

public interface IHdfsOperation {
    JobResultProjection perform(ClusterEntity clusterEntity);
}
