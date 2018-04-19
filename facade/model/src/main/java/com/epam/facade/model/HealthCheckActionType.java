package com.epam.facade.model;

import java.util.*;

public enum HealthCheckActionType {
    FS, MEMORY, HDFS_MEMORY, YARN_SERVICE, HDFS_SERVICE, OTHER_SERVICES, NONE, ALL;

    public static boolean containAllActionTypes(Set<HealthCheckActionType> actionTypes) {
        List<HealthCheckActionType> healthCheckActionTypes = new ArrayList<HealthCheckActionType>();
        healthCheckActionTypes.addAll(Arrays.asList(HealthCheckActionType.values()));
        healthCheckActionTypes.remove(NONE);
        healthCheckActionTypes.remove(ALL);
        return actionTypes.containsAll(healthCheckActionTypes);
    }

    public static void main(String[] args) {
        HealthCheckActionType.containAllActionTypes(new HashSet<>());
    }
}
