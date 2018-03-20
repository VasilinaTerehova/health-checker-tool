package com.epam.health.tool.model;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
public enum ServiceTypeEnum {
    HBASE("hbase", "hbase"),
    HIVE("hive", "hive"),
    IMPALA("impala", "impala"),
    YARN("yarn", "yarn"),
    OOZIE("oozie", "oozie"),
    SQOOP("sqoop", "sqoop");

    String code;
    String title;

    ServiceTypeEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }
}
