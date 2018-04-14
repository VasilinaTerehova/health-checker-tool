package com.epam.health.tool.facade.cdh.service.action.fs;

import com.epam.health.tool.facade.cdh.service.CdhConfigSiteHandler;
import com.epam.facade.model.service.DownloadableFileConstants;
import com.epam.health.tool.facade.common.service.action.fs.GetFsStatisticsAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.facade.model.service.DownloadableFileConstants.YarnProperties.YARN_NODEMANAGER_LOG_DIRS;

@Component( "CDH-fs-statistics" )
public class CdhGetFsStatisticsAction extends GetFsStatisticsAction {
    @Autowired
    private CdhConfigSiteHandler cdhConfigSiteHandler;

    @Override
    protected String getLogDirectory( ClusterEntity clusterEntity ) throws InvalidResponseException {
        String logDirPropery = getPropertySiteXml( clusterEntity, DownloadableFileConstants.ServiceFileName.YARN, YARN_NODEMANAGER_LOG_DIRS );

        System.out.println("log.dir: " + logDirPropery);
        return logDirPropery;
    }

    @Override
    protected String getPropertySiteXml(ClusterEntity clusterEntity, String siteName, String propertyName) throws InvalidResponseException {
        return cdhConfigSiteHandler.getPropertySiteXml( clusterEntity, siteName, propertyName );
    }
}
