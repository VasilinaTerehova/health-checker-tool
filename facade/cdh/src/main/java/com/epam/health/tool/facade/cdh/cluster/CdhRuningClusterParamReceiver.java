package com.epam.health.tool.facade.cdh.cluster;

import com.epam.facade.model.service.RoleJson;
import com.epam.facade.model.service.YarnRoleEnum;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.common.cluster.receiver.CommonRuningClusterParamReceiver;
import com.epam.health.tool.facade.common.resolver.impl.ClusterSpecificComponent;
import com.epam.health.tool.facade.context.IApplicationContext;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.file.FileCommonUtil;
import com.epam.util.common.json.CommonJsonHandler;
import com.epam.util.common.xml.XmlPropertyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
@Component
@Qualifier("CDH-cluster")
@ClusterSpecificComponent( ClusterTypeEnum.CDH )
public class CdhRuningClusterParamReceiver extends CommonRuningClusterParamReceiver {
    private static final Logger log = LoggerFactory.getLogger( CdhRuningClusterParamReceiver.class );

    @Autowired
    public CdhRuningClusterParamReceiver(HttpAuthenticationClient httpAuthenticationClient, ClusterDao clusterDao, IApplicationContext applicationContext) {
        super(httpAuthenticationClient, clusterDao, applicationContext);
    }

    public String getPropertySiteXml(ClusterEntity clusterEntity, String siteName, String propertyName ) throws InvalidResponseException {
        String serviceFileName = getServiceFileName( clusterEntity.getClusterName(), siteName );

        if ( !isFileExist( clusterEntity.getClusterName(), siteName ) ) {
            String siteFileUrl = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterEntity.getClusterName()
                    + "/services/yarn/roles/" + findNodeManagerRole( clusterEntity ) + "/process/configFiles/" + siteName;
            downloadSiteFile( clusterEntity, siteFileUrl, serviceFileName );
        }

        //get xml tag
        return XmlPropertyHandler.readXmlPropertyValue(serviceFileName, propertyName);
    }

    @Override
    protected Logger log() {
        return log;
    }

    private String findNodeManagerRole(ClusterEntity clusterEntity ) throws InvalidResponseException {
        try {
            String url = "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterEntity.getClusterName() + "/services/yarn/roles";
            String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterEntity.getClusterName(), url, false);
            System.out.println(answer);
            List<RoleJson> yarnRoles = CommonJsonHandler.get().getListTypedValueFromInnerField(answer, RoleJson.class, "items");
            System.out.println(yarnRoles);
            return yarnRoles.stream().filter(roleJson -> roleJson.getType().equals(YarnRoleEnum.NODEMANAGER)).findAny()
                    .orElseThrow( () -> new InvalidResponseException( "Can't find NodeManager role on cluster - " + clusterEntity.getClusterName() ) ).getName();
        }
        catch ( CommonUtilException | AuthenticationRequestException ex ) {
            throw new InvalidResponseException( ex );
        }
    }

    private void downloadSiteFile( ClusterEntity clusterEntity, String sourceUrl, String dest ) throws InvalidResponseException {
        try {
            String xmlContent = httpAuthenticationClient.makeAuthenticatedRequest( clusterEntity.getClusterName(), sourceUrl, false);
            System.out.println(xmlContent);
            FileCommonUtil.writeStringToFile( dest, xmlContent );
        }
        catch ( CommonUtilException | AuthenticationRequestException ex ) {
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
