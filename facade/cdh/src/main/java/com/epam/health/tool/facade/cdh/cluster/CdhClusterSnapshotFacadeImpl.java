package com.epam.health.tool.facade.cdh.cluster;

import com.epam.facade.model.ServiceStatus;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.authentication.http.BaseHttpAuthenticatedAction;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.common.cluster.CommonClusterSnapshotFacadeImpl;
import com.epam.health.tool.facade.common.service.DownloadableFileConstants;
import com.epam.health.tool.facade.common.service.RoleJson;
import com.epam.health.tool.facade.common.service.YarnRoleEnum;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.file.FileCommonUtil;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import com.epam.util.common.xml.XmlPropertyHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.epam.health.tool.facade.common.service.DownloadableFileConstants.YarnProperties.YARN_NODEMANAGER_LOG_DIRS;
import static com.epam.health.tool.facade.common.service.DownloadableFileConstants.YarnProperties.YARN_RESOURCEMANAGER_WEBAPP_ADDRESS;

@Component("CDH-cluster")
public class CdhClusterSnapshotFacadeImpl extends CommonClusterSnapshotFacadeImpl {

    @Autowired
    private HttpAuthenticationClient httpAuthenticationClient;

    @Override
    public List<ServiceStatus> askForCurrentServicesSnapshot(String clusterName) throws InvalidResponseException {
        ClusterEntityProjection clusterEntity = clusterFacade.getCluster(clusterName);
        String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterName + "/services";

        String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterName, url );

        return extractFromJsonString(answer);
    }

    private List<ServiceStatus> extractFromJsonString(String jsonString) throws InvalidResponseException {
        try {
            return CommonJsonHandler.get().getListTypedValueFromInnerField(jsonString, ServiceStatus.class, "items");
        } catch (CommonUtilException e) {
            throw new InvalidResponseException("Can't extract application list from answer - " + jsonString, e);
        }
    }

    public String getActiveResourceManagerAddress(String clusterName) throws CommonUtilException {
        String rmAddress = getPropertySiteXml(clusterName, DownloadableFileConstants.ServiceFileName.YARN, YARN_RESOURCEMANAGER_WEBAPP_ADDRESS);

        if (rmAddress == null) {
            //possibly ha mode for rm
            String haIds = getPropertySiteXml(clusterName, DownloadableFileConstants.ServiceFileName.YARN, "yarn.resourcemanager.ha.rm-ids");
            //Optional.of(haIds).ifPresent(s -> Optional.of(s.split(",")).ifPresent(strings -> ));
            String[] split = haIds.split(",");
            if (split.length > 0) {
                String rmId = split[0];
                rmAddress = getPropertySiteXml(clusterName, DownloadableFileConstants.ServiceFileName.YARN, YARN_RESOURCEMANAGER_WEBAPP_ADDRESS + "." + rmId);
            }
        }

        System.out.println("rm address: " + rmAddress);
        return rmAddress;
    }

    public String getLogDirectory(String clusterName) throws CommonUtilException {
        String logDirPropery = getPropertySiteXml(clusterName, DownloadableFileConstants.ServiceFileName.YARN, YARN_NODEMANAGER_LOG_DIRS);

        System.out.println("log.dir: " + logDirPropery);
        return logDirPropery;
    }

    public String getPropertySiteXml(String clusterName, String siteName, String propertyName) throws CommonUtilException {
        String serviceFileName = getServiceFileName(clusterName, siteName);
        if (!isFileExist(clusterName, siteName)) {

            ClusterEntityProjection clusterEntity = clusterFacade.getCluster(clusterName);
            String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterName + "/services/yarn/roles";
            String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterName, url);
            System.out.println(answer);
            List<RoleJson> yarnRoles = CommonJsonHandler.get().getListTypedValueFromInnerField(answer, RoleJson.class, "items");
            System.out.println(yarnRoles);
            RoleJson roleJson1 = yarnRoles.stream().filter(roleJson -> roleJson.getType().equals(YarnRoleEnum.NODEMANAGER)).findAny().get();

            String url2 = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterName + "/services/yarn/roles/" + roleJson1.getName() + "/process/configFiles/" + siteName;
            String xmlContent = BaseHttpAuthenticatedAction.get()
                    .withUsername(clusterEntity.getHttp().getUsername())
                    .withPassword(clusterEntity.getHttp().getPassword())
                    .makeAuthenticatedRequest(url2);
            System.out.println(xmlContent);
            FileCommonUtil.writeStringToFile(serviceFileName, xmlContent);
        }

        //get xml tag
        return XmlPropertyHandler.readXmlPropertyValue(serviceFileName, propertyName);
    }

    private boolean isFileExist(String clusterName, String serviceFileName) {
        String dest = getServiceFileName(clusterName, serviceFileName);
        return new File(dest).exists();
    }

    private String getServiceFileName(String clusterName, String serviceFileName) {
        return "clusters/" + clusterName + "/" + serviceFileName;
    }

}
