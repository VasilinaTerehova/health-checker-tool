package com.epam.health.tool.facade.common.service.action;

import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.facade.service.action.IServiceHealthCheckAction;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonSshHealthCheckAction implements IServiceHealthCheckAction {
    @Autowired
    protected SshAuthenticationClient sshAuthenticationClient;

    protected void kinitOnClusterIfNecessary( ClusterEntity clusterEntity ) {
        if ( clusterEntity.isSecured() ) {
            sshAuthenticationClient.executeCommand( clusterEntity, "echo " + clusterEntity.getKerberos().getPassword()
                    + " | kinit " + clusterEntity.getKerberos().getUsername() );
        }
    }
}
