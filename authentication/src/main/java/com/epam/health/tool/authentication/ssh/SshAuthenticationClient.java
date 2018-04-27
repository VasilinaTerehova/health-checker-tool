package com.epam.health.tool.authentication.ssh;

import com.epam.health.tool.authentication.exception.AuthenticationRequestException;
import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.credentials.SshCredentialsEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.file.DownloadedFileWrapper;
import com.epam.util.ssh.SshCommonUtil;
import com.epam.util.ssh.delegating.SshExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SshAuthenticationClient {
    @Autowired
    private ClusterDao clusterDao;

    public SshExecResult executeCommand( String clusterName, String command ) throws AuthenticationRequestException {
        return executeCommand( getClusterEntity( clusterName ), command );
    }

    public SshExecResult executeCommand(String clusterName, String command, String host) throws AuthenticationRequestException {
        return executeCommand( getClusterEntity( clusterName ), command, host );
    }

    public SshExecResult executeCommand(ClusterEntity clusterEntity, String command) throws AuthenticationRequestException {
        return executeCommand(clusterEntity, command, clusterEntity.getHost());
    }

    public SshExecResult executeCommand(ClusterEntity clusterEntity, String command, String host) throws AuthenticationRequestException {
        try {
            return SshCommonUtil.buildSshCommandExecutor( clusterEntity.getSsh().getUsername(), clusterEntity.getSsh().getPassword(), clusterEntity.getSsh().getPemFilePath() )
                    .executeCommand( trimHost( host ), command );
        } catch (CommonUtilException e) {
            throw new AuthenticationRequestException( e );
        }
    }
    public SshExecResult executeCommand( SshCredentialsEntity sshCredentialsEntity, String command, String host ) throws AuthenticationRequestException {
        try {
            return SshCommonUtil.buildSshCommandExecutor( sshCredentialsEntity.getUsername(), sshCredentialsEntity.getPassword(), sshCredentialsEntity.getPemFilePath() )
                    .executeCommand( trimHost( host ), command );
        } catch (CommonUtilException e) {
            throw new AuthenticationRequestException( e );
        }
    }

    public DownloadedFileWrapper downloadFile(ClusterEntity clusterEntity, String pathToFile) throws AuthenticationRequestException {
        try {
            return SshCommonUtil.buildSshSftpDownloader( clusterEntity.getSsh().getUsername(), clusterEntity.getSsh().getPassword(), clusterEntity.getSsh().getPemFilePath() )
                    .downloadViaSftpAsFileWrapper( clusterEntity.getHost(), pathToFile );
        } catch (CommonUtilException e) {
            throw new AuthenticationRequestException( e );
        }
    }

    public DownloadedFileWrapper downloadFile( String clusterName, String command ) throws AuthenticationRequestException {
        return downloadFile( getClusterEntity( clusterName ), command );
    }

    private ClusterEntity getClusterEntity( String clusterName ) {
        return clusterDao.findByClusterName( clusterName );
    }

    private String trimHost( String host ) {
        return host.contains( ":" ) ? host.split( ":" )[0] : host;
    }
}
