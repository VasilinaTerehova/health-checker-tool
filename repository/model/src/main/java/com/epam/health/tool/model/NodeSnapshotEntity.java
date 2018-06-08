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
 * Created by Vasilina_Terehova on 4/6/2018.
 */
@Entity
@Table(name = NodeSnapshotEntity.TABLE_NAME)
public class NodeSnapshotEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "node_snapshot";
    public static final String COLUMN_NODE = "column_name_";
    public static final String COLUMN_FK_CLUSTER_SNAPSHOT = ClusterSnapshotEntity.TABLE_NAME;

    @Embedded
    FsUsageEntity fsUsageEntity;

    @Column(name = COLUMN_NODE)
    private String node;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER_SNAPSHOT)
    private ClusterSnapshotEntity clusterSnapshotEntity;

    public NodeSnapshotEntity() {
    }

    public NodeSnapshotEntity(FsUsageEntity fsUsageEntity, String node, @NotNull ClusterSnapshotEntity clusterSnapshotEntity) {
        this.fsUsageEntity = fsUsageEntity;
        this.node = node;
        this.clusterSnapshotEntity = clusterSnapshotEntity;
    }

    public FsUsageEntity getFsUsageEntity() {
        return fsUsageEntity;
    }

    public void setFsUsageEntity(FsUsageEntity fsUsageEntity) {
        this.fsUsageEntity = fsUsageEntity;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public ClusterSnapshotEntity getClusterSnapshotEntity() {
        return clusterSnapshotEntity;
    }

    public void setClusterSnapshotEntity(ClusterSnapshotEntity clusterSnapshotEntity) {
        this.clusterSnapshotEntity = clusterSnapshotEntity;
    }
}
