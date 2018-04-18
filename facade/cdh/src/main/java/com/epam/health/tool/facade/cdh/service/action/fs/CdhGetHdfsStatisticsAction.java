package com.epam.health.tool.facade.cdh.service.action.fs;

import com.epam.facade.model.service.DownloadableFileConstants;
import com.epam.health.tool.facade.common.resolver.impl.action.HealthCheckAction;
import com.epam.facade.model.HealthCheckActionType;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.service.action.fs.GetHdfsStatisticsAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.epam.facade.model.service.DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTP_ADDRESS;

@Component("CDH-hdfs-statistics")
@HealthCheckAction( HealthCheckActionType.FS )
public class CdhGetHdfsStatisticsAction extends GetHdfsStatisticsAction {
    @Autowired
    @Qualifier("CDH-cluster")
    private IRunningClusterParamReceiver iRunningClusterParamReceiver;

    @Override
    public String getHANameNodeUrl(String clusterName) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName(clusterName);
        String haIds = iRunningClusterParamReceiver.getPropertySiteXml(clusterEntity, DownloadableFileConstants.ServiceFileName.HDFS, "dfs.ha.namenodes." + clusterEntity.getClusterName());
        if (!CheckingParamsUtil.isParamsNullOrEmpty(haIds)) {
            return iRunningClusterParamReceiver.getPropertySiteXml(clusterEntity, DownloadableFileConstants.ServiceFileName.HDFS,
                    DFS_NAMENODE_HTTP_ADDRESS + "." + clusterEntity.getClusterName() + "." + Arrays.stream(haIds.split(",")).findFirst()
                            .orElseThrow(() -> new InvalidResponseException("Can't find name node url for cluster - " + clusterEntity.getClusterName())));
        }

        throw new InvalidResponseException("Can't find name node url for cluster - " + clusterEntity.getClusterName());
    }
}
