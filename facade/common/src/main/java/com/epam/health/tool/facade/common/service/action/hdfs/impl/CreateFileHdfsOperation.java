package com.epam.health.tool.facade.common.service.action.hdfs.impl;

import com.epam.facade.model.accumulator.results.impl.JobResultImpl;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.common.service.action.hdfs.CommonHdfsOperation;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.ssh.delegating.SshExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("create-file")
public class CreateFileHdfsOperation extends CommonHdfsOperation {
    private final static String HADOOP_COMMAND = "hadoop fs -touchz";
    private final static String HDFS_OPERATION_NAME = "Create empty file";
    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;

    @Override
    public JobResultProjection perform(ClusterEntity clusterEntity) {
        JobResultImpl hdfsOperationResult = new JobResultImpl(HDFS_OPERATION_NAME);

        SshExecResult sshExecResult = sshAuthenticationClient.executeCommand(clusterEntity,
                HADOOP_COMMAND.concat(" ").concat(createUserDirectoryPathString(clusterEntity)));
        if (isDirectoryNotExists(sshExecResult)) {
            sshExecResult = sshAuthenticationClient.executeCommand(clusterEntity,
                    HADOOP_COMMAND.concat(" ").concat(createTempDirectoryPathString()));
        }

        removeBashRCWarnings(sshExecResult);
        hdfsOperationResult.setSuccess(isTouchRunSuccessfully(sshExecResult));
        hdfsOperationResult.setAlerts(Collections.singletonList(getError(sshExecResult)));

        return hdfsOperationResult;
    }

    private boolean isTouchRunSuccessfully(SshExecResult sshExecResult) {
        return sshExecResult.getErrMessage().isEmpty() && sshExecResult.getOutMessage().isEmpty();
    }
}
