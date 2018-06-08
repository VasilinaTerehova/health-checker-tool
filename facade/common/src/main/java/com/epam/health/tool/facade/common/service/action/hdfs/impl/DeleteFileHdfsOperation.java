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

package com.epam.health.tool.facade.common.service.action.hdfs.impl;

import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.common.service.action.hdfs.CommonHdfsOperation;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.ssh.delegating.SshExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component( "delete-file" )
public class DeleteFileHdfsOperation extends CommonHdfsOperation {
  private static final String HADOOP_COMMAND = "hadoop fs -rm -skipTrash";
  private static final String HDFS_OPERATION_NAME = "Delete file";

  @Autowired
  public DeleteFileHdfsOperation( SshAuthenticationClient sshAuthenticationClient ) {
    super( sshAuthenticationClient );
  }

  @Override
  protected SshExecResult performWithException( ClusterEntity clusterEntity ) throws InvalidResponseException {
    try {
      SshExecResult sshExecResult = sshAuthenticationClient.executeCommand( clusterEntity,
        HADOOP_COMMAND.concat( " " ).concat( createUserDirectoryPathString( clusterEntity ) ), true );
      if ( isDirectoryNotExists( sshExecResult ) ) {
        sshExecResult = sshAuthenticationClient.executeCommand( clusterEntity,
          HADOOP_COMMAND.concat( " " ).concat( createTempDirectoryPathString() ) );
      }

      return sshExecResult;
    } catch ( AuthenticationRequestException ex ) {
      throw new InvalidResponseException( ex );
    }
  }

  @Override
  protected boolean isRunSuccessfully( SshExecResult sshExecResult ) {
    return sshExecResult.getOutMessage().contains( "Deleted" );
  }

  @Override
  protected List<String> getAlerts( SshExecResult sshExecResult ) {
    return Collections.singletonList( getError( sshExecResult ) );
  }

  @Override
  protected String getJobName() {
    return HDFS_OPERATION_NAME;
  }
}
