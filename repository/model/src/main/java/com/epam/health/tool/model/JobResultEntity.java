package com.epam.health.tool.model;

import com.epam.health.tool.common.AbstractManagedEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public static final String COLUMN_ALERTS = "alerts_";
    public static final String COLUMN_FK_CLUSTER_SERVICE_SNAPSHOT = ClusterServiceSnapshotEntity.TABLE_NAME;

    @Column(name = COLUMN_JOB_NAME)
    private String jobName;

    @Column(name = COLUMN_DATE_OF_RUN)
    private Date dateOfRun;

    @Column(name = COLUMN_JOB_RESULT)
    private boolean result;

    @Column(name = COLUMN_ALERTS)
    @Lob
    private String alerts;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = TABLE_NAME + DELIMITER_INDEX + COLUMN_FK_CLUSTER_SERVICE_SNAPSHOT)
    private ClusterServiceSnapshotEntity clusterServiceSnapshotEntity;

    public JobResultEntity() {
    }

    public JobResultEntity(String jobName, Date dateOfRun, boolean result, @NotNull ClusterServiceSnapshotEntity clusterServiceSnapshotEntity, List<String> alerts) {
        this.jobName = jobName;
        this.dateOfRun = dateOfRun;
        this.result = result;
        this.clusterServiceSnapshotEntity = clusterServiceSnapshotEntity;
        setAlerts(alerts);
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

    public ClusterServiceSnapshotEntity getClusterServiceSnapshotEntity() {
        return clusterServiceSnapshotEntity;
    }

    public void setClusterServiceSnapshotEntity(ClusterServiceSnapshotEntity clusterServiceSnapshotEntity) {
        this.clusterServiceSnapshotEntity = clusterServiceSnapshotEntity;
    }

    public List<String> getAlerts() {
        try {
            return new ObjectMapper().readValue(alerts, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void setAlerts(List<String> alerts) {
        try {
            String alertsToDb = new ObjectMapper().writeValueAsString(alerts);
            this.alerts = alertsToDb;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
