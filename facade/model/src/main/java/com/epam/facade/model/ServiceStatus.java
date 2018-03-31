package com.epam.facade.model;

import com.epam.facade.model.projection.ServiceStatusProjection;
import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * this class is common for showing on ui, implements project from database layer,
 * do not update this class for vendor specific transferring, names are used for ui
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceStatus implements ServiceStatusProjection {
    private ServiceTypeEnum type;
    private ServiceStatusEnum healthSummary;

    public ServiceStatusEnum getHealthStatus() {
        return healthSummary;
    }

    public void setHealthStatus(ServiceStatusEnum healthSummary) {
        this.healthSummary = healthSummary;
    }

    @Override
    public ServiceTypeEnum getType() {
        return type;
    }

    public void setType(ServiceTypeEnum type) {
        this.type = type;
    }

    @Override
    public ServiceStatusEnum getHealthSummary() {
        return healthSummary;
    }

    public void setHealthSummary(ServiceStatusEnum healthSummary) {
        this.healthSummary = healthSummary;
    }

    @Override
    public ServiceTypeEnum getDisplayName() {
        return type;
    }

    @Override
    public String toString() {
        return "name: \"" + getDisplayName() + "\" " + getHealthStatus() + " " + getType();
    }
}
