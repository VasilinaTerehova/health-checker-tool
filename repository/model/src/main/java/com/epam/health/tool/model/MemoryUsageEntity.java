package com.epam.health.tool.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
@Embeddable
public class MemoryUsageEntity {
    public static final String HDFS_POSTFIX = "_memory";
    public static final String COLUMN_USED = "column_used";
    public static final String COLUMN_TOTAL = "column_total";

    @Column(name = COLUMN_USED + HDFS_POSTFIX)
    private long used;
    @Column(name = COLUMN_TOTAL + HDFS_POSTFIX)
    private long total;

    public MemoryUsageEntity() {
    }

    public MemoryUsageEntity(long used, long total) {
        this.used = used;
        this.total = total;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getUsed() {
        return used;
    }

    public long getTotal() {
        return total;
    }
}
