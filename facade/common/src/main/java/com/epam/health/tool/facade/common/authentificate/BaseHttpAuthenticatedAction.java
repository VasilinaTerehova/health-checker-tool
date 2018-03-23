package com.epam.health.tool.facade.common.authentificate;

import com.epam.util.common.CommonUtilException;
import com.epam.util.common.StringUtils;
import com.epam.util.http.HttpRequestExecutor;
import com.epam.util.http.header.IHeaderCreator;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.log4j.Logger;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class BaseHttpAuthenticatedAction {
    private static final Logger logger = Logger.getLogger(BaseHttpAuthenticatedAction.class);
    private String username;
    private String password;

    private BaseHttpAuthenticatedAction() {}

    public static BaseHttpAuthenticatedAction get() {
        return new BaseHttpAuthenticatedAction();
    }

    public BaseHttpAuthenticatedAction withUsername(String username ) {
        this.username = username;

        return this;
    }

    public BaseHttpAuthenticatedAction withPassword(String password ) {
        this.password = password;

        return this;
    }

    public BaseHttpAuthenticatedAction withUsernameAndPassword(String username, String password ) {
        this.username = username;
        this.password = password;

        return this;
    }

    public String makeAuthenticatedRequest( String url ) throws CommonUtilException {
        return HttpRequestExecutor.get().setAuthSchemes( createAuthShemesList() )
                .setCredentialsProvider( createHttpCredentialsProvider() )
                .setHeaderCreator( buildHeaderCreator() ).executeUrlRequest( url );
    }

    private CredentialsProvider createHttpCredentialsProvider(  ) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials =
                new UsernamePasswordCredentials( username, password );
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

    private IHeaderCreator buildHeaderCreator() {
        return httpUriRequest -> {
            try {
                return new BasicScheme()
                        .authenticate( createHttpCredentialsProvider().getCredentials(AuthScope.ANY),
                                httpUriRequest, null);
            } catch (AuthenticationException e) {
                logger.error(e);
                return null;
            }
        };
    }
}
