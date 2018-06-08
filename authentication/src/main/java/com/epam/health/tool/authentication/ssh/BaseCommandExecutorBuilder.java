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

package com.epam.health.tool.authentication.ssh;

import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.ssh.executor.impl.SshCommandExecutor;
import org.apache.log4j.Logger;

import java.util.Objects;

/**
 * Created by Aliaksandr_Zhuk on 5/18/2018.
 */
public class BaseCommandExecutorBuilder {

  private static final Logger logger = Logger.getLogger( BaseCommandExecutorBuilder.class );
  private String username;
  private String password;
  private String identityPath;
  private ClusterEntity clusterEntity;
  private boolean useKinit;

  private BaseCommandExecutorBuilder() {
    this.useKinit = false;
  }

  public static BaseCommandExecutorBuilder get() {
    return new BaseCommandExecutorBuilder();
  }

  public BaseCommandExecutorBuilder withUsername( String username ) {
    this.username = username;

    return this;
  }

  public BaseCommandExecutorBuilder withPassword( String password ) {
    this.password = password;

    return this;
  }

  public BaseCommandExecutorBuilder withUsernameAndPassword( String username, String password ) {
    this.username = username;
    this.password = password;

    return this;
  }

  public BaseCommandExecutorBuilder withIdentityPath( String identityPath ) {
    this.identityPath = identityPath;

    return this;
  }

  public BaseCommandExecutorBuilder withKinit( boolean useKinit ) {
    this.useKinit = useKinit;

    return this;
  }

  public BaseCommandExecutorBuilder withClusterEntity( ClusterEntity clusterEntity ) {
    this.clusterEntity = clusterEntity;

    return this;
  }

  public SshCommandExecutor build() {
    SshCommandExecutor sshCommandExecutor = createSshExecutor();
    if ( useKinit && Objects.nonNull( clusterEntity ) ) {
      kinitOnClusterIfNecessary( sshCommandExecutor );
    }
    return sshCommandExecutor;
  }

  private SshCommandExecutor createSshExecutor() {
    return new SshCommandExecutor( this.username, this.password, this.identityPath );
  }

  private void kinitOnClusterIfNecessary( SshCommandExecutor sshCommandExecutor ) {
    try {
      if ( clusterEntity.isSecured() ) {
        sshCommandExecutor
          .executeCommand( trimHost( clusterEntity.getHost() ),
            "echo ".concat( clusterEntity.getKerberos().getPassword() )
              .concat( " | kinit " ).concat( clusterEntity.getKerberos().getUsername() ) );
      }
    } catch ( CommonUtilException ex ) {
      ex.printStackTrace();
    }
  }

  private String trimHost( String host ) {
    return host.contains( ":" ) ? host.split( ":" )[ 0 ] : host;
  }
}
