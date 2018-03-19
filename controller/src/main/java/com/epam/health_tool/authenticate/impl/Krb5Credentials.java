package com.epam.health_tool.authenticate.impl;

import org.apache.commons.lang.StringUtils;

public class Krb5Credentials {
  String username;
  String password;
  private String keytabLocation;
  private String krb5ConfPath;

  //For test
  public Krb5Credentials() {
    this( StringUtils.EMPTY );
  }

  public Krb5Credentials( String keytabLocation ) {
    this( StringUtils.EMPTY, StringUtils.EMPTY, keytabLocation );
  }

  public Krb5Credentials( String username, String password ) {
    this( username, password, StringUtils.EMPTY );
  }

  public Krb5Credentials( String username, String password, String keytabLocation ) {
    this.username = username;
    this.password = password;
    this.keytabLocation = keytabLocation;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername( String username ) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword( String password ) {
    this.password = password;
  }

  public String getKeytabLocation() {
    return keytabLocation;
  }

  public void setKeytabLocation( String keytabLocation ) {
    this.keytabLocation = keytabLocation;
  }

  public String getKrb5ConfPath() {
    return krb5ConfPath;
  }

  public void setKrb5ConfPath(String krb5ConfPath) {
    this.krb5ConfPath = krb5ConfPath;
  }
}
