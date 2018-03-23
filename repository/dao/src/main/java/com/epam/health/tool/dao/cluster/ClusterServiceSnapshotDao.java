package com.epam.health.tool.dao.cluster;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.ClusterServiceShapshotEntity;
import com.epam.health.tool.model.ClusterShapshotEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/20/2018.
 */
@Repository
public interface ClusterServiceSnapshotDao extends CrudRepository<ClusterServiceShapshotEntity, Long> {
    @Query("select ce from ClusterEntity ce left join ClusterShapshotEntity cse on cse.clusterEntity.id=ce.id " +
            " group by ce.id having max(cse.dateOfSnapshot)<?1 or count(cse)=0")
    List<ClusterEntity> findClustersForSnapshot(Date lastHourCheck);
}
