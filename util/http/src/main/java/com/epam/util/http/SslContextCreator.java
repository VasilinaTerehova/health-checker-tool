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
    private SslContextCreator() {}

    public static SslContextCreator get() {
        return new SslContextCreator();
    }

    public SSLContext createSslContextAllowAll() throws CommonUtilException {
        try {
            return new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();
        } catch ( NoSuchAlgorithmException | KeyStoreException | KeyManagementException e ) {
            throw new CommonUtilException( e );
        }
    }
}
