package com.epam.health.tool.model;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
public enum ClusterTypeEnum {
    CDH("cdh", "cdh"),
    HDP("hdp", "hdp"),
    MAPR("mapr", "mapr"),
    HDI("hdi", "hdi"),
    EMR("emr", "emr");

    String code;
    String title;

    ClusterTypeEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }
}
