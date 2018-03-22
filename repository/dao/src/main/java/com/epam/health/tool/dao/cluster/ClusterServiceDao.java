package com.epam.health.tool.dao.cluster;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.model.ClusterServiceEntity;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
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
