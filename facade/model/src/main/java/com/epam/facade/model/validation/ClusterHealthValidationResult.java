package com.epam.facade.model.validation;

import com.epam.util.common.StringUtils;

public class ClusterHealthValidationResult {
    private boolean clusterHealthy;
    private String errorSummary;

    public ClusterHealthValidationResult() { }

    public ClusterHealthValidationResult(boolean clusterHealthy) {
        this.clusterHealthy = clusterHealthy;
        this.errorSummary = StringUtils.EMPTY;
    }

    public ClusterHealthValidationResult(boolean clusterHealthy, String errorSummary) {
        this.clusterHealthy = clusterHealthy;
        this.errorSummary = errorSummary;
    }

    public boolean isClusterHealthy() {
        return clusterHealthy;
    }

    public void setClusterHealthy(boolean clusterHealthy) {
        this.clusterHealthy = clusterHealthy;
    }

    public String getErrorSummary() {
        return errorSummary;
    }

    public void setErrorSummary(String errorSummary) {
        this.errorSummary = errorSummary;
    }

    public void appendErrorSummary( String error ) {
        if ( this.errorSummary != null ) {
            this.errorSummary = this.errorSummary.concat( "\n" ).concat( error );
        }
        else {
            this.setErrorSummary( error );
        }
    }
}
