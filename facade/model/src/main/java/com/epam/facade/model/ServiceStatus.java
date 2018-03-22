package com.epam.facade.model;

import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceStatus {
    @JsonProperty( "type" )
    private ServiceTypeEnum serviceType;
    @JsonProperty( "healthSummary" )
    private ServiceStatusEnum healthSummary;
    @JsonProperty( "displayName" )
    private String name;

    public ServiceTypeEnum getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeEnum serviceType) {
        this.serviceType = serviceType;
    }

    public ServiceStatusEnum getHealthStatus() {
        return healthSummary;
    }

    public void setHealthStatus(ServiceStatusEnum healthSummary) {
        this.healthSummary = healthSummary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
