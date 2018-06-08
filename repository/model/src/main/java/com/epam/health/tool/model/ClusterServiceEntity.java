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

package com.epam.health.tool.model;

import com.epam.health.tool.common.AbstractManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
@Entity
@Table(name = ClusterServiceEntity.TABLE_NAME, uniqueConstraints =
@UniqueConstraint(columnNames = {ClusterServiceEntity.JOIN_COLUMN_CLUSTER, ClusterServiceEntity.COLUMN_SERVICE_TYPE}))
public class ClusterServiceEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "cluster_service";
    public static final String COLUMN_LOG_FILE_PATH = "log_file_path_";
    public static final String COLUMN_CLUSTER_NODE = "cluster_node_";
    public static final String COLUMN_SERVICE_TYPE = "service_type_";
    public static final String COLUMN_FK_CLUSTER = ClusterEntity.TABLE_NAME;
    public static final String JOIN_COLUMN_CLUSTER = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = JOIN_COLUMN_CLUSTER)
    private ClusterEntity clusterEntity;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = COLUMN_SERVICE_TYPE)
    private ServiceTypeEnum serviceType;

    @Column(name = COLUMN_LOG_FILE_PATH)
    private String logPath;

    @Column(name = COLUMN_CLUSTER_NODE)
    private String clusterNode;

    public ServiceTypeEnum getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeEnum serviceType) {
        this.serviceType = serviceType;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getClusterNode() {
        return clusterNode;
    }

    public void setClusterNode(String clusterNode) {
        this.clusterNode = clusterNode;
    }

    public ClusterEntity getClusterEntity() {
        return clusterEntity;
    }

    public void setClusterEntity(ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }
}
