package com.epam.health.tool.facade.common.service.action.hdfs;

import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.ssh.delegating.SshExecResult;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class CommonHdfsOperation implements IHdfsOperation {
    public final static String TEST_FILE_NAME = "test_only_k.txt";
    //Use Linux File.separator
    protected String createUserDirectoryPathString( ClusterEntity clusterEntity ) {
        return "/user/".concat( clusterEntity.getSsh().getUsername() ).concat( "/" )
                .concat( TEST_FILE_NAME );
    }

    //Use Linux File.separator
    protected String createTempDirectoryPathString() {
        return "/tmp/".concat( TEST_FILE_NAME );
    }

    protected boolean isDirectoryNotExists( SshExecResult sshExecResult ) {
        return sshExecResult.getErrMessage().contains( "No such file or directory" )
                || sshExecResult.getOutMessage().contains( "No such file or directory" );
    }

    protected String getError( SshExecResult sshExecResult ) {
        return sshExecResult.getErrMessage().concat( " " ).concat( sshExecResult.getOutMessage() );
    }

    protected void removeBashRCWarnings( SshExecResult sshExecResult ) {
        sshExecResult.setErrMessage( removeBashRCWarningsFromString( sshExecResult.getErrMessage() ) );
        sshExecResult.setOutMessage( removeBashRCWarningsFromString( sshExecResult.getOutMessage() ) );
    }

    private String removeBashRCWarningsFromString( String message ) {
        return Arrays.stream( message.split( "\n" ) )
                .filter( line -> !line.contains( "line editing not enabled" ) ).collect(Collectors.joining( "\n" )).trim();
    }
}
