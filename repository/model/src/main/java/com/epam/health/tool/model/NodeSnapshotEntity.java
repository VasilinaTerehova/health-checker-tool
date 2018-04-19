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
