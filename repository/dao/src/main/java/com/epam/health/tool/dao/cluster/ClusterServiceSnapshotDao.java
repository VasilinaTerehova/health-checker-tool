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

package com.epam.health.tool.dao.cluster;

import com.epam.facade.model.projection.ServiceStatusHolder;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterServiceSnapshotEntity;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/20/2018.
 */
@Repository
public interface ClusterServiceSnapshotDao extends CrudRepository<ClusterServiceSnapshotEntity, Long> {
    @Query("select ce from ClusterEntity ce left join ClusterSnapshotEntity cse on cse.clusterEntity.id=ce.id " +
            " group by ce.id having max(cse.dateOfSnapshot)<?1 or count(cse)=0")
    List<ClusterEntity> findClustersForSnapshot(Date lastHourCheck);

    @Query("select csse from ClusterServiceSnapshotEntity csse left join csse.clusterSnapshotEntity cse left join csse.clusterServiceEntity csere where cse.id=?1")
    List<ServiceStatusHolder> findServiceProjectionsBy(Long clusterSnapshotId);

    @Query("select csse from ClusterServiceSnapshotEntity csse left join csse.clusterSnapshotEntity cse left join csse.clusterServiceEntity csere where cse.id=?1 and csere.serviceType=?2")
    ClusterServiceSnapshotEntity findByClusterSnapshotIdServiceId(Long clusterSnapshotId, ServiceTypeEnum serviceType);

}
