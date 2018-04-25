package com.epam.health.tool.authentication.ssh;

import com.epam.health.tool.dao.cluster.ClusterDao;
import com.epam.health.tool.model.ClusterEntity;
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

    public SshExecResult executeCommand(ClusterEntity clusterEntity, String command) {
        return executeCommand(clusterEntity, command, clusterEntity.getHost());
    }

    public SshExecResult executeCommand(ClusterEntity clusterEntity, String command, String host) {
        try {
            return SshCommonUtil.buildSshCommandExecutor( clusterEntity.getSsh().getUsername(), clusterEntity.getSsh().getPassword(), clusterEntity.getSsh().getPemFilePath() )
                    .executeCommand( trimHost( host ), command );
        } catch (CommonUtilException e) {
            throw new RuntimeException( e );
        }
    }

    public DownloadedFileWrapper downloadFile(ClusterEntity clusterEntity, String pathToFile) {
        try {
            return SshCommonUtil.buildSshSftpDownloader( clusterEntity.getSsh().getUsername(), clusterEntity.getSsh().getPassword(), clusterEntity.getSsh().getPemFilePath() )
                    .downloadViaSftpAsFileWrapper( clusterEntity.getHost(), pathToFile );
        } catch (CommonUtilException e) {
            throw new RuntimeException( e );
        }
    }

    public SshExecResult executeCommand( String clusterName, String command ) {
        return executeCommand( getClusterEntity( clusterName ), command );
    }

    public DownloadedFileWrapper downloadFile( String clusterName, String command ) {
        return downloadFile( getClusterEntity( clusterName ), command );
    }

    private ClusterEntity getClusterEntity( String clusterName ) {
        return clusterDao.findByClusterName( clusterName );
    }

    private String trimHost( String host ) {
        return host.contains( ":" ) ? host.split( ":" )[0] : host;
    }
}
