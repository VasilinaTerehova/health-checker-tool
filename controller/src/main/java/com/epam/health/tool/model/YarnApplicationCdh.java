package com.epam.health.tool.model;

import java.util.Date;

/**
 * Created by Vasilina_Terehova on 3/7/2018.
 */
public class YarnApplicationCdh {
    String applicationId;
    String name;
    Date startTime;
    String state;

    public YarnApplicationCdh() {
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
