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

package com.epam.util.ssh.executor;

import com.epam.util.ssh.delegating.DelegatingSshSession;

import java.io.IOException;

public abstract class SshAbstractExecutor {
    private String username;
    private String password;
    private String identityPath;

    public SshAbstractExecutor(String username, String password, String identityPath) {
        this.username = username;
        this.password = password;
        this.identityPath = identityPath;
    }

    protected DelegatingSshSession createDelegationSshSession(String host, int port)
            throws IOException {
        return new DelegatingSshSession( username, host, port, password, identityPath );
    }
}
