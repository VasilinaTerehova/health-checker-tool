/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

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
    public static final String YARN_RESOURCEMANAGER_WEBAPP_ADDRESS = "yarn.resourcemanager.webapp.address";
    public static final String YARN_RESOURCEMANAGER_HTTPS_WEBAPP_ADDRESS = "yarn.resourcemanager.webapp.https.address";
    public static final String YARN_RESOURCEMANAGER_HA_RM_IDS = "yarn.resourcemanager.ha.rm-ids";
    public static final String YARN_NODEMANAGER_LOG_DIRS = "yarn.nodemanager.log-dirs";
    public static final String YARN_NODEMANAGER_LOCAL_DIRS = "yarn.nodemanager.local-dirs";
  }

  public static class HdfsProperties {
    public static final String DFS_NAMENODE_HTTP_ADDRESS = "dfs.namenode.http-address";
    public static final String DFS_NAMENODE_HTTPS_ADDRESS = "dfs.namenode.https-address";
  }
}
