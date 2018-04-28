package com.epam.health.tool.facade.common.service.action.yarn.searcher.impl;

import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.common.service.action.yarn.searcher.BaseJarSearcher;

public abstract class DefaultPathJarSearcher extends BaseJarSearcher {
    public DefaultPathJarSearcher(SshAuthenticationClient sshAuthenticationClient) {
        super(sshAuthenticationClient);
    }

    @Override
    public int speedRating() {
        return 1;
    }

    @Override
    protected String searchJarPath(String jarMask, String clusterName) {
        return findExamplesPath( jarMask, clusterName, getDefaultPath() );
    }

    protected abstract String getDefaultPath();
}
