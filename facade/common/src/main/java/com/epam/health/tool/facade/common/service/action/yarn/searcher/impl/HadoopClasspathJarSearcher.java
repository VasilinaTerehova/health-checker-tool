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

package com.epam.health.tool.facade.common.service.action.yarn.searcher.impl;

import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.common.service.action.yarn.searcher.BaseJarSearcher;
import com.epam.health.tool.facade.resolver.ClusterSpecificComponent;
import com.epam.health.tool.model.ClusterTypeEnum;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

@Component
@ClusterSpecificComponent( ClusterTypeEnum.NONE )
public class HadoopClasspathJarSearcher extends BaseJarSearcher {
    private final static Logger log = LoggerFactory.getLogger( HadoopClasspathJarSearcher.class );
    private final static String HADOOP_CLASSPATH_COMMAND = "hadoop classpath";

    @Autowired
    public HadoopClasspathJarSearcher(SshAuthenticationClient sshAuthenticationClient) {
        super(sshAuthenticationClient);
    }

    @Override
    public int speedRating() {
        return 9;
    }

    @Override
    protected String searchJarPath(String jarMask, String clusterName) {
        try {
            String hadoopClasspath = sshAuthenticationClient
                    .executeCommand( clusterName, HADOOP_CLASSPATH_COMMAND ).getOutMessage().trim();

            // 16 threads - sshd can limit parallel ssh connections, so use max 5 threads
            ForkJoinPool forkJoinPool = new ForkJoinPool( Integer.min( hadoopClasspath.split( ":" ).length, 5 ) );

            try {
                return forkJoinPool.submit( () -> Arrays.stream( hadoopClasspath.split(":") ).parallel()
                        .map( possiblePathToJar -> findExamplesPath( jarMask, clusterName, possiblePathToJar ))
                        .filter( CheckingParamsUtil::isParamsNotNullOrEmpty )
                        .findFirst().orElse( StringUtils.EMPTY ) ).get();
            } catch ( InterruptedException | ExecutionException e ) {
                log.error( e.getMessage() );
            }
            finally {
                //Is it necessary?
                if ( !forkJoinPool.isShutdown() ) {
                    forkJoinPool.shutdownNow();
                }
            }
        } catch ( AuthenticationRequestException e ) {
            log.error( e.getMessage() );
        }

        return StringUtils.EMPTY;
    }

    @Override
    protected Logger log() {
        return log;
    }
}
