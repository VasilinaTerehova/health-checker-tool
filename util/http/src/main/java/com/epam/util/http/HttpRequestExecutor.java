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

package com.epam.util.http;

import com.epam.util.common.CommonUtilException;
import com.epam.util.http.header.IHeaderCreator;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.util.function.Function;

public class HttpRequestExecutor {
    private CredentialsProvider credentialsProvider;
    private Registry<AuthSchemeProvider> authSchemeRegistry;
    //HttpClientBuilder setup chaining functions
    private Function<HttpClientBuilder, HttpClientBuilder> httpClientBuilderSetupAction;
    private AuthCache authCache;
    private IHeaderCreator headerCreator;

    private HttpRequestExecutor() {
        httpClientBuilderSetupAction = this::setSslContextAllowAll;
        httpClientBuilderSetupAction.andThen( this::setDefaultAuthSchemeRegistry );
        httpClientBuilderSetupAction.andThen( this::setDefaultCredentialsProvider );
    }

    public static HttpRequestExecutor get(){
        return new HttpRequestExecutor();
    }

    public HttpRequestExecutor setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;

        return this;
    }

    public HttpRequestExecutor setAuthSchemes( Registry<AuthSchemeProvider> authSchemeRegistry ) {
        this.authSchemeRegistry = authSchemeRegistry;

        return this;
    }

    public HttpRequestExecutor setAuthCache( AuthCache authCache ) {
        if ( authCache != null ) {
            this.authCache = authCache;
        }

        return this;
    }

    public HttpRequestExecutor setHeader( IHeaderCreator headerCreator ) {
        this.headerCreator = headerCreator;

        return this;
    }

    public String executeUrlRequest(String url ) throws CommonUtilException {
        try {
            HttpContext httpContext = getCredentialContext();

            return EntityUtils.toString(  createHttpClient().execute( createHttpUriRequest( url, httpContext ), httpContext ).getEntity() );
        } catch (IOException e) {
            throw new CommonUtilException( e );
        }
    }

    private HttpClientContext getCredentialContext() {
        HttpClientContext context = HttpClientContext.create();

        if ( authCache != null ) {
            context.setAuthCache( authCache );
        }
        context.setCredentialsProvider( credentialsProvider );

        return context;
    }

    private HttpClient createHttpClient() throws CommonUtilException {
        return createHttpClientBuilder().build();
    }

    private HttpClientBuilder createHttpClientBuilder() throws CommonUtilException {
        return httpClientBuilderSetupAction.apply( HttpClients.custom());
    }

    private HttpUriRequest createHttpUriRequest( String uri, HttpContext httpClientContext ) throws CommonUtilException {
        HttpUriRequest httpUriRequest = new HttpGet( uri );

        if ( headerCreator != null ) {
            httpUriRequest.setHeader( headerCreator.createHeader( httpUriRequest, httpClientContext ) );
        }

        return httpUriRequest;
    }

    private HttpClientBuilder setDefaultAuthSchemeRegistry( HttpClientBuilder httpClientBuilder ) {
        return authSchemeRegistry != null ? httpClientBuilder.setDefaultAuthSchemeRegistry( authSchemeRegistry )
                : httpClientBuilder;
    }

    private HttpClientBuilder setDefaultCredentialsProvider( HttpClientBuilder httpClientBuilder ) {
        return credentialsProvider != null ? httpClientBuilder.setDefaultCredentialsProvider( credentialsProvider )
                : httpClientBuilder;
    }

    private HttpClientBuilder setSslContextAllowAll( HttpClientBuilder httpClientBuilder ) {
        try {
            return httpClientBuilder.setSSLContext( SslContextCreator.get().createSslContextAllowAll() ).setSSLHostnameVerifier( new NoopHostnameVerifier());
        } catch (CommonUtilException e) {
            //Add logging
            return httpClientBuilder;
        }
    }
}
