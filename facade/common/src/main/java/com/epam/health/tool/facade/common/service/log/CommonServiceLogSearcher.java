package com.epam.health.tool.facade.common.service.log;

import com.epam.facade.model.ClusterNodes;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.exception.ImplementationNotResolvedException;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.log.IServiceLogsSearcher;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.ssh.delegating.SshExecResult;

import java.util.Arrays;
import java.util.Collections;

public abstract class CommonServiceLogSearcher implements IServiceLogsSearcher {
    private SshAuthenticationClient sshAuthenticationClient;
    private ClusterDao clusterDao;
    private IFacadeImplResolver<IRunningClusterParamReceiver> clusterParamReceiverIFacadeImplResolver;
    private static final String PS_AUX_CLI = "ps aux | grep";
    private static final String NAME_VALUE_SEPARATOR = "=";

    public CommonServiceLogSearcher(SshAuthenticationClient sshAuthenticationClient, ClusterDao clusterDao,
                                    IFacadeImplResolver<IRunningClusterParamReceiver> clusterParamReceiverIFacadeImplResolver) {
        this.sshAuthenticationClient = sshAuthenticationClient;
        this.clusterDao = clusterDao;
        this.clusterParamReceiverIFacadeImplResolver = clusterParamReceiverIFacadeImplResolver;
    }

    @Override
    public String searchLogsLocation( String clusterName ) {
        return getClusterLiveNodes( clusterDao.findByClusterName( clusterName ) ).getLiveNodes().parallelStream()
                .map( node -> sshAuthenticationClient.executeCommand( clusterName, createPsAuxCommand() ) )
                .map( this::parseCommandResult ).filter( CheckingParamsUtil::isParamsNotNullOrEmpty ).findFirst().orElse( getDefaultPath() );
    }

    protected abstract String getLogPropertyName();
    protected abstract String getDefaultPath();

    private ClusterNodes getClusterLiveNodes( ClusterEntity clusterEntity ) {
        try {
            return new ClusterNodes( this.clusterParamReceiverIFacadeImplResolver.resolveFacadeImpl( clusterEntity.getClusterTypeEnum().name() )
                    .getHdfsNamenodeJson( clusterEntity.getClusterName() ).getLiveNodes(), clusterEntity.getClusterName() );
        } catch (InvalidResponseException | ImplementationNotResolvedException | CommonUtilException e) {
            return new ClusterNodes( Collections.emptySet(), clusterEntity.getClusterName() );
        }
    }

    private String createPsAuxCommand() {
        return PS_AUX_CLI.concat( " " ).concat( getLogPropertyName() );
    }

    private String parseCommandResult( SshExecResult sshExecResult ) {
        return Arrays.stream( sshExecResult.getOutMessage().split( "\\s+" ) ).filter( this::isLogPropertyExpression )
                .findFirst().orElse( " ".concat( NAME_VALUE_SEPARATOR ).concat( " " ) ).split( NAME_VALUE_SEPARATOR )[1].trim();
    }

    private boolean isLogPropertyExpression( String property ) {
        return !CheckingParamsUtil.isParamsNullOrEmpty( property ) && property.equals( getLogPropertyName().concat( NAME_VALUE_SEPARATOR ) );
    }
}
