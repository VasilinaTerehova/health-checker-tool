package com.epam.health.tool.facade.common.cluster;

import com.epam.facade.model.fs.HdfsNamenodeJson;
import com.epam.facade.model.service.DownloadableFileConstants;
import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.facade.cluster.IRunningClusterParamReceiver;
import com.epam.health.tool.facade.exception.InvalidResponseException;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.StringUtils;
import com.epam.util.common.json.CommonJsonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static com.epam.facade.model.service.DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTPS_ADDRESS;
import static com.epam.facade.model.service.DownloadableFileConstants.HdfsProperties.DFS_NAMENODE_HTTP_ADDRESS;
import static com.epam.facade.model.service.DownloadableFileConstants.YarnProperties.*;

/**
 * Created by Vasilina_Terehova on 4/14/2018.
 */
public abstract class CommonRuningClusterParamReceiver implements IRunningClusterParamReceiver {
    private static final Logger log = LoggerFactory.getLogger( CommonRuningClusterParamReceiver.class );
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    @Autowired
    protected HttpAuthenticationClient httpAuthenticationClient;

    @Autowired
    protected ClusterDao clusterDao;

    @Override
    public String getPropertySiteXml(String clusterName, String siteName, String propertyName) throws InvalidResponseException {
        return getPropertySiteXml( clusterDao.findByClusterName( clusterName ), siteName, propertyName );
    }

    @Override
    public String getLogDirectory( String clusterName ) throws InvalidResponseException {
        String logDirPropery = getPropertySiteXml( clusterName, DownloadableFileConstants.ServiceFileName.YARN, YARN_NODEMANAGER_LOG_DIRS);

        System.out.println("log.dir: " + logDirPropery);
        return logDirPropery;
    }

    public HdfsNamenodeJson getHdfsNamenodeJson( String clusterName ) throws InvalidResponseException {
        try {
            String nameNodeUrl = getNameNodeUrl( clusterName );
            String url = nameNodeUrl + "/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo";

            System.out.println(url);
            String answer = httpAuthenticationClient.makeAuthenticatedRequest( clusterName, url );
            System.out.println(answer);
            HdfsNamenodeJson hdfsUsageJson = CommonJsonHandler.get().getTypedValueFromInnerFieldArrElement(answer, HdfsNamenodeJson.class, "beans");
            System.out.println(hdfsUsageJson);
            return hdfsUsageJson;
        }
        catch ( CommonUtilException | AuthenticationRequestException ex ) {
            throw new InvalidResponseException( ex );
        }
    }

    public String getNameNodeUrl( String clusterName ) throws InvalidResponseException {
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

    public String getActiveResourceManagerAddress( String clusterName ) throws InvalidResponseException {
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
        log.info( "Extract property - ".concat( webappPropertyName ).concat( " from cluster - " ).concat( clusterName )
                .concat( " from file - " ).concat( serviceFileName ) );
        try {
            return CheckingParamsUtil.isParamsNotNullOrEmpty( webappPropertyName )
                    ? getPropertySiteXml( clusterName, serviceFileName, webappPropertyName )
                    : StringUtils.EMPTY;
        } catch (InvalidResponseException e) {
            return StringUtils.EMPTY;
        }
    }

    private String[] getHAIds(String clusterName, String haIdPropertyName, String serviceFileName) throws InvalidResponseException {
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
        log.info( "Check address - ".concat( rmAddress ).concat( " from cluster - " ).concat( clusterName ) );
        try {
            if ( CheckingParamsUtil.isParamsNotNullOrEmpty( rmAddress, clusterName ) ) {
                log.info( "Start - ".concat( new Date().toString() ) );
                this.httpAuthenticationClient.makeAuthenticatedRequest( clusterName, rmAddress );
                log.info( "Done - ".concat( new Date().toString() ) );
            }
        }
        catch ( AuthenticationRequestException ex ) {
            //Catch Connection refused exception message
            log.info( "Done - ".concat( new Date().toString() ) );
            return false;
        }

        return true;
    }

    private String throwAddressNotFoundException( String message ) throws InvalidResponseException {
        throw new InvalidResponseException( message );
    }
}
