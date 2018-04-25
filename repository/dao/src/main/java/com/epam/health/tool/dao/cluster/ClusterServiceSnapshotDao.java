package com.epam.health.tool.dao.cluster;

import com.epam.facade.model.projection.ServiceStatusProjection;
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
    List<ServiceStatusProjection> findServiceProjectionsBy(Long clusterSnapshotId);

    @Query("select csse from ClusterServiceSnapshotEntity csse left join csse.clusterSnapshotEntity cse left join csse.clusterServiceEntity csere where cse.id=?1 and csere.serviceType=?2")
    ClusterServiceSnapshotEntity findByClusterSnapshotIdServiceId(Long clusterSnapshotId, ServiceTypeEnum serviceType);

}
