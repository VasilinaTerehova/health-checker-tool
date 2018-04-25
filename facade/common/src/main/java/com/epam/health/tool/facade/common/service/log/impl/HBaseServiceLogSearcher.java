package com.epam.health.tool.facade.common.service.log.impl;

import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.common.service.log.CommonServiceLogSearcher;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;

public abstract class HBaseServiceLogSearcher extends CommonServiceLogSearcher {
    private static final String HBASE_LOG_PROPERTY = "hbase.log.dir";

    public HBaseServiceLogSearcher(SshAuthenticationClient sshAuthenticationClient, ClusterDao clusterDao, IFacadeImplResolver<IRunningClusterParamReceiver> clusterParamReceiverIFacadeImplResolver) {
        super(sshAuthenticationClient, clusterDao, clusterParamReceiverIFacadeImplResolver);
    }

    @Override
    protected String getLogPropertyName() {
        return HBASE_LOG_PROPERTY;
    }
}
