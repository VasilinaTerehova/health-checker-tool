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

import com.epam.facade.model.accumulator.results.BaseActionResult;
import com.epam.facade.model.projection.JobResultProjection;
import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.health.tool.model.ServiceStatusEnum;
import com.epam.health.tool.model.ServiceTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * this class is common for showing on ui, implements project from database layer,
 * do not update this class for vendor specific transferring, names are used for ui
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceStatus implements ServiceStatusHolder, BaseActionResult {
    private ServiceTypeEnum type;
    private ServiceStatusEnum healthSummary;
    private List<JobResultProjection> jobResults;
    private String logDirectory;
    private String clusterNode;

    //todo: delete health status
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
    public String getLogDirectory() {
        return logDirectory;
    }

    @Override
    public String getClusterNode() {
        return clusterNode;
    }

    @Override
    public List<JobResultProjection> getJobResults() {
        return jobResults;
    }

    public void setJobResults(List<JobResultProjection> jobResults) {
        this.jobResults = jobResults;
    }

    @Override
    public void setLogDirectory(String logDirectory) {
        this.logDirectory = logDirectory;
    }

    @Override
    public void setClusterNode(String clusterNode) {
        this.clusterNode = clusterNode;
    }

    @Override
    public String toString() {
        return "name: \"" + getDisplayName() + "\" " + getHealthStatus() + " " + getType();
    }

    @Override
    public HealthCheckActionType getHealthActionType() {
        return HealthCheckActionType.OTHER_SERVICES;
    }
}
