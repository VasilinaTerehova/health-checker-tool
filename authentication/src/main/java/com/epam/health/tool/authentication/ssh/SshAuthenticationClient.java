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

import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.credentials.SshCredentialsEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.file.DownloadedFileWrapper;
import com.epam.util.ssh.SshCommonUtil;
import com.epam.util.ssh.delegating.SshExecResult;
import com.epam.util.ssh.executor.impl.SshCommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SshAuthenticationClient {
  @Autowired
  private ClusterDao clusterDao;

  public SshExecResult executeCommand( String clusterName, String command ) throws AuthenticationRequestException {
    return executeCommand( getClusterEntity( clusterName ), command );
  }

  public SshExecResult executeCommand( String clusterName, String command, String host )
    throws AuthenticationRequestException {
    return executeCommand( getClusterEntity( clusterName ), command, host );
  }

  public SshExecResult executeCommand( ClusterEntity clusterEntity, String command )
    throws AuthenticationRequestException {
    return executeCommand( clusterEntity, command, clusterEntity.getHost() );
  }

  public SshExecResult executeCommand( ClusterEntity clusterEntity, String command, boolean useKinit )
    throws AuthenticationRequestException {
    return executeCommand( clusterEntity, command, clusterEntity.getHost(), useKinit );
  }

  public SshExecResult executeCommand( ClusterEntity clusterEntity, String command, String host, boolean useKinit )
    throws AuthenticationRequestException {
    try {
      SshCommandExecutor commandExecutor =
        BaseCommandExecutorBuilder.get().withUsername( clusterEntity.getSsh().getUsername() )
          .withPassword( clusterEntity.getSsh().getPassword() )
          .withIdentityPath( clusterEntity.getSsh().getPemFilePath() ).withClusterEntity( clusterEntity )
          .withKinit( useKinit ).build();

      return commandExecutor.executeCommand( trimHost( host ), command );

    } catch ( CommonUtilException e ) {
      throw new AuthenticationRequestException( e );
    }
  }

  public SshExecResult executeCommand( ClusterEntity clusterEntity, String command, String host )
    throws AuthenticationRequestException {
    try {
      SshCommandExecutor commandExecutor =
        BaseCommandExecutorBuilder.get().withUsername( clusterEntity.getSsh().getUsername() )
          .withPassword( clusterEntity.getSsh().getPassword() )
          .withIdentityPath( clusterEntity.getSsh().getPemFilePath() ).build();

      return commandExecutor.executeCommand( trimHost( host ), command );

    } catch ( CommonUtilException e ) {
      throw new AuthenticationRequestException( e );
    }
  }

  public SshExecResult executeCommand( SshCredentialsEntity sshCredentialsEntity, String command, String host )
    throws AuthenticationRequestException {
    try {
      SshCommandExecutor commandExecutor =
        BaseCommandExecutorBuilder.get().withUsername( sshCredentialsEntity.getUsername() )
          .withPassword( sshCredentialsEntity.getPassword() )
          .withIdentityPath( sshCredentialsEntity.getPemFilePath() ).build();

      return commandExecutor.executeCommand( trimHost( host ), command );

    } catch ( CommonUtilException e ) {
      throw new AuthenticationRequestException( e );
    }
  }

  public DownloadedFileWrapper downloadFile( ClusterEntity clusterEntity, String pathToFile )
    throws AuthenticationRequestException {
    try {
      return SshCommonUtil
        .buildSshSftpDownloader( clusterEntity.getSsh().getUsername(), clusterEntity.getSsh().getPassword(),
          clusterEntity.getSsh().getPemFilePath() )
        .downloadViaSftpAsFileWrapper( clusterEntity.getHost(), pathToFile );
    } catch ( CommonUtilException e ) {
      throw new AuthenticationRequestException( e );
    }
  }

  public DownloadedFileWrapper downloadFile( String clusterName, String command )
    throws AuthenticationRequestException {
    return downloadFile( getClusterEntity( clusterName ), command );
  }

  private ClusterEntity getClusterEntity( String clusterName ) {
    return clusterDao.findByClusterName( clusterName );
  }

  private String trimHost( String host ) {
    return host.contains( ":" ) ? host.split( ":" )[ 0 ] : host;
  }
}
