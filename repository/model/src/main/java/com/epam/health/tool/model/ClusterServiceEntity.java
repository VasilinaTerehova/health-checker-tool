package com.epam.health.tool.model;

import com.epam.health.tool.common.AbstractManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Vasilina_Terehova on 3/19/2018.
 */
@Entity
@Table(name = ClusterServiceEntity.TABLE_NAME)
public class ClusterServiceEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "cluster_service";
    public static final String COLUMN_LOG_FILE_PATH = "log_file_path_";
    public static final String COLUMN_CLUSTER_NODE = "cluster_node_";
    public static final String COLUMN_SERVICE_TYPE = "service_type_";
    public static final String COLUMN_FK_CLUSTER = ClusterEntity.TABLE_NAME;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER)
    private ClusterEntity clusterEntity;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = COLUMN_SERVICE_TYPE)
    private ServiceTypeEnum serviceTypeEnum;

    @Column(name = COLUMN_LOG_FILE_PATH)
    private String logPath;

    @Column(name = COLUMN_CLUSTER_NODE)
    private String clusterNode;

    public ServiceTypeEnum getServiceTypeEnum() {
        return serviceTypeEnum;
    }

    public void setServiceTypeEnum(ServiceTypeEnum serviceTypeEnum) {
        this.serviceTypeEnum = serviceTypeEnum;
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
