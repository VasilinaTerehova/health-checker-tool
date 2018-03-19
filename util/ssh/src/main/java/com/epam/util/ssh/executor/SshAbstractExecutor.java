package com.epam.util.ssh.executor;

import com.epam.util.ssh.delegating.DelegatingSshSession;

import java.io.IOException;

public abstract class SshAbstractExecutor {
    private String username;
    private String password;
    private String identityPath;

    public SshAbstractExecutor(String username, String password, String identityPath) {
        this.username = username;
        this.password = password;
        this.identityPath = identityPath;
    }

    protected DelegatingSshSession createDelegationSshSession(String host, int port)
            throws IOException {
        return new DelegatingSshSession( username, host, port, password, identityPath );
    }
}
