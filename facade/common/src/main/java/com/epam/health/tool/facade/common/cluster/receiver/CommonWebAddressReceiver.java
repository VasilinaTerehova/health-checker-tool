package com.epam.health.tool.facade.common.cluster.receiver;

import com.epam.facade.model.cluster.receiver.WebAppAddressParam;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.cluster.receiver.ISingleParamReceiver;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.StringUtils;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public abstract class CommonWebAddressReceiver implements ISingleParamReceiver<String> {
    protected HttpAuthenticationClient httpAuthenticationClient;

    public CommonWebAddressReceiver( HttpAuthenticationClient httpAuthenticationClient ) {
        this.httpAuthenticationClient = httpAuthenticationClient;
    }

    protected abstract Logger log();

    protected String getHAWebAppAddress(String[] rmIds, WebAppAddressParam webAppAddressParam, IRunningClusterParamReceiver runningClusterParamReceiver) {
        ForkJoinPool forkJoinPool = new ForkJoinPool( rmIds.length );

        try {
            return forkJoinPool.submit( () -> Arrays.stream( rmIds ).parallel().map(rmId -> createHAWebAppProperty( webAppAddressParam.getWebAppPrefix(), rmId ) )
                    .map( webappPropertyName -> getHAAddress( webappPropertyName, webAppAddressParam, runningClusterParamReceiver ) )
                    .map( address -> createUrl( webAppAddressParam.getHttpPrefix(), address ) )
                    .filter( rmAddress -> isAddressAvailable( rmAddress, webAppAddressParam.getClusterName() ) )
                    .findFirst().orElse( StringUtils.EMPTY ) ).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            if ( forkJoinPool.isShutdown() ) {
                forkJoinPool.shutdownNow();
            }
        }

        return StringUtils.EMPTY;
    }

    private String getHAAddress( String webappPropertyName, WebAppAddressParam webAppAddressParam, IRunningClusterParamReceiver runningClusterParamReceiver ) {
        log().info( "Extract property - ".concat( webappPropertyName ).concat( " from cluster - " ).concat( webAppAddressParam.getClusterName() )
                .concat( " from file - " ).concat( webAppAddressParam.getSourceFileName() ) );
        try {
            return CheckingParamsUtil.isParamsNotNullOrEmpty( webappPropertyName )
                    ? runningClusterParamReceiver.getPropertySiteXml( webAppAddressParam.getClusterName(), webAppAddressParam.getSourceFileName(), webappPropertyName )
                    : StringUtils.EMPTY;
        } catch (InvalidResponseException e) {
            return StringUtils.EMPTY;
        }
    }

    private String[] getHAIds( String haIdPropertyName, WebAppAddressParam webAppAddressParam,
                               IRunningClusterParamReceiver runningClusterParamReceiver ) throws InvalidResponseException {
        String haIds = runningClusterParamReceiver.getPropertySiteXml( webAppAddressParam.getClusterName(), webAppAddressParam.getSourceFileName(), haIdPropertyName );

        return CheckingParamsUtil.isParamsNotNullOrEmpty( haIds ) ? haIds.split( "," ) : new String[]{};
    }

    private String createHAWebAppProperty( String propertyPrefix, String rmId ) {
        return CheckingParamsUtil.isParamsNotNullOrEmpty( propertyPrefix ) ? propertyPrefix.concat( "." ).concat( rmId ) : StringUtils.EMPTY;
    }

    private String createUrl( String httpPrefix, String address ) {
        return CheckingParamsUtil.isParamsNotNullOrEmpty( httpPrefix, address ) ? httpPrefix.concat( address ) : StringUtils.EMPTY;
    }

    private boolean isAddressAvailable( String rmAddress, String clusterName ) {
        log().info( "Check address - ".concat( rmAddress ).concat( " from cluster - " ).concat( clusterName ) );
        try {
            if ( CheckingParamsUtil.isParamsNotNullOrEmpty( rmAddress, clusterName ) ) {
                this.httpAuthenticationClient.makeAuthenticatedRequest( clusterName, rmAddress );
            }
        }
        catch ( AuthenticationRequestException ex ) {
            //Catch Connection refused exception message
            return false;
        }

        return true;
    }

    private String throwAddressNotFoundException( String message ) throws InvalidResponseException {
        throw new InvalidResponseException( message );
    }
}
