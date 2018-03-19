package com.epam.util.http;

import com.epam.util.common.CommonUtilException;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class SslContextCreator {
    public static SSLContext createSslContext() throws CommonUtilException {
        try {
            SSLContextBuilder sslcb = new SSLContextBuilder();
            sslcb.loadTrustMaterial( KeyStore.getInstance( KeyStore.getDefaultType() ),
                    new TrustSelfSignedStrategy() );

            return sslcb.build();
        } catch ( NoSuchAlgorithmException | KeyStoreException | KeyManagementException e ) {
            throw new CommonUtilException( e );
        }
    }
}
