package com.epam.util.ssh.executor.impl;

import com.epam.util.common.CommonUtilException;
import com.epam.util.ssh.delegating.DelegatingSshSession;
import com.epam.util.ssh.executor.SshAbstractExecutor;

import java.io.IOException;

public class SshCommandExecutor extends SshAbstractExecutor {
    public SshCommandExecutor(String username, String password, String identityPath) {
        super(username, password, identityPath);
    }

    public String executeCommand( String host, int port, String command ) throws
            CommonUtilException {
        try (DelegatingSshSession sshSession = createDelegationSshSession( host, port )) {
            return sshSession.executeCommand(command);
        } catch (IOException ex) {
            throw new CommonUtilException(ex);
        }
    }

    public String executeCommand( String host, String command ) throws
            CommonUtilException {
        return executeCommand( host, 22, command );
    }
}
