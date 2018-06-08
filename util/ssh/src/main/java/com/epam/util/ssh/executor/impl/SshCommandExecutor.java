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

package com.epam.util.ssh.executor.impl;

import com.epam.util.common.CommonUtilException;
import com.epam.util.ssh.delegating.DelegatingSshSession;
import com.epam.util.ssh.delegating.SshExecResult;
import com.epam.util.ssh.executor.SshAbstractExecutor;

import java.io.IOException;

public class SshCommandExecutor extends SshAbstractExecutor {
    public SshCommandExecutor(String username, String password, String identityPath) {
        super(username, password, identityPath);
    }

    public SshExecResult executeCommand(String host, int port, String command ) throws
            CommonUtilException {
        try (DelegatingSshSession sshSession = createDelegationSshSession( host, port )) {
            return sshSession.executeCommand(command);
        } catch (IOException ex) {
            throw new CommonUtilException(ex);
        }
    }

    public SshExecResult executeCommand( String host, String command ) throws
            CommonUtilException {
        return executeCommand( host, 22, command );
    }
}
