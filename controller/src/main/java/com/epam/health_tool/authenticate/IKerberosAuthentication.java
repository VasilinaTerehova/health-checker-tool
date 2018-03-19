package com.epam.health_tool.authenticate;

public interface IKerberosAuthentication {
  boolean isKerberosSet();

  void setKerberosAuth(boolean isKerberos);
}
