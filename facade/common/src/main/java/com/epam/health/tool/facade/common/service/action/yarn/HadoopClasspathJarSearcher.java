package com.epam.health.tool.facade.common.service.action.yarn;

import com.epam.facade.model.exception.StreamTaskExecutionException;
import com.epam.health.tool.model.credentials.SshCredentialsEntity;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.StringUtils;
import com.epam.util.ssh.SshCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.*;

public class HadoopClasspathJarSearcher {
    private static final Logger log = LoggerFactory.getLogger( HadoopClasspathJarSearcher.class );
    private final static String HADOOP_CLASSPATH_COMMAND = "hadoop classpath";
    private SshCredentialsEntity sshCredentialsEntity;
    private String host;

    private HadoopClasspathJarSearcher() {
        this.sshCredentialsEntity = new SshCredentialsEntity();
        this.host = StringUtils.EMPTY;
    }

    public static HadoopClasspathJarSearcher get() {
        return new HadoopClasspathJarSearcher();
    }

    public HadoopClasspathJarSearcher withUsername( String username ) {
        this.sshCredentialsEntity.setUsername( username );

        return this;
    }

    public HadoopClasspathJarSearcher withPassword( String password ) {
        this.sshCredentialsEntity.setPassword( password );

        return this;
    }

    public HadoopClasspathJarSearcher withSshCredentials( SshCredentialsEntity sshCredentials ) {
        this.sshCredentialsEntity = sshCredentials;

        return this;
    }

    public HadoopClasspathJarSearcher withHost( String host ) {
        this.host = host;

        return this;
    }

    public String findJobJarOnCluster( String jarMask ) {
        try {
            String hadoopClasspath = SshCommonUtil.buildSshCommandExecutor( sshCredentialsEntity.getUsername(),
                    sshCredentialsEntity.getPassword(), sshCredentialsEntity.getPemFilePath() )
                    .executeCommand( host, HADOOP_CLASSPATH_COMMAND ).getOutMessage().trim();

            ForkJoinPool forkJoinPool = new ForkJoinPool( hadoopClasspath.split( ":" ).length );

            try {
                return forkJoinPool.submit( () -> Arrays.stream( hadoopClasspath.split(":") ).parallel()
                        .map( possiblePathToJar -> findExamplesPath( possiblePathToJar, jarMask ))
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
        } catch (CommonUtilException | StreamTaskExecutionException e) {
            log.error( e.getMessage() );
        }

        return StringUtils.EMPTY;
    }

    private String findExamplesPath( String possiblePathToJar, String jarMask ) {
        try {
            String result = SshCommonUtil.buildSshCommandExecutor( sshCredentialsEntity.getUsername(), sshCredentialsEntity.getPassword(), sshCredentialsEntity.getPemFilePath() )
                    .executeCommand( host, "ls " + possiblePathToJar + " | grep " + jarMask ).getOutMessage();
            if ( !CheckingParamsUtil.isParamsNullOrEmpty( result ) ) {
                return result.split( "\\s+" )[0].trim();
            }

            return StringUtils.EMPTY;
        }
        catch ( CommonUtilException ex ) {
            throw new StreamTaskExecutionException( ex );
        }
    }
}
