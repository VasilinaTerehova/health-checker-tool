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

package com.epam.util.ssh;

import com.epam.util.ssh.executor.impl.SshCommandExecutor;
import com.epam.util.ssh.executor.impl.SshSftpDownloader;

public class SshCommonUtil {
    public static SshCommandExecutor buildSshCommandExecutor( String username, String password, String identityPath ) {
        return new SshCommandExecutor( username, password, identityPath );
    }

    public static SshSftpDownloader buildSshSftpDownloader( String username, String password, String identityPath ) {
        return new SshSftpDownloader( username, password, identityPath );
    }
}
