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

import com.epam.facade.model.projection.ClusterSnapshotEntityProjection;
import com.epam.facade.model.projection.ServiceStatusHolder;

import java.util.List;

public class ClusterHealthSummary {
    //todo: rename cluster - also on ui - to cluster snapshot, cluster name is not valid
    private ClusterSnapshotEntityProjection cluster;
    private List<ServiceStatusHolder> serviceStatusList;

    public ClusterHealthSummary(ClusterSnapshotEntityProjection cluster, List<ServiceStatusHolder> serviceProjectionsBy) {
        setCluster(cluster);
        //todo: this field can be accessed via onetomany field, now refresh can't be called normally, replace with refresh
        setServiceStatusList(serviceProjectionsBy);
    }

    public ClusterHealthSummary(ClusterSnapshotEntityProjection cluster) {
        setCluster(cluster);
        setServiceStatusList(cluster.getClusterServiceSnapshotEntityList());
    }

    public ClusterSnapshotEntityProjection getCluster() {
        return cluster;
    }

    public void setCluster(ClusterSnapshotEntityProjection cluster) {
        this.cluster = cluster;
    }

    public List<ServiceStatusHolder> getServiceStatusList() {
        return serviceStatusList;
    }

    public void setServiceStatusList(List<ServiceStatusHolder> serviceStatusList) {
        this.serviceStatusList = serviceStatusList;
    }

}
