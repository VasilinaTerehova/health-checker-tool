package com.epam.util.http;

import com.epam.util.common.CommonUtilException;
import com.epam.util.http.header.IHeaderCreator;
import org.apache.http.Header;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static com.epam.util.http.SslContextCreator.createSslContext;

public class HttpRequestExecutor {
    private CredentialsProvider credentialsProvider;
    private List<String> authSchemes;
    private List<IHeaderCreator> headerCreatorList;

    private HttpRequestExecutor() {
        this.authSchemes = new ArrayList<>();
        this.headerCreatorList = new ArrayList<>();
    }

    public static HttpRequestExecutor get(){
        return new HttpRequestExecutor();
    }

    public HttpRequestExecutor setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;

        return this;
    }

    public HttpRequestExecutor setAuthSchemes(List<String> authSchemes) {
        this.authSchemes = authSchemes;

        return this;
    }

    public HttpRequestExecutor setHeaderCreator(IHeaderCreator headerCreator) {
        if ( headerCreator != null ) {
            this.headerCreatorList.add( headerCreator );
        }

        return this;
    }

    public String executeUrlRequest(String url ) throws CommonUtilException {
        try {
            HttpClientContext credentialContext = getCredentialContext();
            return EntityUtils.toString(  createHttpClient().execute( createHttpUriRequest( url ), credentialContext ).getEntity() );
        } catch (IOException e) {
            throw new CommonUtilException( e );
        }
    }

    private HttpClientContext getCredentialContext() {
        Credentials useJaasCreds = new Credentials() {

            public String getPassword() {
                return null;
            }

            public Principal getUserPrincipal() {
                return null;
            }

        };
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials( new AuthScope( null, -1, AuthScope.ANY_REALM ), useJaasCreds );
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider( credentialsProvider );
        return context;
    }

    private HttpClientBuilder createHttpClientBuilder() {
        return HttpClientBuilder.create().setDefaultCredentialsProvider( credentialsProvider );
    }

    private RequestBuilder createRequestBuilder(String uri ) {
        return RequestBuilder.get().setUri( uri ).setConfig(RequestConfig.custom()
                .setTargetPreferredAuthSchemes( authSchemes ).build());
    }

    private HttpClient createHttpClient() throws CommonUtilException {
        HttpClientBuilder builder = createHttpClientBuilder();
        builder.setSSLContext( createSslContext() );

        //CloseableHttpClient httpclient = builder.build();
        Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
                .register( AuthSchemes.SPNEGO, new SPNegoSchemeFactory(  ) )
                .register( AuthSchemes.BASIC, new BasicSchemeFactory(  ) )
                .build();
        HttpClient httpClient = HttpClientBuilder.create().setDefaultAuthSchemeRegistry( authSchemeRegistry ).build();
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        Credentials useJaasCreds = new Credentials() {

            public String getPassword() {
                return null;
            }

            public Principal getUserPrincipal() {
                return null;
            }

        };
        credentialsProvider.setCredentials( new AuthScope( null, -1, AuthScope.ANY_REALM ), useJaasCreds );
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider( credentialsProvider );
        return httpClient;
    }

    private HttpUriRequest createHttpUriRequest(String uri ) throws CommonUtilException {
        HttpUriRequest uriRequest = createRequestBuilder( uri ).build();
        headerCreatorList.forEach( headerCreator -> setHeader( uriRequest, headerCreator ) );

        return uriRequest;
    }

    private void setHeader( HttpUriRequest httpUriRequest, IHeaderCreator headerCreator ) {
        Header header = headerCreator.createHeader( httpUriRequest );

        if ( header != null ) {
            httpUriRequest.setHeader( header );
        }
    }
}
