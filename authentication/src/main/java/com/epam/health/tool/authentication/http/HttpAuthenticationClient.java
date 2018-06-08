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

package com.epam.health.tool.authentication.http;

import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.kerberos.KerberosAuthenticationClient;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpAuthenticationClient {
    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;
    @Autowired
    private ClusterDao clusterDao;
    @Autowired
    private KerberosAuthenticationClient kerberosAuthenticationClient;

    public String makeAuthenticatedRequest(ClusterEntity clusterEntity, String url, boolean useSpnego) throws AuthenticationRequestException {
        return isUsingSpnego( useSpnego, clusterEntity.isSecured() ) ? makeDoAsRequest( clusterEntity, url )
                : makeSimpleRequest( clusterEntity, url );
    }

    public String makeAuthenticatedRequest( String clusterName, String url ) throws AuthenticationRequestException {
        return makeAuthenticatedRequest( getClusterEntity( clusterName ), url, true );
    }

    public String makeAuthenticatedRequest( String clusterName, String url, boolean useSpnego ) throws AuthenticationRequestException {
        return makeAuthenticatedRequest( getClusterEntity( clusterName ), url, useSpnego );
    }

    private boolean isUsingSpnego( boolean useSpnego, boolean secureCluster ) {
        return useSpnego && secureCluster;
    }

    private ClusterEntity getClusterEntity( String clusterName ) {
        return clusterDao.findByClusterName( clusterName );
    }

    private String makeDoAsRequest( ClusterEntity clusterEntity, String url ) throws AuthenticationRequestException {
        return kerberosAuthenticationClient.makeDoAsAction( clusterEntity, () -> makeSimpleRequest( clusterEntity, url, true ));
    }

    private String makeSimpleRequest( ClusterEntity clusterEntity, String url ) throws AuthenticationRequestException {
        return makeSimpleRequest( clusterEntity, url, false );
    }

    private String makeSimpleRequest( ClusterEntity clusterEntity, String url, boolean useSpnego ) throws AuthenticationRequestException {
        try {
            return BaseHttpAuthenticatedAction.get()
                    .withUsername( clusterEntity.getHttp().getUsername() )
                    .withPassword( clusterEntity.getHttp().getPassword() )
                    .withSpnego( useSpnego )
                    .makeAuthenticatedRequest( url );
        } catch (CommonUtilException e) {
            throw new AuthenticationRequestException( e );
        }
    }
}
