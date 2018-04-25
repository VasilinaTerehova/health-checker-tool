package com.epam.health.tool.facade.hdp.cluster;

import com.epam.health.tool.model.ServiceTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HdpServiceStatusDTO {
    @JsonProperty( "service_name" )
    private ServiceTypeEnum serviceType;
    @JsonProperty( "state" )
    private String healthSummary;

    public ServiceTypeEnum getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeEnum serviceType) {
        this.serviceType = serviceType;
    }

    public String getHealthStatus() {
        return healthSummary;
    }

    public void setHealthStatus(String healthSummary) {
        this.healthSummary = healthSummary;
    }
}
