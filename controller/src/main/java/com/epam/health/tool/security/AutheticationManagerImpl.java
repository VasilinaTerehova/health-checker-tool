package com.epam.health.tool.security;

import com.epam.health.tool.authenticate.impl.*;
import com.epam.health_tool.authenticate.impl.*;
import com.epam.health_tool.kerberos.HadoopKerberosUtil;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class AutheticationManagerImpl implements AuthenticationManager {
    private static final Logger logger = Logger.getLogger(AutheticationManagerImpl.class);

    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if (auth instanceof ClusterAuthentication && auth.isAuthenticated()) {
            return auth;
        }

        if (auth instanceof ClusterCredentials) {
            return makeAuthentication((ClusterCredentials) auth);
        }

        throw new BadCredentialsException("Can't authenticate");
    }

    private Authentication makeAuthentication(ClusterCredentials authentication)
            throws AuthenticationException {
        ClusterAuthentication clusterAuthentication = new ClusterAuthentication();

        clusterAuthentication
                .setCredentialsProvider(createHttpCredentialsProvider(authentication.getHttp()));
        clusterAuthentication.setAuthShemes(createAuthShemesList());
        clusterAuthentication.setSshCredentials(createSshCredentials(authentication.getSsh()));
        clusterAuthentication.setKerberosAuth(loginWithKerberos(authentication));

        // Necessary?
        clusterAuthentication.setAuthenticated(true);

        return clusterAuthentication;
    }

    private CredentialsProvider createHttpCredentialsProvider(HttpCredentials httpCredentials)
            throws AuthenticationException {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials =
                new UsernamePasswordCredentials(httpCredentials.getUsername(), httpCredentials.getPassword());
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        credentialsProvider
                .setCredentials(new AuthScope(null, -1, AuthScope.ANY_REALM, AuthPolicy.SPNEGO), new Credentials() {
                    @Override
                    public Principal getUserPrincipal() {
                        return null;
                    }

                    @Override
                    public String getPassword() {
                        return null;
                    }
                });

        return credentialsProvider;
    }

    private List<String> createAuthShemesList() {
        List<String> authShemes = new ArrayList<>();

        authShemes.add(AuthSchemes.BASIC);
        authShemes.add(AuthSchemes.SPNEGO);

        return authShemes;
    }

    private boolean loginWithKerberos(ClusterCredentials clusterCredentials) throws AuthenticationException {
        Krb5Credentials krb5Credentials = clusterCredentials.getKerberos();
        if (clusterCredentials.getCluster().isSecured()) {
            String krb5Location = krb5Credentials.getKrb5ConfPath();

            if (!krb5Location.isEmpty() && !krb5Credentials.getUsername().isEmpty()) {
                System.setProperty("java.security.krb5.conf", krb5Location);
                logger.info(
                        "Keberos authentication from krb5 file - " + krb5Location + " . With credentials: username - " + krb5Credentials
                                .getUsername()
                                + " password - " + krb5Credentials.getPassword());
                try {
                    if (krb5Credentials.getKeytabLocation() != null && !krb5Credentials.getKeytabLocation().isEmpty()) {
                        HadoopKerberosUtil.doLoginWithKeytab(krb5Credentials.getUsername(), krb5Credentials.getKeytabLocation());
                    } else {
                        HadoopKerberosUtil
                                .doLoginWithPrincipalAndPassword(krb5Credentials.getUsername(), krb5Credentials.getPassword());
                    }

                    return true;
                } catch (IOException | LoginException ex) {
                    throw new BadCredentialsException("Bad kerberos credentials", ex);
                }
            }

            logger.info("Keberos auth unnecessary. Krb5 location - " + krb5Location);
        }

        return false;
    }

    private SshCredentials createSshCredentials(SshCredentials sshCredentials) throws AuthenticationException {
        return sshCredentials;
    }
}
