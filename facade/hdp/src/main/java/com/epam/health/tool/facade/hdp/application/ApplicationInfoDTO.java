package com.epam.health.tool.facade.hdp.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationInfoDTO {
    private String id;
    private String name;
    private Date startedTime;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(Date startedTime) {
        this.startedTime = startedTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
