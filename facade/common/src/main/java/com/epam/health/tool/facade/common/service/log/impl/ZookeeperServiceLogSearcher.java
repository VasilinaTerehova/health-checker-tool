package com.epam.health.tool.facade.common.service.log.impl;

import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.service.log.CommonServiceLogSearcher;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;

public abstract class ZookeeperServiceLogSearcher extends CommonServiceLogSearcher {
    @Override
    protected String getLogPropertyName() {
        return null;
    }

    public ZookeeperServiceLogSearcher(SshAuthenticationClient sshAuthenticationClient, ClusterDao clusterDao, IFacadeImplResolver<IRunningClusterParamReceiver> clusterParamReceiverIFacadeImplResolver) {
        super(sshAuthenticationClient, clusterDao, clusterParamReceiverIFacadeImplResolver);
    }
}
