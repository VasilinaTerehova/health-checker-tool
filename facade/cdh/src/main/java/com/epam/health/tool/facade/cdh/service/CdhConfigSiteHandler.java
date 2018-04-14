package com.epam.health.tool.facade.cdh.service;

import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.facade.model.service.RoleJson;
import com.epam.facade.model.service.YarnRoleEnum;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.file.FileCommonUtil;
import com.epam.util.common.json.CommonJsonHandler;
import com.epam.util.common.xml.XmlPropertyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class CdhConfigSiteHandler {
    @Autowired
    private HttpAuthenticationClient httpAuthenticationClient;

    public String getPropertySiteXml(ClusterEntity clusterEntity, String siteName, String propertyName) throws InvalidResponseException {
        String serviceFileName = getServiceFileName( clusterEntity.getClusterName(), siteName );
        if ( !isFileExist( clusterEntity.getClusterName(), siteName ) ) {
            String siteFileUrl = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterEntity.getClusterName()
                    + "/services/yarn/roles/" + findNodeManagerRole( clusterEntity ) + "/process/configFiles/" + siteName;
            downloadSiteFile( clusterEntity, siteFileUrl, serviceFileName );
        }

        //get xml tag
        return XmlPropertyHandler.readXmlPropertyValue(serviceFileName, propertyName);
    }

    private String findNodeManagerRole( ClusterEntity clusterEntity ) throws InvalidResponseException {
        try {
            String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterEntity.getClusterName() + "/services/yarn/roles";
            String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterEntity.getClusterName(), url, false);
            System.out.println(answer);
            List<RoleJson> yarnRoles = CommonJsonHandler.get().getListTypedValueFromInnerField(answer, RoleJson.class, "items");
            System.out.println(yarnRoles);
            return yarnRoles.stream().filter(roleJson -> roleJson.getType().equals(YarnRoleEnum.NODEMANAGER)).findAny()
                    .orElseThrow( () -> new InvalidResponseException( "Can't find NodeManager role on cluster - " + clusterEntity.getClusterName() ) ).getName();
        }
        catch ( CommonUtilException ex ) {
            throw new InvalidResponseException( ex );
        }
    }

    private void downloadSiteFile( ClusterEntity clusterEntity, String sourceUrl, String dest ) throws InvalidResponseException {
        try {
            String xmlContent = httpAuthenticationClient.makeAuthenticatedRequest( clusterEntity.getClusterName(), sourceUrl, false);
            System.out.println(xmlContent);
            FileCommonUtil.writeStringToFile( dest, xmlContent );
        }
        catch ( CommonUtilException ex ) {
            throw new InvalidResponseException( ex );
        }
    }

    private boolean isFileExist(String clusterName, String serviceFileName) {
        String dest = getServiceFileName(clusterName, serviceFileName);
        return new File(dest).exists();
    }

    private String getServiceFileName(String clusterName, String serviceFileName) {
        return "clusters/" + clusterName + "/" + serviceFileName;
    }
}
