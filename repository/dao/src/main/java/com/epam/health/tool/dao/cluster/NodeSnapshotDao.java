package com.epam.health.tool.dao.cluster;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterIdsProjection;
import com.epam.health.tool.model.ClusterEntity;
import com.epam.health.tool.model.NodeSnapshotEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/20/2018.
 */
@Repository
public interface NodeSnapshotDao extends CrudRepository<NodeSnapshotEntity, Long> {
}
