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

package com.epam.health.tool.facade.common.service.fix.action.yarn;

import com.epam.facade.model.cluster.receiver.InvalidBuildParamsException;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.facade.model.exception.StreamTaskExecutionException;
import com.epam.facade.model.service.fix.ServiceFixResult;
import com.epam.facade.model.service.fix.SshRunningParam;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.fix.action.IServiceFixAction;
import com.epam.health.tool.facade.service.fix.action.ServiceFixAction;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.ssh.delegating.SshExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ServiceFixAction( ServiceTypeEnum.YARN )
public class CommonYarnServiceFixAction implements IServiceFixAction {
    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;
    @Autowired
    private ClusterDao clusterDao;
    @Autowired
    private IFacadeImplResolver<IRunningClusterParamReceiver> runningClusterParamImplResolver;

    @Override
    public ServiceFixResult performFix( String clusterName, String rootUsername,  String rootPassword ) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName( clusterName );
        try {
            IRunningClusterParamReceiver runningClusterParamReceiver = runningClusterParamImplResolver.resolveFacadeImpl( clusterEntity.getClusterTypeEnum() );
            SshRunningParam.SshRunningParamBuilder sshRunningParamBuilder = SshRunningParam.SshRunningParamBuilder.get()
                    .withSshEntity( clusterEntity.getSsh() ).withPassword( rootPassword ).withUsername( rootUsername )
                    .withCommand( createCleanCommand( runningClusterParamReceiver, clusterName ) );

            return runningClusterParamReceiver.getLiveNodes( clusterName ).stream()
                    .filter(CheckingParamsUtil::isParamsNotNullOrEmpty)
                    .map( sshRunningParamBuilder::withNode ).map( this::cleanYarnCache ).reduce( this::mergeCleaningResults )
                    .map( this::mapSshResultToFixResult ).orElseThrow( () -> new InvalidResponseException( "Fix fails. No result!" ) );
        } catch ( ImplementationNotResolvedException e ) {
            throw new InvalidResponseException( e );
        }
    }

    @Override
    public List<String> getStepsForFix(String clusterName) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName( clusterName );

        try {
            return createStepList( runningClusterParamImplResolver.resolveFacadeImpl( clusterEntity.getClusterTypeEnum() ), clusterName );
        } catch ( ImplementationNotResolvedException e ) {
            throw new InvalidResponseException( e );
        }
    }

    @Override
    public String getFixCommand(String clusterName) throws InvalidResponseException {
        ClusterEntity clusterEntity = clusterDao.findByClusterName( clusterName );

        try {
            return createCleanCommand( runningClusterParamImplResolver.resolveFacadeImpl( clusterEntity.getClusterTypeEnum() ), clusterName );
        } catch ( ImplementationNotResolvedException e ) {
            throw new InvalidResponseException( e );
        }
    }

    private SshExecResult cleanYarnCache(SshRunningParam.SshRunningParamBuilder sshRunningParamBuilder ) {
        try {
            SshRunningParam sshRunningParam = sshRunningParamBuilder.build();

            return sshAuthenticationClient.executeCommand( sshRunningParam.getSshCredentialsEntity(), sshRunningParam.getCommand(), sshRunningParam.getNode() );
        } catch (InvalidBuildParamsException | AuthenticationRequestException e) {
            return SshExecResult.SshExecResultBuilder.get().setErrMessage( "Cleaning cache failed with error - ".concat( e.getMessage())).build();
        }
    }

    private String createCleanCommand( IRunningClusterParamReceiver runningClusterParamReceiver, String clusterName ) throws InvalidResponseException {
        return YarnCleanCacheCommandBuilder.get().buildCommand( runningClusterParamReceiver.getYarnLocalDirectory( clusterName ) );
    }

    private List<String> createStepList( IRunningClusterParamReceiver runningClusterParamReceiver, String clusterName ) throws InvalidResponseException {
        return YarnCleanCacheCommandBuilder.get().buildStepList( runningClusterParamReceiver.getYarnLocalDirectory( clusterName ) );
    }

    private SshExecResult mergeCleaningResults( SshExecResult sshExecResult, SshExecResult anotherSshExecResult ) {
        return SshExecResult.SshExecResultBuilder.get( sshExecResult )
                .appendToOut( anotherSshExecResult.getOutMessage() )
                .appendToErr( anotherSshExecResult.getErrMessage() ).build();
    }

    private ServiceFixResult mapSshResultToFixResult( SshExecResult sshExecResult ) {
        try {
            return ServiceFixResult.ServiceFixResultBuilder.get().withServiceName( ServiceTypeEnum.YARN.name() )
                    .withFixed( sshExecResult.getErrMessage().isEmpty() && sshExecResult.getOutMessage().isEmpty() )
                    .withAlert( sshExecResult.getErrMessage().concat( " " ).concat( sshExecResult.getOutMessage() ) )
                    .build();
        } catch (InvalidBuildParamsException e) {
            throw new StreamTaskExecutionException( e );
        }
    }
}
