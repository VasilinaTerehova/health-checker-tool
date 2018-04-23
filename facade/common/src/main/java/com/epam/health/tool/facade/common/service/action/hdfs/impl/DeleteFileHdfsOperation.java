package com.epam.health.tool.facade.common.service.action.hdfs.impl;

import com.epam.facade.model.accumulator.results.impl.HdfsHealthCheckResult;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.common.service.action.hdfs.CommonHdfsOperation;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.ssh.delegating.SshExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("delete-file")
public class DeleteFileHdfsOperation extends CommonHdfsOperation {
    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;
    private final static String HADOOP_COMMAND = "hadoop fs -rm -skipTrash";
    private final static String HDFS_OPERATION_NAME = "Delete file";

    @Override
    public HdfsHealthCheckResult.HdfsOperationResult perform(ClusterEntity clusterEntity) {
        HdfsHealthCheckResult.HdfsOperationResult hdfsOperationResult = new HdfsHealthCheckResult.HdfsOperationResult( HDFS_OPERATION_NAME );

        SshExecResult sshExecResult = sshAuthenticationClient.executeCommand( clusterEntity,
                HADOOP_COMMAND.concat( " " ).concat( createUserDirectoryPathString( clusterEntity ) ) );
        if ( isDirectoryNotExists( sshExecResult ) ) {
            sshExecResult = sshAuthenticationClient.executeCommand( clusterEntity,
                    HADOOP_COMMAND.concat( " " ).concat( createTempDirectoryPathString() ) );
        }

        removeBashRCWarnings( sshExecResult );
        hdfsOperationResult.setSuccess( isDeleteRunSuccessfully( sshExecResult ) );
        hdfsOperationResult.setAlert( getError( sshExecResult ) );

        return hdfsOperationResult;
    }

    private boolean isDeleteRunSuccessfully( SshExecResult sshExecResult ) {
        return sshExecResult.getOutMessage().contains( "Deleted" );
    }
}
