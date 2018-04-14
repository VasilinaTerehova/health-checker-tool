package com.epam.facade.model.service;

public class DownloadableFileConstants {
  public static class ServiceName {
    public static final String SSL_TRUSTSTORE = "ssl_truststore";
    public static final String HDFS = "hdfs";
    public static final String HIVE = "hive";
    public static final String HBASE = "hbase";
    public static final String YARN = "yarn";
    public static final String MAPREDUCE2 = "mapreduce2";
    public static final String EMR = "emr";
  }

  public static class ServiceFileName {
    public static final String SSL_TRUSTSTORE = "ssl_truststore";
    public static final String HDFS = "hdfs-site.xml";
    public static final String CORE = "core-site.xml";
    public static final String HIVE = "hive-site.xml";
    public static final String HBASE = "hbase-site.xml";
    public static final String YARN = "yarn-site.xml";
    public static final String MAPRED = "mapred-site.xml";
    public static final String EMRFS = "emrfs-site.xml";
  }

  public static class YarnProperties {
    public static final String YARN_RESOURCEMANAGER_WEBAPP_ADDRESS = "yarn.resourcemanager.webapp.address";;
    public static final String YARN_NODEMANAGER_LOG_DIRS = "yarn.nodemanager.log-dirs";
  }

  public static class HdfsProperties {
    public static final String DFS_NAMENODE_HTTP_ADDRESS = "dfs.namenode.http-address";
  }

}
