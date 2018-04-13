package com.epam.health.tool.facade.cdh.service.action.fs;

import com.epam.health.tool.facade.cdh.service.CdhConfigSiteHandler;
import com.epam.health.tool.facade.common.service.DownloadableFileConstants;
import com.epam.health.tool.facade.common.service.action.fs.GetMemoryStatisticsAction;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.health.tool.facade.common.service.DownloadableFileConstants.YarnProperties.YARN_RESOURCEMANAGER_WEBAPP_ADDRESS;

@Component("CDH-memory")
public class CdhGetMemoryStatisticsActions extends GetMemoryStatisticsAction {
    @Autowired
    private CdhConfigSiteHandler cdhConfigSiteHandler;

    @Override
    protected String getActiveResourceManagerAddress(ClusterEntity clusterEntity) throws InvalidResponseException {
        String rmAddress = getPropertySiteXml( clusterEntity, DownloadableFileConstants.ServiceFileName.YARN, YARN_RESOURCEMANAGER_WEBAPP_ADDRESS);

        if ( CheckingParamsUtil.isParamsNullOrEmpty(  rmAddress ) ) {
            //possibly ha mode for rm
            String haIds = getPropertySiteXml( clusterEntity, DownloadableFileConstants.ServiceFileName.YARN, "yarn.resourcemanager.ha.rm-ids");
            //Optional.of(haIds).ifPresent(s -> Optional.of(s.split(",")).ifPresent(strings -> ));
            String[] split = haIds.split(",");
            if (split.length > 0) {
                String rmId = split[0];
                rmAddress = getPropertySiteXml( clusterEntity, DownloadableFileConstants.ServiceFileName.YARN, YARN_RESOURCEMANAGER_WEBAPP_ADDRESS + "." + rmId);
            }
        }

        System.out.println("rm address: " + rmAddress);
        return rmAddress;
    }

    @Override
    protected String getPropertySiteXml(ClusterEntity clusterEntity, String siteName, String propertyName) throws InvalidResponseException {
        return cdhConfigSiteHandler.getPropertySiteXml( clusterEntity, siteName, propertyName );
    }
}
