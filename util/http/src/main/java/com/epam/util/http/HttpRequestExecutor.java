package com.epam.util.http;

import com.epam.util.common.CommonUtilException;
import com.epam.util.http.header.IHeaderCreator;
import org.apache.http.Header;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
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
            return createHttpClient().execute( createHttpUriRequest( url ) ).getEntity().toString();
        } catch (IOException e) {
            throw new CommonUtilException( e );
        }
    }

    private HttpClientBuilder createHttpClientBuilder() {
        return HttpClientBuilder.create().setDefaultCredentialsProvider( credentialsProvider );
    }

    private RequestBuilder createRequestBuilder(String uri ) {
        return RequestBuilder.get().setUri( uri ).setConfig(RequestConfig.custom()
                .setTargetPreferredAuthSchemes( authSchemes ).build());
    }

    private CloseableHttpClient createHttpClient() throws CommonUtilException {
        HttpClientBuilder builder = createHttpClientBuilder();
        builder.setSSLContext( createSslContext() );

        return builder.build();
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
