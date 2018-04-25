package com.epam.health.tool.facade.common.service.log;

import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.log.IServiceLogSearchFacade;
import com.epam.health.tool.facade.service.log.IServiceLogsSearcher;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.epam.util.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class CommonServiceLogSearchFacade implements IServiceLogSearchFacade {
    public static final String HBASE_LOG_PROPERTY = "hbase.log.dir";
    public static final String ZOOKEEPER_LOG_PROPERTY = "zookeeper.log.dir";

    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;
    @Autowired
    private ClusterDao clusterDao;
    @Autowired
    private IFacadeImplResolver<IRunningClusterParamReceiver> clusterParamReceiverIFacadeImplResolver;

    @Override
    public String searchLogs( String clusterName, ServiceTypeEnum serviceType ) {
        return getLogSearchersMap().getOrDefault(serviceType, clusterName1 -> StringUtils.EMPTY).searchLogsLocation( clusterName );
    }

    protected IServiceLogsSearcher createServiceLogSearcher( String logProperty, String defaultPath ) {
        return new CommonServiceLogSearcher( sshAuthenticationClient, clusterDao, clusterParamReceiverIFacadeImplResolver ) {
            @Override
            protected String getLogPropertyName() {
                return logProperty;
            }

            @Override
            protected String getDefaultPath() {
                return defaultPath;
            }
        };
    }


    protected abstract Map<ServiceTypeEnum, IServiceLogsSearcher> getLogSearchersMap();
}
