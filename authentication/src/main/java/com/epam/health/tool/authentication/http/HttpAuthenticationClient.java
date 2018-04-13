package com.epam.health.tool.authentication.http;

import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.file.DownloadedFileWrapper;
import com.epam.util.common.file.FileCommonUtil;
import com.epam.util.kerberos.HadoopKerberosUtil;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class HttpAuthenticationClient {
    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;
    @Autowired
    private ClusterDao clusterDao;
    private ReentrantLock lock = new ReentrantLock();

    public String makeAuthenticatedRequest(ClusterEntity clusterEntity, String url, boolean useSpnego) {
        return isUsingSpnego( useSpnego, clusterEntity.isSecured() ) ? makeDoAsRequest( clusterEntity, url )
                : makeSimpleRequest( clusterEntity, url );
    }

    public String makeAuthenticatedRequest( String clusterName, String url ) {
        return makeAuthenticatedRequest( getClusterEntity( clusterName ), url, true );
    }

    public String makeAuthenticatedRequest( String clusterName, String url, boolean useSpnego ) {
        return makeAuthenticatedRequest( getClusterEntity( clusterName ), url, useSpnego );
    }

    private boolean isUsingSpnego( boolean useSpnego, boolean secureCluster ) {
        return useSpnego && secureCluster;
    }

    private ClusterEntity getClusterEntity( String clusterName ) {
        return clusterDao.findByClusterName( clusterName );
    }

    private String makeDoAsRequest( ClusterEntity clusterEntity, String url ) {
        lock.lock();
        try {
            return Subject.doAs( createKerberosSubject( clusterEntity ),
                    (PrivilegedExceptionAction<String>) () -> makeSimpleRequest( clusterEntity, url, true ));
        } catch ( PrivilegedActionException e ) {
            throw new RuntimeException( e );
        } finally {
            lock.unlock();
        }
    }

    private String makeSimpleRequest( ClusterEntity clusterEntity, String url ) {
        return makeSimpleRequest( clusterEntity, url, false );
    }

    private String makeSimpleRequest( ClusterEntity clusterEntity, String url, boolean useSpnego ) {
        try {
            return BaseHttpAuthenticatedAction.get()
                    .withUsername( clusterEntity.getHttp().getUsername() )
                    .withPassword( clusterEntity.getHttp().getPassword() )
                    .withSpnego( useSpnego )
                    .makeAuthenticatedRequest( url );
        } catch (CommonUtilException e) {
            throw new RuntimeException( e );
        }
    }

    private Subject createKerberosSubject( ClusterEntity clusterEntity ) {
        try {
            System.setProperty( "java.security.krb5.conf", downloadAndSaveKrb5FileIfNotExists( clusterEntity ) );
            return HadoopKerberosUtil.doLoginWithPrincipalAndPassword(clusterEntity.getKerberos().getUsername(), clusterEntity.getKerberos().getPassword()).getSubject();
        } catch (CommonUtilException e) {
            throw new RuntimeException( e );
        }
    }

    private String downloadAndSaveKrb5FileIfNotExists( ClusterEntity clusterEntity ) {
        DownloadedFileWrapper downloadedFileWrapper = sshAuthenticationClient.downloadFile( clusterEntity, "/etc/krb5.conf" );

        if ( downloadedFileWrapper.isEmpty() ) {
            throw new RuntimeException( "Can't download krb5 file from " + clusterEntity.getHost() );
        }

        try {
            if ( !downloadedFileWrapper.isByteContentEmpty() ) {
                FileCommonUtil.writeByteArrayToFile( createPathToSaveKrb5(clusterEntity.getClusterName()), downloadedFileWrapper.getByteFileContent() );
            }
            else if ( !downloadedFileWrapper.isStringContentEmpty() ) {
                FileCommonUtil.writeStringToFile( createPathToSaveKrb5( clusterEntity.getClusterName() ), downloadedFileWrapper.getStringFileContent() );
            }
        }
        catch ( CommonUtilException e ) {
            throw new RuntimeException( e );
        }

        return createPathToSaveKrb5( clusterEntity.getClusterName() );
    }

    private String createPathToSaveKrb5( String clusterName ) {
        return getRootUtilityFolder() + File.separator + "clusters" + File.separator + clusterName + File.separator + clusterName + "_krb5.conf";
    }

    private String getRootUtilityFolder() {
        try {
            return Paths.get( HttpAuthenticationClient.class.getProtectionDomain().getCodeSource().getLocation().toURI() )
                    .getParent().toAbsolutePath().normalize().toString();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }
}
