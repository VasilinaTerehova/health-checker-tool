package com.epam.health.tool.facade.cluster;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.projection.HdfsUsageEntityProjection;
import com.epam.facade.model.projection.MemoryUsageEntityProjection;
import com.epam.facade.model.projection.NodeSnapshotEntityProjection;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.accumulator.YarnHealthCheckResult;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;
import com.epam.util.common.CommonUtilException;

import java.util.List;

public interface IClusterSnapshotFacade {
    ClusterHealthSummary getLastClusterSnapshot(String clusterName );
    List<ClusterHealthSummary> getClusterSnapshotHistory(String clusterName ) throws InvalidResponseException;
    HealthCheckResultsAccumulator askForCurrentClusterSnapshot(String clusterName ) throws InvalidResponseException;
    default YarnHealthCheckResult askForCurrentYarnHealthCheck(String clusterName) throws InvalidResponseException {
        return null;
    }
    default HealthCheckResultsAccumulator askForCurrentFullHealthCheck(String clusterName ) throws InvalidResponseException {
        return null;
    }
    List<ServiceStatus> askForCurrentServicesSnapshot(String clusterName ) throws InvalidResponseException;
    ClusterShapshotEntity receiveAndSaveClusterSnapshot(ClusterEntity clusterEntity);

    String getActiveResourceManagerAddress(String clusterName) throws CommonUtilException;

    String getLogDirectory(String clusterName) throws CommonUtilException;

    String getPropertySiteXml(String clusterName, String siteName, String propertyName) throws CommonUtilException;

    HdfsUsageEntityProjection getAvailableDiskHdfs(String clusterName) throws InvalidResponseException;

    MemoryUsageEntityProjection getMemoryTotal(String clusterName) throws InvalidResponseException;

    List<? extends NodeSnapshotEntityProjection> getAvailableDiskDfs(String cdh512Unsecure);

}
