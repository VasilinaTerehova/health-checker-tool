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

package com.epam.health.tool.facade.cdh.application;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.ApplicationInfo;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.cluster.IClusterFacade;
import com.epam.health.tool.facade.common.application.CommonApplicationFacade;
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ClusterSpecificComponent( ClusterTypeEnum.CDH )
public class CdhApplicationFacadeImpl extends CommonApplicationFacade {

  @Autowired
  private IClusterFacade clusterFacade;
  @Autowired
  private HttpAuthenticationClient httpAuthenticationClient;

  public List<ApplicationInfo> getApplicationList( String clusterName ) throws InvalidResponseException {
    ClusterEntityProjection clusterEntity = clusterFacade.getCluster( clusterName );
    String url =
      "http://" + clusterEntity.getHost() + ":7180/api/v10/clusters/" + clusterName
        + "/services/yarn/yarnApplications?from=0&limit=20";

    try {
      String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterName, url, false );
      List<ApplicationInfo> appList =
        CommonJsonHandler.get().getListTypedValueFromInnerField( answer, ApplicationInfo.class, "applications" );

      if ( appList != null ) {
        return appList;
      } else {
        throw new InvalidResponseException( "Elements not found. Answer string - " + answer );
      }
    } catch ( CommonUtilException | AuthenticationRequestException ex ) {
      throw new InvalidResponseException( "Elements not found.", ex );
    }
  }
}
