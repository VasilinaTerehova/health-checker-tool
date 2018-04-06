package com.epam.health.tool.facade.common.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleJson {
    private String name;
    private YarnRoleEnum type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YarnRoleEnum getType() {
        return type;
    }

    public void setType(YarnRoleEnum type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "type: " + type +  " name: " + name;
    }
}
