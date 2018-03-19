package com.epam.health.tool.authenticate;

import com.epam.health.tool.authenticate.impl.SshCredentials;

public interface ISshCredentials {
  SshCredentials getSshCredentials();

  void setSshCredentials(SshCredentials sshCredentials);
}
