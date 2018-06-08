/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.health.tool.authentication.http;

import com.epam.util.common.CommonUtilException;
import com.epam.util.http.HttpRequestExecutor;
import com.epam.util.http.header.IHeaderCreator;
import org.apache.http.HttpHost;
import org.apache.http.auth.*;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.log4j.Logger;

import java.security.Principal;

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
                .setAuthCache( generateAuthCache( extractHostFromUrlString( url ) ) )
                .setHeader( generateAuthHeader() )
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

    private AuthCache generateAuthCache( String host ) {
        if ( !useSpnego ) {
            AuthCache authCache = new BasicAuthCache();
            authCache.put( HttpHost.create( host ), new BasicScheme() );

            return authCache;
        }

        return null;
    }

    private IHeaderCreator generateAuthHeader() {
        if ( !useSpnego ) {
            return (httpUriRequest, httpClientContext) -> {
                try {
                    return new BasicScheme().authenticate( new UsernamePasswordCredentials( username, password ), httpUriRequest, httpClientContext );
                } catch (AuthenticationException e) {
                    throw new CommonUtilException( e );
                }
            };
        }

        return null;
    }

    private String extractHostFromUrlString( String url ) {
        String[] hostSplit = url.split( ":" );

        return hostSplit[0].concat( "://" ).concat( hostSplit[1] ).concat( ":" ).concat( hostSplit[2].split( "/" )[0] );
    }
}
