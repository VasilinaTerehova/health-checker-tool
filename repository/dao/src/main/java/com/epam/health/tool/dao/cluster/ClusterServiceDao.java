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

import com.epam.health.tool.model.ClusterServiceEntity;
import com.epam.health.tool.model.ServiceTypeEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Vasilina_Terehova on 3/20/2018.
 */
@Repository
public interface ClusterServiceDao extends CrudRepository<ClusterServiceEntity, Long> {
    @Query("select c from #{#entityName} c left join c.clusterEntity ce where ce.id=?1 and c.serviceType=?2")
    ClusterServiceEntity findByClusterIdAndServiceType(Long clusterId, ServiceTypeEnum serviceTypeEnum);
}
