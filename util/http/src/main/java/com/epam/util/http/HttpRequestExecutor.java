package com.epam.util.http;

import com.epam.util.common.CommonUtilException;
import com.epam.util.http.header.IHeaderCreator;
import org.apache.http.Header;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.epam.util.http.SslContextCreator.createSslContext;

public class HttpRequestExecutor {
    private CredentialsProvider credentialsProvider;
    private Registry<AuthSchemeProvider> authSchemeRegistry;
    //HttpClientBuilder setup chaining functions
    private Function<HttpClientBuilder, HttpClientBuilder> httpClientBuilderSetupAction;

    private HttpRequestExecutor() {
        httpClientBuilderSetupAction = this::setDefaultAuthSchemeRegistry;
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

    public String executeUrlRequest(String url ) throws CommonUtilException {
        try {
            return EntityUtils.toString(  createHttpClient().execute( createHttpUriRequest( url ), getCredentialContext() ).getEntity() );
        } catch (IOException e) {
            throw new CommonUtilException( e );
        }
    }

    private HttpClientContext getCredentialContext() {
        HttpClientContext context = HttpClientContext.create();
//        AuthCache authCache = new BasicAuthCache();
        context.setCredentialsProvider( credentialsProvider );

        return context;
    }

    private HttpClient createHttpClient() throws CommonUtilException {
        //CloseableHttpClient httpclient = builder.build();
//        Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
//                .register( AuthSchemes.SPNEGO, new SPNegoSchemeFactory(  ) )
//                .register( AuthSchemes.BASIC, new BasicSchemeFactory(  ) )
//                .build();
//        HttpClient httpClient = HttpClientBuilder.create().setDefaultAuthSchemeRegistry( authSchemeRegistry ).build();
//        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        Credentials useJaasCreds = new Credentials() {
//
//            public String getPassword() {
//                return null;
//            }
//
//            public Principal getUserPrincipal() {
//                return null;
//            }
//
//        };
//        credentialsProvider.setCredentials( new AuthScope( null, -1, AuthScope.ANY_REALM ), useJaasCreds );
//
//        CredentialsProvider credsProvider = new BasicCredentialsProvider();
//        credsProvider.setCredentials(new AuthScope(null, -1, null), useJaasCreds);
//        Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(true)).build();
        return createHttpClientBuilder().build();
    }

    private HttpClientBuilder createHttpClientBuilder() {
        return httpClientBuilderSetupAction.apply( HttpClients.custom() );
    }

    private HttpUriRequest createHttpUriRequest(String uri ) throws CommonUtilException {
        return new HttpGet( uri );
    }

    private HttpClientBuilder setDefaultAuthSchemeRegistry( HttpClientBuilder httpClientBuilder ) {
        return authSchemeRegistry != null ? httpClientBuilder.setDefaultAuthSchemeRegistry( authSchemeRegistry )
                : httpClientBuilder;
    }

    private HttpClientBuilder setDefaultCredentialsProvider( HttpClientBuilder httpClientBuilder ) {
        return credentialsProvider != null ? httpClientBuilder.setDefaultCredentialsProvider( credentialsProvider )
                : httpClientBuilder;
    }
}
