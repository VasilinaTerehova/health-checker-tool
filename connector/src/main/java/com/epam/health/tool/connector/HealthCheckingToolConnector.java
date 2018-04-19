package com.epam.health.tool.connector;

import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.json.CommonJsonHandler;
import com.epam.util.http.HttpRequestExecutor;

public class HealthCheckingToolConnector {
    public static void main( String... args ) {
        if ( isParamsValid( args ) ) {
            try {
                String clusterName = HttpRequestExecutor.get()
                        .executeUrlRequest( "http://10.6.117.32:8888/cluster/search?".concat( createRequestParamString( buildSearchParamFromArguments( args ) ) ) );
                if ( !CheckingParamsUtil.isParamsNullOrEmpty( clusterName ) ) {
                    String jsonResult = HttpRequestExecutor.get().executeUrlRequest( "http://10.6.117.32:8888/check/cluster/".concat( clusterName ) );
                    HealthCheckResult healthCheckResult = CommonJsonHandler.get()
                            .getTypedValue( jsonResult, HealthCheckResult.class );
                    if ( healthCheckResult != null ) {
                        System.out.println( healthCheckResult.isClusterHealthy() );
                    }
                    else {
                        reportError( "Unexpected result - ".concat( jsonResult ) );
                    }
                }
                else {
                    reportError( "Unexpected cluster name for parameters - ".concat( createRequestParamString( buildSearchParamFromArguments( args ) ) ) );
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
        return !CheckingParamsUtil.isParamsNullOrEmpty( args ) && args.length > 2;
    }

    private static void reportError( String message ) {
        System.err.println( message );
    }

    private static ClusterSearchParam buildSearchParamFromArguments( String... args ) {
        return ClusterSearchParam.ClusterSearchParamBuilder.get().withNode( args[0] ).withShimName( args[1] ).withSecure( args[2] ).build();
    }

    private static String createRequestParamString( ClusterSearchParam clusterSearchParam ) {
        return ParamStringBuilder.get().buildFromSearchParams( clusterSearchParam );
    }
}
