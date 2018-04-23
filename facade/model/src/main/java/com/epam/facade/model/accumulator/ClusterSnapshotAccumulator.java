package com.epam.facade.model.accumulator;

import java.util.Date;

public class ClusterSnapshotAccumulator {
    private Long id;
    private Date dateOfSnapshot;
    private String token;
    private String clusterName;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateOfSnapshot(Date dateOfSnapshot) {
        if ( dateOfSnapshot!= null ) {
            this.dateOfSnapshot = dateOfSnapshot;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setClusterName(String clusterName) {
        if ( clusterName != null ) {
            this.clusterName = clusterName;
        }
    }

    public Long getId() {
        return id;
    }

    public Date getDateOfSnapshot() {
        return dateOfSnapshot;
    }

    public String getToken() {
        return token;
    }

    public String getClusterName() {
        return clusterName;
    }
}
