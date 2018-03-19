package com.epam.health_tool.model;

/**
 * Created by Vasilina_Terehova on 3/6/2018.
 */
public enum ServiceType {
    HBASE("Hbase"),
    HIVE("Hive"),
    ZOOKEEPER("Zookeeper"),
    OOZIE("Oozie");

    private String serviceName;

    ServiceType(String serviceName) {
        this.serviceName = serviceName;
    }

    public static ServiceType parseValue(String value) {
        for (ServiceType serviceType : ServiceType.values()) {
            if (serviceType.serviceName.equals(value)) {
                return serviceType;
            }
        }
        return null;
    }
}
