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
