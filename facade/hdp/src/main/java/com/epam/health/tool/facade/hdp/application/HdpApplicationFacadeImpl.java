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

package com.epam.health.tool.facade.hdp.application;

import com.epam.facade.model.ApplicationInfo;
import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.common.application.CommonApplicationFacade;
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.health.tool.transfer.impl.SVTransfererManager;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ClusterSpecificComponent( ClusterTypeEnum.HDP )
public class HdpApplicationFacadeImpl extends CommonApplicationFacade {
  @Autowired
  private IClusterFacade clusterFacade;
  @Autowired
  private SVTransfererManager svTransfererManager;
  @Autowired
  private HttpAuthenticationClient httpAuthenticationClient;
  @Autowired
  private SshAuthenticationClient sshAuthenticationClient;

  public List<ApplicationInfo> getApplicationList( String clusterName ) throws InvalidResponseException {
    ClusterEntityProjection clusterEntity = clusterFacade.getCluster( clusterName );

    try {
      String url = "http://" + getYarnRestUrl( clusterEntity ).trim() + "/ws/v1/cluster/apps?limit=20";
      String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterName, url );
      List<ApplicationInfoDTO> appList =
        CommonJsonHandler.get().getListTypedValueFromInnerField( answer, ApplicationInfoDTO.class, "apps", "app" );

      if ( appList != null ) {
        return mapDTOInfoToApplicationInfo( appList );
      } else {
        throw new InvalidResponseException( "Elements not found. Answer string - " + answer );
      }
    } catch ( CommonUtilException | AuthenticationRequestException ex ) {
      throw new InvalidResponseException( "Elements not found. Reason - " + ex.getMessage(), ex );
    }
  }

/*    public void killApplication(String clusterName, String appId) {

    }*/

  private List<ApplicationInfo> mapDTOInfoToApplicationInfo( List<ApplicationInfoDTO> applicationInfoDTOs ) {
    return applicationInfoDTOs.stream()
      .map( applicationInfoDTO -> svTransfererManager.<ApplicationInfoDTO, ApplicationInfo>getTransferer(
        ApplicationInfoDTO.class, ApplicationInfo.class )
        .transfer( applicationInfoDTO, ApplicationInfo.class ) ).collect( Collectors.toList() );
  }

  private String getYarnRestUrl( ClusterEntityProjection clusterEntity ) throws InvalidResponseException {
    try {
      String configUrl = "http://" + clusterEntity.getHost() + ":8080/api/v1/clusters/" + clusterEntity.getName()
        + "/configurations?type=yarn-site&tag=version1";
      String yarnRestUrl = CommonJsonHandler.get()
        .getTypedValueFromInnerField(
          httpAuthenticationClient.makeAuthenticatedRequest( clusterEntity.getName(), configUrl, false ),
          String.class, "items", "properties", "yarn.resourcemanager.webapp.address" );

      if ( CheckingParamsUtil.isParamsNullOrEmpty( yarnRestUrl ) ) {
        yarnRestUrl = sshAuthenticationClient
          .executeCommand( clusterEntity.getName(), "hdfs getconf -confKey yarn.resourcemanager.webapp.address" )
          .getOutMessage();

        if ( CheckingParamsUtil.isParamsNullOrEmpty( yarnRestUrl ) ) {
          throw new InvalidResponseException(
            "Can't find yarn resource manager webapp address for cluster - " + clusterEntity.getName() );
        }
      }

      return yarnRestUrl;
    } catch ( CommonUtilException | AuthenticationRequestException ex ) {
      throw new InvalidResponseException( ex );
    }
  }
}
