package com.epam.health.tool.facade.common.service.action.hdfs.impl;

import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.common.service.action.hdfs.CommonHdfsOperation;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.ssh.delegating.SshExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("create-file")
public class CreateFileHdfsOperation extends CommonHdfsOperation {
    private final static String HADOOP_COMMAND = "hadoop fs -touchz";
    private final static String HDFS_OPERATION_NAME = "Create empty file";

    @Autowired
    public CreateFileHdfsOperation(SshAuthenticationClient sshAuthenticationClient) {
        super(sshAuthenticationClient);
    }

    @Override
    protected SshExecResult performWithException(ClusterEntity clusterEntity) throws InvalidResponseException {
        try {
            SshExecResult sshExecResult = sshAuthenticationClient.executeCommand(clusterEntity,
                    HADOOP_COMMAND.concat(" ").concat(createUserDirectoryPathString(clusterEntity)));
            if (isDirectoryNotExists(sshExecResult)) {
                sshExecResult = sshAuthenticationClient.executeCommand(clusterEntity,
                        HADOOP_COMMAND.concat(" ").concat(createTempDirectoryPathString()));
            }

            return sshExecResult;
        }
        catch ( AuthenticationRequestException ex ) {
            throw new InvalidResponseException( ex );
        }
    }

    @Override
    protected boolean isRunSuccessfully(SshExecResult sshExecResult) {
        return sshExecResult.getErrMessage().isEmpty() && sshExecResult.getOutMessage().isEmpty();
    }

    @Override
    protected List<String> getAlerts(SshExecResult sshExecResult) {
        return Collections.singletonList( getError(sshExecResult) );
    }

    @Override
    protected String getJobName() {
        return HDFS_OPERATION_NAME;
    }
}
