package com.epam.health.tool.authentication.http;

import com.epam.health.tool.authentication.kerberos.KerberosAuthenticationClient;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpAuthenticationClient {
    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;
    @Autowired
    private ClusterDao clusterDao;
    @Autowired
    private KerberosAuthenticationClient kerberosAuthenticationClient;

    public String makeAuthenticatedRequest(ClusterEntity clusterEntity, String url, boolean useSpnego) {
        return isUsingSpnego( useSpnego, clusterEntity.isSecured() ) ? makeDoAsRequest( clusterEntity, url )
                : makeSimpleRequest( clusterEntity, url );
    }

    public String makeAuthenticatedRequest( String clusterName, String url ) {
        return makeAuthenticatedRequest( getClusterEntity( clusterName ), url, true );
    }

    public String makeAuthenticatedRequest( String clusterName, String url, boolean useSpnego ) {
        return makeAuthenticatedRequest( getClusterEntity( clusterName ), url, useSpnego );
    }

    private boolean isUsingSpnego( boolean useSpnego, boolean secureCluster ) {
        return useSpnego && secureCluster;
    }

    private ClusterEntity getClusterEntity( String clusterName ) {
        return clusterDao.findByClusterName( clusterName );
    }

    private String makeDoAsRequest( ClusterEntity clusterEntity, String url ) {
        return kerberosAuthenticationClient.makeDoAsAction( clusterEntity, () -> makeSimpleRequest( clusterEntity, url, true ));
    }

    private String makeSimpleRequest( ClusterEntity clusterEntity, String url ) {
        return makeSimpleRequest( clusterEntity, url, false );
    }

    private String makeSimpleRequest( ClusterEntity clusterEntity, String url, boolean useSpnego ) {
        try {
            return BaseHttpAuthenticatedAction.get()
                    .withUsername( clusterEntity.getHttp().getUsername() )
                    .withPassword( clusterEntity.getHttp().getPassword() )
                    .withSpnego( useSpnego )
                    .makeAuthenticatedRequest( url );
        } catch (CommonUtilException e) {
            throw new RuntimeException( e );
        }
    }
}
