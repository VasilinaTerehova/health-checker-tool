package com.epam.health.tool.authentication.ssh;

import com.epam.health.tool.model.ClusterEntity;
import com.epam.util.common.CommonUtilException;
import com.epam.util.common.file.DownloadedFileWrapper;
import com.epam.util.ssh.SshCommonUtil;
import org.springframework.stereotype.Component;

@Component
public class SshAuthenticationClient {
    public String executeCommand(ClusterEntity clusterEntity, String command) {
        try {
            return SshCommonUtil.buildSshCommandExecutor( clusterEntity.getSsh().getUsername(), clusterEntity.getSsh().getPassword(), clusterEntity.getSsh().getPemFilePath() )
                    .executeCommand( clusterEntity.getHost(), command );
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
}
