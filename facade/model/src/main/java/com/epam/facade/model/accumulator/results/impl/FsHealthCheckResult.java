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

package com.epam.facade.model.accumulator.results.impl;

import com.epam.facade.model.HealthCheckActionType;
import com.epam.facade.model.accumulator.results.BaseActionResult;
import com.epam.facade.model.projection.HdfsUsageEntityProjection;
import com.epam.facade.model.projection.MemoryUsageEntityProjection;
import com.epam.facade.model.projection.NodeSnapshotEntityProjection;

import java.util.List;

public class FsHealthCheckResult implements BaseActionResult {
    //Fs check result
    private MemoryUsageEntityProjection memoryUsageEntityProjection;
    private HdfsUsageEntityProjection hdfsUsageEntityProjection;
    private List<? extends NodeSnapshotEntityProjection> nodeSnapshotEntityProjections;

    public MemoryUsageEntityProjection getMemoryUsageEntityProjection() {
        return memoryUsageEntityProjection;
    }

    public void setMemoryUsageEntityProjection(MemoryUsageEntityProjection memoryUsageEntityProjection) {
        this.memoryUsageEntityProjection = memoryUsageEntityProjection;
    }

    public HdfsUsageEntityProjection getHdfsUsageEntityProjection() {
        return hdfsUsageEntityProjection;
    }

    public void setHdfsUsageEntityProjection(HdfsUsageEntityProjection hdfsUsageEntityProjection) {
        this.hdfsUsageEntityProjection = hdfsUsageEntityProjection;
    }

    public List<? extends NodeSnapshotEntityProjection> getNodeSnapshotEntityProjections() {
        return nodeSnapshotEntityProjections;
    }

    public void setNodeSnapshotEntityProjections(List<? extends NodeSnapshotEntityProjection> nodeSnapshotEntityProjections) {
        this.nodeSnapshotEntityProjections = nodeSnapshotEntityProjections;
    }

    @Override
    public HealthCheckActionType getHealthActionType() {
        return HealthCheckActionType.FS;
    }
}
