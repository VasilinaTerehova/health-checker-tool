package com.epam.health.tool.authentication.http;

import com.epam.util.common.CommonUtilException;
import com.epam.util.http.HttpRequestExecutor;
import com.epam.util.http.header.IHeaderCreator;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.log4j.Logger;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class BaseHttpAuthenticatedAction {
    private static final Logger logger = Logger.getLogger(BaseHttpAuthenticatedAction.class);
    private String username;
    private String password;
    private boolean useSpnego;

    private BaseHttpAuthenticatedAction() {
        this.useSpnego = false;
    }

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

    public BaseHttpAuthenticatedAction withSpnego() {
        this.useSpnego = true;

        return this;
    }

    public BaseHttpAuthenticatedAction withSpnego( boolean useSpnego ) {
        this.useSpnego = useSpnego;

        return this;
    }

    public String makeAuthenticatedRequest( String url ) throws CommonUtilException {
        return HttpRequestExecutor.get().setAuthSchemes( createAuthShemesList() )
                .setCredentialsProvider( createHttpCredentialsProvider() )
                .executeUrlRequest( url );
    }

    private CredentialsProvider createHttpCredentialsProvider(  ) {
        return useSpnego ? createSpnegoCredentialsProvider() : createBasicCredentialsProvider();
    }

    private Registry<AuthSchemeProvider> createAuthShemesList() {
        return useSpnego ? RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(true)).build()
                : RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.BASIC, new BasicSchemeFactory()).build();
    }

    private CredentialsProvider createSpnegoCredentialsProvider() {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        Credentials useJaasCreds = new Credentials() {

            public String getPassword() {
                return null;
            }

            public Principal getUserPrincipal() {
                return null;
            }

        };

        credsProvider.setCredentials(new AuthScope(null, -1, null), useJaasCreds);

        return credsProvider;
    }

    private CredentialsProvider createBasicCredentialsProvider() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials =
                new UsernamePasswordCredentials( username, password );
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        return credentialsProvider;
    }
}
