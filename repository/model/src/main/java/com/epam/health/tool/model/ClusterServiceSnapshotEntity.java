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
import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
@Entity
@Table(name = ClusterServiceSnapshotEntity.TABLE_NAME, uniqueConstraints =
@UniqueConstraint(columnNames = {ClusterServiceSnapshotEntity.FK_CLUSTER_SNAPSHOT, ClusterServiceSnapshotEntity.FK_SERVICE_TYPE}))
public class ClusterServiceSnapshotEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "cluster_service_snapshot";
    public static final String COLUMN_SERVICE_STATUS = "service_status_";
    public static final String COLUMN_FK_CLUSTER_SERVICE = ClusterServiceEntity.TABLE_NAME;
    public static final String FK_SERVICE_TYPE = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER_SERVICE;
    public static final String COLUMN_FK_CLUSTER_SNAPSHOT = ClusterSnapshotEntity.TABLE_NAME;
    public static final String FK_CLUSTER_SNAPSHOT = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER_SNAPSHOT;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FK_SERVICE_TYPE)
    private ClusterServiceEntity clusterServiceEntity;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = COLUMN_SERVICE_STATUS)
    private ServiceStatusEnum healthStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FK_CLUSTER_SNAPSHOT)
    private ClusterSnapshotEntity clusterSnapshotEntity;

    @OneToMany(mappedBy = "clusterServiceSnapshotEntity")
    private List<JobResultEntity> jobResults;

    public ClusterServiceEntity getClusterServiceEntity() {
        return clusterServiceEntity;
    }

    public void setClusterServiceEntity(ClusterServiceEntity clusterServiceEntity) {
        this.clusterServiceEntity = clusterServiceEntity;
    }

    public ServiceStatusEnum getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(ServiceStatusEnum healthStatus) {
        this.healthStatus = healthStatus;
    }

    public ClusterSnapshotEntity getClusterSnapshotEntity() {
        return clusterSnapshotEntity;
    }

    public void setClusterSnapshotEntity(ClusterSnapshotEntity clusterSnapshotEntity) {
        this.clusterSnapshotEntity = clusterSnapshotEntity;
    }

    public List<JobResultEntity> getJobResults() {
        return jobResults;
    }
}
