package com.epam.health.tool.facade.common.cluster.receiver;

import com.epam.facade.model.fs.HdfsNamenodeJson;
import com.epam.facade.model.service.DownloadableFileConstants;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.context.holder.NodesContextHolder;
import com.epam.health.tool.context.holder.StringContextHolder;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.receiver.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.context.IApplicationContext;
import com.epam.facade.model.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.StringUtils;
import com.epam.util.common.json.CommonJsonHandler;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static com.epam.facade.model.service.DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTPS_ADDRESS;
import static com.epam.facade.model.service.DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTP_ADDRESS;
import static com.epam.facade.model.service.DownloadableFileConstants.YarnProperties.*;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
public abstract class CommonRuningClusterParamReceiver implements IRunningClusterParamReceiver {
    protected HttpAuthenticationClient httpAuthenticationClient;
    protected ClusterDao clusterDao;
    private IApplicationContext applicationContext;

    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String RM_ADDRESS_CACHE = "RM_ADDRESS_CACHE";
    private static final String NAME_NODE_ADDRESS_CACHE = "NAME_NODE_ADDRESS_CACHE";
    private static final String NODE_LIST_CACHE = "NODE_LIST_CACHE";

    public CommonRuningClusterParamReceiver(HttpAuthenticationClient httpAuthenticationClient, ClusterDao clusterDao, IApplicationContext applicationContext) {
        this.httpAuthenticationClient = httpAuthenticationClient;
        this.clusterDao = clusterDao;
        this.applicationContext = applicationContext;
    }

    @Override
    public String getPropertySiteXml(String clusterName, String siteName, String propertyName) throws InvalidResponseException {
        return getPropertySiteXml( clusterDao.findByClusterName( clusterName ), siteName, propertyName );
    }

    @Override
    public String getLogDirectory( String clusterName ) throws InvalidResponseException {
        String logDirPropery = getPropertySiteXml( clusterName, DownloadableFileConstants.ServiceFileName.YARN, YARN_NODEMANAGER_LOG_DIRS);

        log().info( "Log dir for cluster - ".concat( clusterName ).concat( " dir - " ).concat( logDirPropery ) );
        return logDirPropery;
    }

    public Set<String> getLiveNodes( String clusterName ) throws InvalidResponseException {
        Set<String> liveNodes = getNodeListFromCache( clusterName );

        if ( CheckingParamsUtil.isParamListNotNullOrEmpty( liveNodes ) ) {
            log().info( "Nodes list from cache - ".concat( liveNodes.toString() ) );
        }
        else {
            liveNodes = getHdfsNamenodeJson( clusterName ).getLiveNodes();
            addNodeListToCache( clusterName, liveNodes );
        }

        return liveNodes;
    }

    public HdfsNamenodeJson getHdfsNamenodeJson( String clusterName ) throws InvalidResponseException {
        try {
            String url = getNameNodeUrl( clusterName ) + "/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo";
            String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterName, url );
            HdfsNamenodeJson hdfsUsageJson = CommonJsonHandler.get().getTypedValueFromInnerFieldArrElement(answer, HdfsNamenodeJson.class, "beans");

            log().info( "Get for url - ".concat( url ).concat( " answer - \n" ).concat( answer ).concat( "\nHdfsNamenodeUsage:\n" ).concat( hdfsUsageJson.toString() ) );

            return hdfsUsageJson;
        }
        catch ( CommonUtilException | AuthenticationRequestException ex ) {
            throw new InvalidResponseException( ex );
        }
    }

    public String getNameNodeUrl( String clusterName ) throws InvalidResponseException {
        String nameNodeUrl = getNodeAddressFromCache( clusterName );

        if ( isAddressAvailable( nameNodeUrl, clusterName ) ) {
            log().info( "From cache namenode url - ".concat( nameNodeUrl ) );
        }
        else {
            nameNodeUrl = getRealNameNodeUrl( clusterName );
            addNodeAddressToCache( clusterName, nameNodeUrl );
        }

        return nameNodeUrl;
    }

    public String getActiveResourceManagerAddress( String clusterName ) throws InvalidResponseException {
        String rmAddress = getRmAddressFromCache( clusterName );

        if ( isAddressAvailable( rmAddress, clusterName ) ) {
            log().info( "From cache resource manager url - ".concat( rmAddress ) );
        }
        else {
            rmAddress = getResourceManagerAddress( clusterName );
            addRmAddressToCache( clusterName, rmAddress );
        }

        return rmAddress;
    }

    private String getRealNameNodeUrl( String clusterName ) throws InvalidResponseException {
        String nameNodeUrl = getPropertySiteXml( clusterName, DownloadableFileConstants.ServiceFileName.HDFS, DFS_NAMENODE_HTTP_ADDRESS );

        if (CheckingParamsUtil.isParamsNullOrEmpty(nameNodeUrl)) {
            //possibly ha mode for rm
            String[] rmIds = getHAIds( clusterName, "dfs.ha.namenodes." + clusterName, DownloadableFileConstants.ServiceFileName.HDFS );
            nameNodeUrl = getHAWebAppAddress( rmIds, clusterName, DFS_NAMENODE_HTTP_ADDRESS.concat( "." ).concat( clusterName ), DownloadableFileConstants.ServiceFileName.HDFS, HTTP );
            if ( CheckingParamsUtil.isParamsNullOrEmpty( nameNodeUrl ) ) {
                nameNodeUrl = getHAWebAppAddress( rmIds, clusterName, DFS_NAMENODE_HTTPS_ADDRESS.concat( "." ).concat( clusterName ), DownloadableFileConstants.ServiceFileName.HDFS, HTTPS );
            }
        }
        else {
            nameNodeUrl = HTTP.concat( nameNodeUrl );
        }

        return isAddressAvailable( nameNodeUrl, clusterName ) ? nameNodeUrl
                : throwAddressNotFoundException( "Namenode url not found for cluster - ".concat( clusterName ) );
    }

    private String getResourceManagerAddress( String clusterName ) throws InvalidResponseException {
        String rmAddress = getPropertySiteXml( clusterName, DownloadableFileConstants.ServiceFileName.YARN, YARN_RESOURCEMANAGER_WEBAPP_ADDRESS);

        if (CheckingParamsUtil.isParamsNullOrEmpty(rmAddress)) {
            //possibly ha mode for rm
            String[] rmIds = getHAIds( clusterName, YARN_RESOURCEMANAGER_HA_RM_IDS, DownloadableFileConstants.ServiceFileName.YARN );
            rmAddress = getHAWebAppAddress( rmIds, clusterName, YARN_RESOURCEMANAGER_WEBAPP_ADDRESS, DownloadableFileConstants.ServiceFileName.YARN, HTTP );
            if ( CheckingParamsUtil.isParamsNullOrEmpty( rmAddress ) ) {
                rmAddress = getHAWebAppAddress( rmIds, clusterName, YARN_RESOURCEMANAGER_HTTPS_WEBAPP_ADDRESS, DownloadableFileConstants.ServiceFileName.YARN, HTTPS );
            }
        }
        else {
            rmAddress = HTTP.concat( rmAddress );
        }

        System.out.println("rm address: " + rmAddress);

        return isAddressAvailable( rmAddress, clusterName ) ? rmAddress
                : throwAddressNotFoundException( "RM address url not found for cluster - ".concat( clusterName ) );
    }

    protected abstract String getPropertySiteXml( ClusterEntity clusterEntity, String siteName, String propertyName ) throws InvalidResponseException;
    protected abstract Logger log();

    //Cache operations
    /*------------------------------------------------------------------------------------------------------*/
    private String getRmAddressFromCache( String clusterName ) {
        return applicationContext.getFromContext( clusterName, RM_ADDRESS_CACHE, StringContextHolder.class, StringUtils.EMPTY );
    }

    private String getNodeAddressFromCache( String clusterName ) {
        return applicationContext.getFromContext( clusterName, NAME_NODE_ADDRESS_CACHE, StringContextHolder.class, StringUtils.EMPTY );
    }

    private Set<String> getNodeListFromCache(String clusterName ) {
        return applicationContext.getFromContext( clusterName, NODE_LIST_CACHE, NodesContextHolder.class, Collections.emptySet());
    }

    private void addRmAddressToCache( String clusterName, String rmAddress ) {
        applicationContext.addToContext( clusterName, RM_ADDRESS_CACHE, StringContextHolder.class, new StringContextHolder( rmAddress ));
    }

    private void addNodeAddressToCache( String clusterName, String nodeAddress ) {
        applicationContext.addToContext( clusterName, NAME_NODE_ADDRESS_CACHE, StringContextHolder.class, new StringContextHolder( nodeAddress ));
    }

    private void addNodeListToCache( String clusterName, Set<String> nodeList ) {
        applicationContext.addToContext( clusterName, NODE_LIST_CACHE, NodesContextHolder.class, new NodesContextHolder( nodeList ));
    }
    /*--------------------------------------------------------------------------------------------------------------------------------------*/

    private String getHAWebAppAddress( String[] rmIds, String clusterName, String webAppPrefix, String serviceFileName, String httpPrefix ) {
        ForkJoinPool forkJoinPool = new ForkJoinPool( rmIds.length );

        try {
            return forkJoinPool.submit( () -> Arrays.stream( rmIds ).parallel().map( rmId -> createHAWebAppProperty( webAppPrefix, rmId ) )
                    .map( webappPropertyName -> getHAAddress( webappPropertyName, clusterName, serviceFileName ) )
                    .map( address -> createUrl( httpPrefix, address ) )
                    .filter( rmAddress -> isAddressAvailable( rmAddress, clusterName ) )
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

    private String getHAAddress( String webappPropertyName, String clusterName, String serviceFileName ) {
        log().info( "Extract property - ".concat( webappPropertyName ).concat( " from cluster - " ).concat( clusterName )
                .concat( " from file - " ).concat( serviceFileName ) );
        try {
            return CheckingParamsUtil.isParamsNotNullOrEmpty( webappPropertyName )
                    ? getPropertySiteXml( clusterName, serviceFileName, webappPropertyName )
                    : StringUtils.EMPTY;
        } catch (InvalidResponseException e) {
            return StringUtils.EMPTY;
        }
    }

    private String[] getHAIds( String clusterName, String haIdPropertyName, String serviceFileName ) throws InvalidResponseException {
        String haIds = getPropertySiteXml( clusterName, serviceFileName, haIdPropertyName);

        return CheckingParamsUtil.isParamsNotNullOrEmpty( haIds ) ? haIds.split( "," ) : new String[]{};
    }

    private String createHAWebAppProperty( String propertyPrefix, String rmId ) {
        return CheckingParamsUtil.isParamsNotNullOrEmpty( propertyPrefix ) ? propertyPrefix.concat( "." ).concat( rmId ) : StringUtils.EMPTY;
    }

    private String createUrl( String httpPrefix, String address ) {
        return CheckingParamsUtil.isParamsNotNullOrEmpty( httpPrefix, address ) ? httpPrefix.concat( address ) : StringUtils.EMPTY;
    }

    private boolean isAddressAvailable( String rmAddress, String clusterName ) {
        try {
            if ( CheckingParamsUtil.isParamsNotNullOrEmpty( rmAddress, clusterName ) ) {
                log().info( "Check address - ".concat( rmAddress ).concat( " from cluster - " ).concat( clusterName ) );
                this.httpAuthenticationClient.makeAuthenticatedRequest( clusterName, rmAddress );
            }
            else {
                return false;
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
