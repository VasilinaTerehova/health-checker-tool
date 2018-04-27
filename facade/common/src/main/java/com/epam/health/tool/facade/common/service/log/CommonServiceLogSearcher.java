package com.epam.health.tool.facade.common.service.log;

import com.epam.facade.model.ClusterNodes;
import com.epam.facade.model.accumulator.LogLocation;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.facade.model.exception.ImplementationNotResolvedException;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.facade.resolver.IFacadeImplResolver;
import com.epam.health.tool.facade.service.log.IServiceLogsSearcher;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.transfer.Tuple2;
import com.epam.util.common.CheckingParamsUtil;
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
    public LogLocation searchLogsLocation(String clusterName ) {
        ClusterEntity byClusterName = clusterDao.findByClusterName(clusterName);
        return getClusterLiveNodes(byClusterName)
                .getLiveNodes().parallelStream()
                .map( node -> new Tuple2<>(node, parseCommandResult(sshAuthenticationClient.executeCommand( byClusterName, createPsAuxCommand(), node)) ) )
                .filter( objects -> CheckingParamsUtil.isParamsNotNullOrEmpty(objects.getT2()) )
                .findFirst()
                .map(objects -> new LogLocation(objects.getT1(), objects.getT2()))
                .orElse(new LogLocation("not found", getDefaultPath()));

    }

    protected abstract String getLogPropertyName();
    protected abstract String getDefaultPath();

    private ClusterNodes getClusterLiveNodes( ClusterEntity clusterEntity ) {
        try {
            return new ClusterNodes( this.clusterParamReceiverIFacadeImplResolver.resolveFacadeImpl( clusterEntity.getClusterTypeEnum().name() )
                    .getLiveNodes( clusterEntity.getClusterName() ), clusterEntity.getClusterName() );
        } catch (InvalidResponseException | ImplementationNotResolvedException e) {
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
        return !CheckingParamsUtil.isParamsNullOrEmpty( property ) && property.contains( getLogPropertyName().concat( NAME_VALUE_SEPARATOR ) );
    }
}
