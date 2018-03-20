package com.epam.health.tool.model;

import com.epam.health.tool.common.AbstractManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.epam.health.tool.common.AbstractEntity.DELIMITER_INDEX;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
@Entity
@Table(name = ClusterServiceShapshotEntity.TABLE_NAME)
public class ClusterServiceShapshotEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "cluster_service_snapshot";
    public static final String COLUMN_SERVICE_STATUS = "service_status_";
    public static final String COLUMN_FK_CLUSTER_SERVICE = ClusterServiceEntity.TABLE_NAME;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER_SERVICE)
    private ClusterServiceEntity clusterServiceEntity;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = COLUMN_SERVICE_STATUS)
    private ServiceStatusEnum serviceStatusEnum;

    public ClusterServiceEntity getClusterServiceEntity() {
        return clusterServiceEntity;
    }

    public void setClusterServiceEntity(ClusterServiceEntity clusterServiceEntity) {
        this.clusterServiceEntity = clusterServiceEntity;
    }

    public ServiceStatusEnum getServiceStatusEnum() {
        return serviceStatusEnum;
    }

    public void setServiceStatusEnum(ServiceStatusEnum serviceStatusEnum) {
        this.serviceStatusEnum = serviceStatusEnum;
    }
}
