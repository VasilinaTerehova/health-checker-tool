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

package com.epam.facade.model;

import com.epam.facade.model.projection.*;
import com.epam.health.tool.model.ClusterTypeEnum;

import java.util.Date;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/30/2018.
 */
public class ClusterSnapshotEntityProjectionImpl implements ClusterSnapshotEntityProjection {
    private final List<ServiceStatusHolder> serviceStatusHolderList;
    private final ClusterEntityProjection clusterEntityProjection;
    private final MemoryUsageEntityProjection memoryUsageEntityProjection;
    private final HdfsUsageEntityProjection hdfsUsageEntityProjection;
    private final List<? extends NodeSnapshotEntityProjection> nodeSnapshotEntityProjectionList;

    public ClusterSnapshotEntityProjectionImpl(ClusterEntityProjection clusterEntityProjection, List<ServiceStatusHolder> serviceStatusHolderList,
                                               MemoryUsageEntityProjection memoryUsageEntityProjection, HdfsUsageEntityProjection hdfsUsageEntityProjection,
                                               List<? extends NodeSnapshotEntityProjection> nodeSnapshotEntityProjectionList) {
        this.clusterEntityProjection = clusterEntityProjection;
        this.serviceStatusHolderList = serviceStatusHolderList;
        this.memoryUsageEntityProjection = memoryUsageEntityProjection;
        this.hdfsUsageEntityProjection = hdfsUsageEntityProjection;
        this.nodeSnapshotEntityProjectionList = nodeSnapshotEntityProjectionList;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getName() {
        return clusterEntityProjection.getName();
    }

    @Override
    public ClusterTypeEnum getClusterType() {
        return clusterEntityProjection.getClusterType();
    }

    @Override
    public String getHost() {
        return clusterEntityProjection.getHost();
    }

    @Override
    public boolean isSecured() {
        return clusterEntityProjection.isSecured();
    }

    @Override
    public Date getDateOfSnapshot() {
        return new Date();
    }

    @Override
    public List<ServiceStatusHolder> getClusterServiceSnapshotEntityList() {
        return serviceStatusHolderList;
    }

    @Override
    public MemoryUsageEntityProjection getMemoryUsage() {
        return memoryUsageEntityProjection;
    }

    @Override
    public HdfsUsageEntityProjection getHdfsUsage() {
        return hdfsUsageEntityProjection;
    }

    @Override
    public List<? extends NodeSnapshotEntityProjection> getNodes() {
        return nodeSnapshotEntityProjectionList;
    }

}
