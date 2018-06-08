/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.facade.model;

import java.util.*;

public enum HealthCheckActionType {
    FS, MEMORY, HDFS_MEMORY, YARN_SERVICE, HDFS_SERVICE, OTHER_SERVICES, NONE, ALL;

    public static boolean containAllActionTypes(Collection<HealthCheckActionType> actionTypes) {
        List<HealthCheckActionType> healthCheckActionTypes = new ArrayList<HealthCheckActionType>();
        healthCheckActionTypes.addAll(Arrays.asList(HealthCheckActionType.values()));
        healthCheckActionTypes.remove(NONE);
        healthCheckActionTypes.remove(ALL);
        return actionTypes.containsAll(healthCheckActionTypes);
    }

    public static void main(String[] args) {
        HealthCheckActionType.containAllActionTypes(new HashSet<>());
    }

    public static List<HealthCheckActionType> all() {
        List<HealthCheckActionType> healthCheckActionTypes = new ArrayList<HealthCheckActionType>();
        healthCheckActionTypes.addAll(Arrays.asList(HealthCheckActionType.values()));
        healthCheckActionTypes.remove(NONE);
        healthCheckActionTypes.remove(ALL);
        return healthCheckActionTypes;
    }
}
