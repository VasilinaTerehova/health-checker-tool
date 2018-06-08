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

package com.epam.health.tool.facade.common.service.action.hdfs;

import com.epam.facade.model.accumulator.results.impl.JobResultImpl;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.ssh.delegating.SshExecResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommonHdfsOperation implements IHdfsOperation {
    protected SshAuthenticationClient sshAuthenticationClient;
    private final static String TEST_FILE_NAME = "test_only_k.txt";

    public CommonHdfsOperation(SshAuthenticationClient sshAuthenticationClient) {
        this.sshAuthenticationClient = sshAuthenticationClient;
    }

    @Override
    public JobResultProjection perform(ClusterEntity clusterEntity) {
        try {
            JobResultImpl hdfsOperationResult = new JobResultImpl( getJobName() );

            SshExecResult sshExecResult = performWithException( clusterEntity );

            removeBashRCWarnings(sshExecResult);
            hdfsOperationResult.setSuccess(isRunSuccessfully( sshExecResult ));
            //Don't set alerts if job was successfully
            if ( !hdfsOperationResult.isSuccess() ) {
                hdfsOperationResult.setAlerts(getAlerts( sshExecResult ));
            }

            return hdfsOperationResult;
        } catch (InvalidResponseException e) {
            return createFailedJob( e.getMessage() );
        }
    }

    protected abstract SshExecResult performWithException( ClusterEntity clusterEntity ) throws InvalidResponseException;
    protected abstract String getJobName();
    protected abstract boolean isRunSuccessfully( SshExecResult sshExecResult );
    protected abstract List<String> getAlerts( SshExecResult sshExecResult );

    //Use Linux File.separator
    protected String createUserDirectoryPathString( ClusterEntity clusterEntity ) {
        return "/user/".concat( clusterEntity.getSsh().getUsername() ).concat( "/" )
                .concat( TEST_FILE_NAME );
    }

    //Use Linux File.separator
    protected String createTempDirectoryPathString() {
        return "/tmp/".concat( TEST_FILE_NAME );
    }

    protected boolean isDirectoryNotExists( SshExecResult sshExecResult ) {
        return sshExecResult.getErrMessage().contains( "No such file or directory" )
                || sshExecResult.getOutMessage().contains( "No such file or directory" );
    }

    protected String getError( SshExecResult sshExecResult ) {
        return sshExecResult.getErrMessage().concat( " " ).concat( sshExecResult.getOutMessage() ).trim();
    }

    private void removeBashRCWarnings( SshExecResult sshExecResult ) {
        SshExecResult.SshExecResultBuilder.get( sshExecResult )
                .setErrMessage( removeBashRCWarningsFromString( sshExecResult.getErrMessage() ) )
                .setOutMessage( removeBashRCWarningsFromString( sshExecResult.getOutMessage() ) )
                .build();
    }

    private String removeBashRCWarningsFromString( String message ) {
        return Arrays.stream( message.split( "\n" ) )
                .filter( line -> !line.contains( "line editing not enabled" ) ).collect(Collectors.joining( "\n" )).trim();
    }

    private JobResultProjection createFailedJob( String message ) {
        return new JobResultImpl( getJobName(), false, Collections.singletonList( message ) );
    }
}
