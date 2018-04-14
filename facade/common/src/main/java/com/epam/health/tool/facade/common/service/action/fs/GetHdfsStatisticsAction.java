package com.epam.health.tool.facade.common.service.action.fs;

import com.epam.facade.model.ClusterHealthSummary;
import com.epam.facade.model.ClusterSnapshotEntityProjectionImpl;
import com.epam.facade.model.accumulator.HealthCheckResultsAccumulator;
import com.epam.facade.model.projection.HdfsUsageEntityProjection;
import com.epam.facade.model.service.DownloadableFileConstants;
import com.epam.facade.model.fs.HdfsNamenodeJson;
import com.epam.health.tool.facade.common.service.action.CommonRestHealthCheckAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;

import static com.epam.facade.model.service.DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTP_ADDRESS;

/**
 * Created by Vasilina_Terehova on 4/9/2018.
 */
public abstract class GetHdfsStatisticsAction extends CommonRestHealthCheckAction {
    @Override
    protected ClusterHealthSummary performRestHealthCheck( ClusterEntity clusterEntity ) throws InvalidResponseException {
        return new ClusterHealthSummary(
                new ClusterSnapshotEntityProjectionImpl( null, null,
                        null, getAvailableDiskHdfs( clusterEntity ), null));
    }

    @Override
    protected void saveClusterHealthSummaryToAccumulator(HealthCheckResultsAccumulator healthCheckResultsAccumulator, ClusterHealthSummary clusterHealthSummary) {
        ClusterHealthSummary tempClusterHealthSummary = healthCheckResultsAccumulator.getClusterHealthSummary();

        if ( tempClusterHealthSummary == null ) {
            tempClusterHealthSummary = clusterHealthSummary;
        }
        else {
            tempClusterHealthSummary = new ClusterHealthSummary(
                    new ClusterSnapshotEntityProjectionImpl( recreateClusterEntityProjection( tempClusterHealthSummary.getCluster() ),
                            tempClusterHealthSummary.getServiceStatusList(), tempClusterHealthSummary.getCluster().getMemoryUsage(),
                            clusterHealthSummary.getCluster().getHdfsUsage(), tempClusterHealthSummary.getCluster().getNodes()) );
        }

        healthCheckResultsAccumulator.setClusterHealthSummary( tempClusterHealthSummary );
    }

    protected abstract String getHANameNodeUrl( ClusterEntity clusterEntity ) throws InvalidResponseException;

    private String getNameNodeUrl( ClusterEntity clusterEntity ) throws InvalidResponseException {
        String nameNodeUrl = getPropertySiteXml( clusterEntity, DownloadableFileConstants.ServiceFileName.HDFS, DFS_NAMENODE_HTTP_ADDRESS);

        if ( CheckingParamsUtil.isParamsNullOrEmpty( nameNodeUrl ) ) {
            return getHANameNodeUrl( clusterEntity );
        }

        return nameNodeUrl;
    }

    private HdfsUsageEntityProjection getAvailableDiskHdfs( ClusterEntity clusterEntity ) throws InvalidResponseException {
        try {
            String nameNodeUrl = getNameNodeUrl( clusterEntity );
            String url = "http://" + nameNodeUrl + "/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo";

            System.out.println(url);
            String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterEntity.getClusterName(), url );
            System.out.println(answer);
            HdfsNamenodeJson hdfsUsageJson = CommonJsonHandler.get().getTypedValueFromInnerFieldArrElement(answer, HdfsNamenodeJson.class, "beans");
            System.out.println(hdfsUsageJson);

            return hdfsUsageJson;
        } catch (CommonUtilException ex) {
            throw new InvalidResponseException("Elements not found.", ex);
        }
    }
}
