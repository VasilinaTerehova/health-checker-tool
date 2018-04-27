package com.epam.health.tool.facade.common.service.action;

import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public abstract class CommonSshHealthCheckAction implements IServiceHealthCheckAction {
    @Autowired
    protected SshAuthenticationClient sshAuthenticationClient;
    @Autowired
    protected ClusterDao clusterDao;

    protected void kinitOnClusterIfNecessary( ClusterEntity clusterEntity ) throws InvalidResponseException {
        try {
            if ( clusterEntity.isSecured() && isKerberosParamsPresent( clusterEntity ) ) {
                sshAuthenticationClient.executeCommand( clusterEntity, "echo ".concat( clusterEntity.getKerberos().getPassword() )
                        .concat( " | kinit " ).concat( clusterEntity.getKerberos().getUsername() ) );
            }
        }
        catch ( AuthenticationRequestException ex) {
            throw new InvalidResponseException( ex );
        }
    }

    private boolean isKerberosParamsPresent( ClusterEntity clusterEntity ) {
        return Objects.nonNull( clusterEntity.getKerberos() ) && CheckingParamsUtil.isParamsNotNullOrEmpty(
                clusterEntity.getKerberos().getUsername(), clusterEntity.getKerberos().getPassword() );
    }
}
