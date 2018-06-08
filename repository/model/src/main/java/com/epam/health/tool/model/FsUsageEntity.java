/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.health.tool.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Vasilina_Terehova on 4/6/2018.
 */
@Embeddable
public class FsUsageEntity {
    public static final String HDFS_POSTFIX = "_fs";
    public static final String COLUMN_USED = "column_used";
    public static final String COLUMN_TOTAL = "column_total";

    @Column(name = COLUMN_USED + HDFS_POSTFIX)
    private long used;
    @Column(name = COLUMN_TOTAL + HDFS_POSTFIX)
    private long total;

    public FsUsageEntity() {
    }

    public FsUsageEntity(long used, long total) {
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
