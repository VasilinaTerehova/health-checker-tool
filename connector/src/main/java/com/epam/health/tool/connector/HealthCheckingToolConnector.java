package com.epam.health.tool.connector;

import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import com.epam.util.http.HttpRequestExecutor;

public class HealthCheckingToolConnector {
    public static void main( String... args ) {
        if ( isParamsValid( args ) ) {
            try {
                String jsonResult = HttpRequestExecutor.get().executeUrlRequest( "http://10.6.117.32:8888/check/cluster/" + args[0] );
                HealthCheckResult healthCheckResult = CommonJsonHandler.get()
                        .getTypedValue( jsonResult, HealthCheckResult.class );
                if ( healthCheckResult != null ) {
                    System.out.println( healthCheckResult.isClusterHealthy() );
                }
                else {
                    reportError( "Unexpected result - " + jsonResult );
                }
            } catch (CommonUtilException e) {
                reportError( e.getMessage() );
            }
        }
        else {
            reportError( "Not enough params!" );
        }
    }

    private static boolean isParamsValid( String... args ) {
        return !CheckingParamsUtil.isParamsNullOrEmpty( args ) && args.length > 0;
    }

    private static void reportError( String message ) {
        System.err.println( message );
    }
}
