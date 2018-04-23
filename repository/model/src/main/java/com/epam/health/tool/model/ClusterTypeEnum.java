package com.epam.health.tool.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
public enum ClusterTypeEnum {
    CDH("cdh", "cdh"),
    HDP("hdp", "hdp"),
    MAPR("mapr", "mapr"),
    HDI("hdi", "hdi"),
    EMR("emr", "emr"),
    NONE("none", "none");

    String code;
    String title;

    ClusterTypeEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public static ClusterTypeEnum parse(String code) {
        Optional<ClusterTypeEnum> any = Arrays.stream(ClusterTypeEnum.values()).filter(clusterTypeEnum -> clusterTypeEnum.code.equals(code)).findAny();
        return any.orElse(null);
    }
}
