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

package com.epam.facade.model.projection;

import com.epam.health.tool.model.ClusterTypeEnum;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

public interface ClusterSnapshotEntityProjection {

    @Value("#{target.id}")
    Long getId();

    @Value("#{target.clusterEntity.clusterName}")
    String getName();

    @Value("#{target.clusterEntity.clusterTypeEnum}")
    ClusterTypeEnum getClusterType();

    @Value("#{target.clusterEntity.host}")
    String getHost();

    @Value("#{target.clusterEntity.secured}")
    boolean isSecured();

    @Value("#{target.dateOfSnapshot}")
    Date getDateOfSnapshot();

    @Value("#{target.clusterServiceSnapshotEntityList}")
    List<ServiceStatusHolder> getClusterServiceSnapshotEntityList();

    @Value("#{target.memoryUsageEntity}")
    MemoryUsageEntityProjection getMemoryUsage();

    @Value("#{target.hdfsUsageEntity}")
    HdfsUsageEntityProjection getHdfsUsage();

    @Value("#{target.nodeSnapshotEntities}")
    List<? extends NodeSnapshotEntityProjection> getNodes();

}
