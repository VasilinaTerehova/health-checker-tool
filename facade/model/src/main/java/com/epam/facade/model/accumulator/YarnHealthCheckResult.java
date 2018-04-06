package com.epam.facade.model.accumulator;

import java.util.List;

public class YarnHealthCheckResult {
    private List<YarnJob> yarnJobs;

    public List<YarnJob> getYarnJobs() {
        return yarnJobs;
    }

    public void setYarnJobs(List<YarnJob> yarnJobs) {
        this.yarnJobs = yarnJobs;
    }

    public static class YarnJob {
        private String name;
        private boolean success;
        private List<String> alerts;

        public YarnJob(String name, boolean success, List<String> alerts) {
            this.name = name;
            this.success = success;
            this.alerts = alerts;
        }

        public String getName() {
            return name;
        }

        public boolean isSuccess() {
            return success;
        }

        public List<String> getAlerts() {
            return alerts;
        }
    }
}
