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

package com.epam.facade.model.service.fix;

import com.epam.facade.model.INoArgConsumer;
import com.epam.facade.model.cluster.receiver.InvalidBuildParamsException;
import com.epam.health.tool.model.credentials.SshCredentialsEntity;
import com.epam.util.common.CheckingParamsUtil;

import java.util.Objects;

public class SshRunningParam {
    private SshCredentialsEntity sshCredentialsEntity;
    private String node;
    private String command;

    private SshRunningParam() {}

    public SshCredentialsEntity getSshCredentialsEntity() {
        return sshCredentialsEntity;
    }

    public String getNode() {
        return node;
    }

    public String getCommand() {
        return command;
    }

    public static class SshRunningParamBuilder {
        private SshRunningParam sshRunningParam;

        private SshRunningParamBuilder() {
            this.sshRunningParam = new SshRunningParam();
        }

        public static SshRunningParamBuilder get() {
            return new SshRunningParamBuilder();
        }

        public SshRunningParamBuilder withNode( String node ) {
            return verifyAndAddStringParam( node, () -> this.sshRunningParam.node = node );
        }

        public SshRunningParamBuilder withCommand( String command ) {
            return verifyAndAddStringParam( command, () -> this.sshRunningParam.command = command );
        }

        public SshRunningParamBuilder withUsername( String username ) {
            return verifyAndAddSsh( username, () -> this.sshRunningParam.sshCredentialsEntity.setUsername( username ) );
        }

        public SshRunningParamBuilder withPassword( String password ) {
            return verifyAndAddSsh( password, () -> this.sshRunningParam.sshCredentialsEntity.setPassword( password ) );
        }

        public SshRunningParamBuilder withPemFile( String pemFile ) {
            return verifyAndAddSsh( pemFile, () -> this.sshRunningParam.sshCredentialsEntity.setPemFilePath( pemFile ) );
        }

        public SshRunningParamBuilder withSshEntity( SshCredentialsEntity sshEntity ) {
            return verifyAndAddSsh( sshEntity.getUsername(), () -> this.sshRunningParam.sshCredentialsEntity.setUsername( sshEntity.getUsername() ) )
                    .verifyAndAddStringParam( sshEntity.getPassword(), () -> this.sshRunningParam.sshCredentialsEntity.setPassword( sshEntity.getPassword() ) )
                    .verifyAndAddStringParam( sshEntity.getPemFilePath(), () -> this.sshRunningParam.sshCredentialsEntity.setPemFilePath( sshEntity.getPemFilePath() ) );
        }

        public SshRunningParam build() throws InvalidBuildParamsException {
            assertParams();

            return this.sshRunningParam;
        }

        private SshRunningParamBuilder verifyAndAddStringParam( String param, INoArgConsumer noArgConsumer ) {
            if ( CheckingParamsUtil.isParamsNotNullOrEmpty( param ) ) {
                noArgConsumer.execute();
            }

            return this;
        }

        private SshRunningParamBuilder verifyAndAddSsh( String param, INoArgConsumer noArgConsumer) {
            if ( Objects.isNull( this.sshRunningParam.sshCredentialsEntity ) ) {
                this.sshRunningParam.sshCredentialsEntity = new SshCredentialsEntity();
            }

            return verifyAndAddStringParam( param, noArgConsumer );
        }

        private void assertParams() throws InvalidBuildParamsException {
            if ( Objects.isNull( this.sshRunningParam.sshCredentialsEntity )
                    || CheckingParamsUtil.isParamsNullOrEmpty( this.sshRunningParam.command, this.sshRunningParam.node,
                    this.sshRunningParam.sshCredentialsEntity.getUsername(), this.sshRunningParam.sshCredentialsEntity.getPassword() ) ) {
                throw new InvalidBuildParamsException( "Not enough params!" );
            }
        }
    }
}
