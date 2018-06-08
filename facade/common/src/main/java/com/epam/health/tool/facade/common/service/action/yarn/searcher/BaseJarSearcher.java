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

package com.epam.health.tool.facade.common.service.action.yarn.searcher;

import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.service.action.IJarSearcher;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.StringUtils;
import org.slf4j.Logger;

public abstract class BaseJarSearcher implements IJarSearcher {
    protected SshAuthenticationClient sshAuthenticationClient;

    public BaseJarSearcher(SshAuthenticationClient sshAuthenticationClient) {
        this.sshAuthenticationClient = sshAuthenticationClient;
    }

    @Override
    public String searchJarPath(String jarMask, String clusterName, String possiblePath) {
        String pathToJar = findExampleJarOnPossiblePath( jarMask, clusterName, possiblePath );

        return pathToJar.isEmpty() ? searchJarPath( jarMask, clusterName ) : pathToJar;
    }

    protected abstract String searchJarPath( String jarMask, String clusterName );
    protected abstract Logger log();

    protected String findExamplesPath( String jarMask, String clusterName, String possiblePathToJar ) {
        try {
            String result = sshAuthenticationClient
                    .executeCommand( clusterName, "ls " + possiblePathToJar + " | grep " + jarMask ).getOutMessage();
            if ( !CheckingParamsUtil.isParamsNullOrEmpty( result ) ) {
                return result.contains( possiblePathToJar ) ? result.split( "\\s+" )[0].trim()
                        : possiblePathToJar.concat( "/" ).concat( result.split( "\\s+" )[0].trim() );
            }

            return StringUtils.EMPTY;
        }
        catch ( AuthenticationRequestException ex ) {
            log().error( ex.getMessage() );
            return StringUtils.EMPTY;
        }
    }

    private String findExampleJarOnPossiblePath( String jarMask, String clusterName, String possiblePath ) {
        return CheckingParamsUtil.isParamsNotNullOrEmpty( possiblePath ) ? findExamplesPath( jarMask, clusterName, possiblePath )
                : StringUtils.EMPTY;
    }
}
