package com.epam.health.tool.authenticate;

import com.epam.health.tool.authenticate.impl.ClusterCredentials;
import com.epam.health_tool.kerberos.Krb5Configurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by Vasilina_Terehova on 3/7/2018.
 */
@Component
public class Authenticator {

    @Autowired
    AuthenticationManager authenticationManager;

    public void authenticate(ClusterCredentials clusterCredentials) {
        if (clusterCredentials.getCluster().isSecured()) {
            clusterCredentials.getKerberos().setKrb5ConfPath(Krb5Configurator.getOrDownloadKrb5FromCluster(clusterCredentials));
        }
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
        SecurityContextHolder.getContext()
                .setAuthentication(authenticationManager.authenticate(
                        clusterCredentials));
    }
}
