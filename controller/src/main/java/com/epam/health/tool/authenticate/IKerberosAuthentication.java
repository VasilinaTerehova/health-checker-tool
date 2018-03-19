package com.epam.health.tool.authenticate;

public interface IKerberosAuthentication {
  boolean isKerberosSet();

  void setKerberosAuth(boolean isKerberos);
}
