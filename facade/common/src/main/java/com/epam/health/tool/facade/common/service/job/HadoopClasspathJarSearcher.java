package com.epam.health.tool.facade.common.service.job;

import com.epam.health.tool.model.credentials.SshCredentialsEntity;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.StringUtils;
import com.epam.util.ssh.SshCommonUtil;

import java.util.Arrays;

public class HadoopClasspathJarSearcher {
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
            String hadoopClasspath = SshCommonUtil.buildSshCommandExecutor( sshCredentialsEntity.getUsername(), sshCredentialsEntity.getPassword(), sshCredentialsEntity.getPemFilePath() )
                    .executeCommand( host, HADOOP_CLASSPATH_COMMAND );

            return Arrays.stream( hadoopClasspath.split(":") )
                    .map( possiblePathToJar -> findExamplesPath( possiblePathToJar, jarMask ) ).findFirst().orElse( StringUtils.EMPTY );
        } catch (CommonUtilException | RuntimeException e) {
            e.printStackTrace();
        }

        return StringUtils.EMPTY;
    }

    private String findExamplesPath( String possiblePathToJar, String jarMask ) {
        try {
            String result = SshCommonUtil.buildSshCommandExecutor( sshCredentialsEntity.getUsername(), sshCredentialsEntity.getPassword(), sshCredentialsEntity.getPemFilePath() )
                    .executeCommand( host, "ls " + possiblePathToJar + " | grep " + jarMask );
            if ( !CheckingParamsUtil.isParamsNullOrEmpty( result ) ) {
                return result.split( "\\S+" )[0].trim();
            }

            return StringUtils.EMPTY;
        }
        catch ( CommonUtilException ex ) {
            throw new RuntimeException( ex );
        }
    }
}
