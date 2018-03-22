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
