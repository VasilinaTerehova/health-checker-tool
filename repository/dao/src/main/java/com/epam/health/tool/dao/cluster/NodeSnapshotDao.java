package com.epam.health.tool.dao.cluster;

import com.epam.health.tool.model.NodeSnapshotEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Vasilina_Terehova on 3/20/2018.
 */
@Repository
public interface NodeSnapshotDao extends CrudRepository<NodeSnapshotEntity, Long> {
}
