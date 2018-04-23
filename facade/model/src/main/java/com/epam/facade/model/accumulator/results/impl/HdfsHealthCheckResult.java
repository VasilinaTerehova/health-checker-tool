package com.epam.facade.model.accumulator.results.impl;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.results.BaseActionResult;
import com.epam.health.tool.model.ServiceStatusEnum;

import java.util.ArrayList;
import java.util.List;

public class HdfsHealthCheckResult implements BaseActionResult {
    private List<HdfsOperationResult> jobResults;
    private ServiceStatusEnum status;

    public ServiceStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ServiceStatusEnum status) {
        this.status = status;
    }

    public HdfsHealthCheckResult() {
        this.jobResults = new ArrayList<>();
    }

    public List<HdfsOperationResult> getJobResults() {
        return jobResults;
    }

    public void setJobResults(List<HdfsOperationResult> jobResults) {
        this.jobResults = jobResults;
    }

    public void addHdfsOperationResult(HdfsOperationResult hdfsOperationResult ) {
        this.jobResults.add( hdfsOperationResult );
    }

    @Override
    public HealthCheckActionType getHealthActionType() {
        return HealthCheckActionType.HDFS_SERVICE;
    }

    public static class HdfsOperationResult {
        private String operationName;
        private boolean success;
        private String alert;

        public HdfsOperationResult(String operationName) {
            this.operationName = operationName;
        }

        public String getOperationName() {
            return operationName;
        }

        public void setOperationName(String operationName) {
            this.operationName = operationName;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }
    }
}
