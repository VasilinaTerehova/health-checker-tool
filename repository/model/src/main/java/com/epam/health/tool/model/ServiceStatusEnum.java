package com.epam.health.tool.model;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
public enum ServiceStatusEnum {
    GOOD("ok", "ok"),
    BAD("bad", "bad"),
    CONCERNING("avg", "concerning"),
    STARTED("STARTED", "STARTED"),
    INSTALLED("INSTALLED", "INSTALLED");

    String code;
    String title;

    ServiceStatusEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }
}
