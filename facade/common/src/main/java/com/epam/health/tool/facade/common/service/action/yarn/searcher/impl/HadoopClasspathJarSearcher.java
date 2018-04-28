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
