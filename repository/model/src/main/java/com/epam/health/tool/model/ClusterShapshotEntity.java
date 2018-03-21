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
@Table(name = ClusterShapshotEntity.TABLE_NAME)
public class ClusterShapshotEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "cluster_snapshot";
    public static final String COLUMN_DATE_OF_SNAPSHOT = "date_of_snapshot_";
    public static final String COLUMN_COUNT_OF_RUNNING_APPS = "count_of_runnings_apps_";
    public static final String COLUMN_USED_MEMORY = "used_memory_";
    public static final String COLUMN_FK_CLUSTER = ClusterEntity.TABLE_NAME;

    @Column(name = COLUMN_DATE_OF_SNAPSHOT)
    private Date dateOfSnapshot;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER)
    private ClusterEntity clusterEntity;

    @Column(name = COLUMN_USED_MEMORY)
    private long usedMemory;

    @Column(name = COLUMN_COUNT_OF_RUNNING_APPS)
    private long countOfRunningApps;

    public Date getDateOfSnapshot() {
        return dateOfSnapshot;
    }

    public void setDateOfSnapshot(Date dateOfSnapshot) {
        this.dateOfSnapshot = dateOfSnapshot;
    }

    public ClusterEntity getClusterEntity() {
        return clusterEntity;
    }

    public void setClusterEntity(ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public long getCountOfRunningApps() {
        return countOfRunningApps;
    }

    public void setCountOfRunningApps(long countOfRunningApps) {
        this.countOfRunningApps = countOfRunningApps;
    }
}
