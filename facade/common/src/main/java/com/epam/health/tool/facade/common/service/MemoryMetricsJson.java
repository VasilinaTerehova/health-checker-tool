package com.epam.health.tool.facade.common.service;

import com.epam.facade.model.projection.MemoryUsageEntityProjection;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Vasilina_Terehova on 4/5/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemoryMetricsJson implements MemoryUsageEntityProjection {
    private int availableMB;
    private int reservedMB;
    private int totalMB;
    private int unhealthyNodes;
    private int totalVirtualCores;
    private int totalNodes;
    private int appsRinning;
    private int appPending;
    private int appsCompleted;
    private int appsSubmitted;

    public int getAvailableMB() {
        return availableMB;
    }

    public void setAvailableMB(int availableMB) {
        this.availableMB = availableMB;
    }

    public int getReservedMB() {
        return reservedMB;
    }

    public void setReservedMB(int reservedMB) {
        this.reservedMB = reservedMB;
    }

    public int getTotalMB() {
        return totalMB;
    }

    public void setTotalMB(int totalMB) {
        this.totalMB = totalMB;
    }

    public int getUnhealthyNodes() {
        return unhealthyNodes;
    }

    public void setUnhealthyNodes(int unhealthyNodes) {
        this.unhealthyNodes = unhealthyNodes;
    }

    public int getTotalVirtualCores() {
        return totalVirtualCores;
    }

    public void setTotalVirtualCores(int totalVirtualCores) {
        this.totalVirtualCores = totalVirtualCores;
    }

    public int getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }

    public int getAppsRinning() {
        return appsRinning;
    }

    public void setAppsRinning(int appsRinning) {
        this.appsRinning = appsRinning;
    }

    public int getAppPending() {
        return appPending;
    }

    public void setAppPending(int appPending) {
        this.appPending = appPending;
    }

    public int getAppsCompleted() {
        return appsCompleted;
    }

    public void setAppsCompleted(int appsCompleted) {
        this.appsCompleted = appsCompleted;
    }

    public int getAppsSubmitted() {
        return appsSubmitted;
    }

    public void setAppsSubmitted(int appsSubmitted) {
        this.appsSubmitted = appsSubmitted;
    }

    @Override
    public String toString() {
        return " availableMB: "+ availableMB + " " + " reservedMB: "+ reservedMB + " " + " totalMB: "+ totalMB + " "
                + " unhealthyNodes: "+ unhealthyNodes + " " + " totalVirtualCores: "+ totalVirtualCores + " "
                + " totalNodes: "+ totalNodes + " " + " appsRinning: "+ appsRinning + " "
                + " appPending: "+ appPending + " " + " appsCompleted: "+ appsCompleted + " " + " appsSubmitted: "+ appsSubmitted + " ";
    }

    @Override
    public int getUsed() {
        return reservedMB;
    }

    @Override
    public int getTotal() {
        return totalMB;
    }
}
