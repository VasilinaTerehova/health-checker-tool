package com.epam.health.tool.facade.common.service.action.hdfs;

import com.epam.facade.model.accumulator.results.impl.HdfsHealthCheckResult;
import com.epam.health.tool.model.ClusterEntity;

public interface IHdfsOperation {
    HdfsHealthCheckResult.HdfsOperationResult perform( ClusterEntity clusterEntity );
}
