package com.epam.health_tool.authenticate;

import com.epam.health_tool.authenticate.impl.SshCredentials;

public interface ISshCredentials {
  SshCredentials getSshCredentials();

  void setSshCredentials(SshCredentials sshCredentials);
}
