package com.epam.health.tool.model;

import com.epam.health.tool.common.AbstractManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
@Entity
@Table(name = ClusterServiceSnapshotEntity.TABLE_NAME)
public class ClusterServiceSnapshotEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "cluster_service_snapshot";
    public static final String COLUMN_SERVICE_STATUS = "service_status_";
    public static final String COLUMN_FK_CLUSTER_SERVICE = ClusterServiceEntity.TABLE_NAME;
    public static final String COLUMN_FK_CLUSTER_SNAPSHOT = ClusterSnapshotEntity.TABLE_NAME;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER_SERVICE)
    private ClusterServiceEntity clusterServiceEntity;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = COLUMN_SERVICE_STATUS)
    private ServiceStatusEnum healthStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER_SNAPSHOT)
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
