package com.epam.health_tool.model;

/**
 * Created by Vasilina_Terehova on 3/6/2018.
 */
public class ServiceStatus {
    //todo: this fields should be transferred, Gson - need to be investigated
    ServiceType serviceType;
    //todo: this fields should be transferred, Gson - need to be investigated
    HealthStatus healthStatus;
    String name;
    String healthSummary;

    public ServiceStatus() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHealthSummary() {
        return healthSummary;
    }

    public void setHealthSummary(String healthSummary) {
        this.healthSummary = healthSummary;
    }
}
