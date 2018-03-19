package com.epam.health_tool.util;

import com.epam.health_tool.authenticate.impl.SshCredentials;
import com.epam.health_tool.delegating.ssh.DelegatingSshSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SshCommonUtil {
    public String executeCommand(SshCredentials sshCredentials, String host, int port, String command) throws
            CommonUtilException {
        try (DelegatingSshSession sshSession = createDelegationSshSession(sshCredentials, host, port)) {
            return sshSession.executeCommand(command);
        } catch (IOException ex) {
            throw new CommonUtilException(ex);
        }
    }

    public String downloadViaSftp(SshCredentials sshCredentials, String host, int port, String source) throws
            CommonUtilException {
        try (DelegatingSshSession sshSession = createDelegationSshSession(sshCredentials, host, port)) {
            return sshSession.downloadFile(source);
        } catch (IOException ex) {
            throw new CommonUtilException(ex);
        }
    }

    public DownloadedFileWrapper downloadViaSftpAsFileWrapper(SshCredentials sshCredentials, String host, int port,
                                                              String source) throws CommonUtilException {
        try (DelegatingSshSession sshSession = createDelegationSshSession(sshCredentials, host, port)) {
            return new DownloadedFileWrapper(sshSession.downloadFileAsByteArray(source));
        } catch (IOException ex) {
            throw new CommonUtilException(ex);
        }
    }

    DelegatingSshSession createDelegationSshSession(SshCredentials sshCredentials, String host, int port)
            throws IOException {
        return new DelegatingSshSession(sshCredentials.getUsername(), host, port,
                sshCredentials.getPassword(), sshCredentials.getIdentityPath());
    }
}
