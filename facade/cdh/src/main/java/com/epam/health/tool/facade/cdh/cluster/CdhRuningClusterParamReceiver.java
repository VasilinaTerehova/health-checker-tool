/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.health.tool.facade.cdh.cluster;

import com.epam.facade.model.service.RoleJson;
import com.epam.facade.model.service.YarnRoleEnum;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.common.cluster.receiver.CommonRuningClusterParamReceiver;
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.health.tool.facade.context.IApplicationContext;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.util.common.CheckingParamsUtil;
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
    private static final String DEFAULT_YARN_LOG_DIR = "/yarn/containers-log";
    private static final String DEFAULT_YARN_LOCAL_DIR = "/yarn/nm";

    @Autowired
    public CdhRuningClusterParamReceiver(HttpAuthenticationClient httpAuthenticationClient, ClusterDao clusterDao, IApplicationContext applicationContext) {
        super(httpAuthenticationClient, clusterDao, applicationContext);
    }

    @Override
    public String getYarnLocalDirectory(String clusterName) throws InvalidResponseException {
        String localDir = super.getYarnLocalDirectory( clusterName );

        return CheckingParamsUtil.isParamsNotNullOrEmpty( localDir ) ? localDir : DEFAULT_YARN_LOCAL_DIR;
    }

    @Override
    public String getLogDirectory(String clusterName) throws InvalidResponseException {
        String logDir = super.getLogDirectory(clusterName);

        return CheckingParamsUtil.isParamsNotNullOrEmpty( logDir ) ? logDir : DEFAULT_YARN_LOG_DIR;
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
