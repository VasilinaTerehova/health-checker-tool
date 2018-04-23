package com.epam.health.tool.model;

import com.epam.health.tool.common.AbstractManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
@Entity
@Table(name = JobResultEntity.TABLE_NAME)
public class JobResultEntity extends AbstractManagedEntity {
    public static final String TABLE_NAME = "job_result";
    public static final String COLUMN_JOB_NAME = "column_name_";
    public static final String COLUMN_DATE_OF_RUN = "date_of_run_";
    public static final String COLUMN_JOB_RESULT = "job_result_";
    public static final String COLUMN_FK_CLUSTER_SERVICE_SNAPSHOT = ClusterServiceShapshotEntity.TABLE_NAME;

    @Column(name = COLUMN_JOB_NAME)
    private String jobName;

    @Column(name = COLUMN_DATE_OF_RUN)
    private Date dateOfRun;

    @Column(name = COLUMN_JOB_RESULT)
    private boolean result;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER_SERVICE_SNAPSHOT)
    private ClusterServiceShapshotEntity clusterServiceShapshotEntity;

    public JobResultEntity() {
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getDateOfRun() {
        return dateOfRun;
    }

    public void setDateOfRun(Date dateOfRun) {
        this.dateOfRun = dateOfRun;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ClusterServiceShapshotEntity getClusterServiceShapshotEntity() {
        return clusterServiceShapshotEntity;
    }

    public void setClusterServiceShapshotEntity(ClusterServiceShapshotEntity clusterServiceShapshotEntity) {
        this.clusterServiceShapshotEntity = clusterServiceShapshotEntity;
    }
}
