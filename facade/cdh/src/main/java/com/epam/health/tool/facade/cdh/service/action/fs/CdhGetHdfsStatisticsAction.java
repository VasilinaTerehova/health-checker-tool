package com.epam.health.tool.facade.cdh.service.action.fs;

import com.epam.health.tool.facade.cdh.service.CdhConfigSiteHandler;
import com.epam.health.tool.facade.common.service.DownloadableFileConstants;
import com.epam.health.tool.facade.common.service.action.fs.GetHdfsStatisticsAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.epam.health.tool.facade.common.service.DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTP_ADDRESS;

@Component("CDH-hdfs-statistics")
public class CdhGetHdfsStatisticsAction extends GetHdfsStatisticsAction {
    @Autowired
    private CdhConfigSiteHandler cdhConfigSiteHandler;

    @Override
    protected String getPropertySiteXml(ClusterEntity clusterEntity, String siteName, String propertyName) throws InvalidResponseException {
        return cdhConfigSiteHandler.getPropertySiteXml( clusterEntity, siteName, propertyName );
    }

    @Override
    protected String getHANameNodeUrl(ClusterEntity clusterEntity) throws InvalidResponseException {
        String haIds = getPropertySiteXml( clusterEntity, DownloadableFileConstants.ServiceFileName.HDFS, "dfs.ha.namenodes." + clusterEntity.getClusterName() );
        if ( !CheckingParamsUtil.isParamsNullOrEmpty( haIds ) ) {
            return getPropertySiteXml( clusterEntity, DownloadableFileConstants.ServiceFileName.HDFS,
                    DFS_NAMENODE_HTTP_ADDRESS + "." + clusterEntity.getClusterName() + "." + Arrays.stream( haIds.split( "," ) ).findFirst()
                            .orElseThrow(() -> new InvalidResponseException( "Can't find name node url for cluster - " + clusterEntity.getClusterName() )));
        }

        throw new InvalidResponseException( "Can't find name node url for cluster - " + clusterEntity.getClusterName() );
    }
}
