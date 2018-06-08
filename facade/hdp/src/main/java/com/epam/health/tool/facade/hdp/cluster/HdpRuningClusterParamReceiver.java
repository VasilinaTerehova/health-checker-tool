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

package com.epam.health.tool.facade.hdp.cluster;

import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.common.cluster.receiver.CommonRuningClusterParamReceiver;
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.health.tool.facade.context.IApplicationContext;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
@Component
@Qualifier("HDP-cluster")
@ClusterSpecificComponent( ClusterTypeEnum.HDP )
public class HdpRuningClusterParamReceiver extends CommonRuningClusterParamReceiver {
    private static final Logger log = LoggerFactory.getLogger( CommonRuningClusterParamReceiver.class );

    @Autowired
    public HdpRuningClusterParamReceiver(HttpAuthenticationClient httpAuthenticationClient, ClusterDao clusterDao, IApplicationContext applicationContext) {
        super(httpAuthenticationClient, clusterDao, applicationContext);
    }

    @Override
    public String getPropertySiteXml(ClusterEntity clusterEntity, String siteName, String propertyName) throws InvalidResponseException {
        //try {
        String clusterName = clusterEntity.getClusterName();
        String siteType = siteName.replace(".xml", "");
        String urlConfiguration = "http://" + clusterEntity.getHost() + ":8080/api/v1/clusters/" + clusterName + "/configurations?type=" + siteType + "&tag=version1";
        System.out.println(urlConfiguration);
        try {
            return CommonJsonHandler.get().getTypedValueFromInnerField(
                    httpAuthenticationClient.makeAuthenticatedRequest(clusterName, urlConfiguration, false), String.class, "items", "properties", propertyName);
        } catch ( CommonUtilException | AuthenticationRequestException e ) {
            throw new InvalidResponseException(e);
        }
    }

    @Override
    public Set<String> getLiveNodes(String clusterName) throws InvalidResponseException {
        return super.getLiveNodes(clusterName).stream().map( this::removePort ).collect( Collectors.toSet() );
    }

    @Override
    protected Logger log() {
        return log;
    }

    private String removePort( String node ) {
        return  node.split( ":" )[0];
    }
}
