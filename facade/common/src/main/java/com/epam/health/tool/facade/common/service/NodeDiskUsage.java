package com.epam.health.tool.facade.common.service;

import com.epam.facade.model.projection.NodeSnapshotEntityProjection;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
public class NodeDiskUsage implements NodeSnapshotEntityProjection {
    private String nodeName;
    private String used;
    private int usedGb;
    private String total;
    private int totalGb;
    private String percent;
    private float percentF;

    public NodeDiskUsage(String nodeName, String used, String total, String percent) {
        this.nodeName = nodeName;
        this.used = used;
        usedGb = Integer.parseInt(used.replaceAll("[^\\d]", "" ));
        this.total = total;
        totalGb = Integer.parseInt(total.replaceAll("[^\\d]", "" ));
        this.percent = percent;
        percentF = Float.parseFloat(percent.replaceAll("%", "" ));
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String getNode() {
        return nodeName;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public int getUsedGb() {
        return usedGb;
    }

    public int getTotalGb() {
        return totalGb;
    }

    public float getPercentF() {
        return percentF;
    }

    @Override
    public String toString() {
        return "used " + usedGb + " out of " + totalGb + ", percent + " + percentF;
    }
}
