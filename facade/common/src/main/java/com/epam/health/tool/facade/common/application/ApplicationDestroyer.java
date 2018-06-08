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

package com.epam.health.tool.facade.common.application;

import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.ssh.delegating.SshExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * Created by Aliaksandr_Zhuk on 5/22/2018.
 */
@Component
@Scope( "prototype" )
public class ApplicationDestroyer implements Callable<Boolean> {

  private static final String YARN_CLI_KILL_JOB = "yarn application -kill ";
  private static final String MASK_FOR_CHEK_KILLED_JOB = "Killed application ";

  private String appId;
  private String clusterName;

  @Autowired
  private SshAuthenticationClient sshAuthenticationClient;

  @Autowired
  protected ClusterDao clusterDao;

  public void setClusterName( String clusterName ) {
    this.clusterName = clusterName;
  }

  public void setAppId( String appId ) {
    this.appId = appId;
  }

  @Override public Boolean call() throws Exception {
    return killApplication();
  }

  private Boolean killApplication() {
    SshExecResult result;

    if ( !CheckingParamsUtil.isParamsNotNullOrEmpty( appId ) ) {
      return false;
    }

    ClusterEntity clusterEntity = clusterDao.findByClusterName( clusterName );

    try {
      result = sshAuthenticationClient.executeCommand( clusterEntity, YARN_CLI_KILL_JOB.concat( appId ), true );
      return extractJobStatusFromSshExecResult( result.getErrMessage(), MASK_FOR_CHEK_KILLED_JOB.concat( appId ) );
    } catch ( AuthenticationRequestException e ) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean extractJobStatusFromSshExecResult( String result, String mask ) {
    if ( result.contains( mask ) ) {
      return true;
    }
    return false;
  }
}
