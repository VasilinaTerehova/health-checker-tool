package com.epam.health.tool.model;

import java.util.Arrays;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
public enum ServiceTypeEnum {
    HBASE("hbase", "Hbase"),
    HIVE("hive", "Hive 2.0"),
    IMPALA("impala", "Impala"),
    YARN("yarn", "Yarn"),
    OOZIE("oozie", "Oozie"),
    SQOOP("sqoop", "Sqoop"),
    KS_INDEXER("ks_indexer", "KS Indexer"),
    ZOOKEEPER("zookeeper", "Zookeeper"),
    HDFS("hdfs", "Hdfs"),
    UNDEFINED( "undefined", "undefined" );
//    SPARK_ON_YARN("SPARK_ON_YARN", "SPARK_ON_YARN"),
//    SQOOP_CLIENT("sqoop_client", "sqoop_client"),
//    SOLR("SOLR", "SOLR"),
    //HUE("HUE", "HUE"),

    String code;
    String title;

    ServiceTypeEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public static ServiceTypeEnum getTypeByName( String name ) {
        return Arrays.stream( ServiceTypeEnum.values() )
                .filter( serviceTypeEnum -> serviceTypeEnum.name().equals( name ) )
                .findFirst().orElse( ServiceTypeEnum.UNDEFINED );
    }
}
