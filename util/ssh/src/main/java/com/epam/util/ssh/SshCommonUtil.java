package com.epam.util.ssh;

import com.epam.util.ssh.executor.impl.SshCommandExecutor;
import com.epam.util.ssh.executor.impl.SshSftpDownloader;

public class SshCommonUtil {
    public static SshCommandExecutor buildSshCommandExecutor( String username, String password, String identityPath ) {
        return new SshCommandExecutor( username, password, identityPath );
    }

    public static SshSftpDownloader buildSshSftpDownloader( String username, String password, String identityPath ) {
        return new SshSftpDownloader( username, password, identityPath );
    }
}
