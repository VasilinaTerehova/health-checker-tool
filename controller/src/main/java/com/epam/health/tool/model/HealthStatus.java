package com.epam.health.tool.model;

/**
 * Created by Vasilina_Terehova on 3/3/2018.
 */
public enum HealthStatus {
    GOOD("GOOD"),
    BAD("BAD"),
    CONCERNING("CONCERNING");

    private String statusName;

    HealthStatus(String statusName) {
        this.statusName = statusName;
    }

    public static HealthStatus parseValue(String value) {
        for (HealthStatus healthStatus : HealthStatus.values()) {
            if (healthStatus.statusName.equals(value)) {
                return healthStatus;
            }
        }
        return null;
    }

    public String getStatusName() {
        return statusName;
    }

}
