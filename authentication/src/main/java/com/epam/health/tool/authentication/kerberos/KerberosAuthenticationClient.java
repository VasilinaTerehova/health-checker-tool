package com.epam.health.tool.authentication.kerberos;

import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.authentication.http.HttpAuthenticationClient;
import com.epam.health.tool.authentication.ssh.SshAuthenticationClient;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.file.DownloadedFileWrapper;
import com.epam.util.common.file.FileCommonUtil;
import com.epam.util.kerberos.HadoopKerberosUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import java.io.File;
import java.nio.file.Paths;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Scope( "singleton" )
public class KerberosAuthenticationClient {
    @Autowired
    private SshAuthenticationClient sshAuthenticationClient;
    private ReentrantLock lock = new ReentrantLock();
    private String lastUsedSubjectClusterName;
    private Subject lastUsedSubject;
    private static final Logger logger = Logger.getLogger( KerberosAuthenticationClient.class );

    public <T> T makeDoAsAction( ClusterEntity clusterEntity, PrivilegedExceptionAction<T> action ) throws AuthenticationRequestException {
        lock.lock();
        try {
            return Subject.doAs( getKerberosSubject( clusterEntity ), action );
        } catch (PrivilegedActionException e) {
            throw new AuthenticationRequestException( e );
        }
        finally {
            lock.unlock();
        }
    }

    private Subject getKerberosSubject( ClusterEntity clusterEntity ) {
        if ( lastUsedSubject != null && clusterEntity.getClusterName().equals( lastUsedSubjectClusterName ) ) {
            logger.info( "Use subject from cache fro cluster - " + lastUsedSubjectClusterName );
            return lastUsedSubject;
        }
        else {
            Subject subject = createKerberosSubject( clusterEntity );
            if ( subject != null ) {
                lastUsedSubject = subject;
                lastUsedSubjectClusterName = clusterEntity.getClusterName();

                return lastUsedSubject;
            }
        }

        throw new RuntimeException( "Can't create kerberos subject for cluster - " + clusterEntity.getClusterName() );
    }

    private Subject createKerberosSubject(ClusterEntity clusterEntity ) {
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
