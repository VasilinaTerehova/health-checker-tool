package com.epam.health.tool.dao.cluster;

import com.epam.facade.model.projection.ClusterEntityProjection;
import com.epam.facade.model.projection.ClusterIdsProjection;
import com.epam.health.tool.model.ClusterEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Vasilina_Terehova on 3/20/2018.
 */
@Repository
public interface ClusterDao extends CrudRepository<ClusterEntity, Long> {
    @Query("select c from #{#entityName} c")
    List<ClusterEntityProjection> findAllProjections();
    ClusterEntityProjection findProjectionByClusterName(String clusterName );
    ClusterEntity findByClusterName( String clusterName );
    @Query("select c from #{#entityName} c where c.clusterName = ?1")
    ClusterIdsProjection findIdsByClusterName(String clusterName );
    @Query("select c from #{#entityName} c where c.host = ?1")
    List<String> findClusterNamesByHost( String host );
    @Query("select c from #{#entityName} c where c.id = ?1")
    ClusterIdsProjection findIdsByClusterId( Long id );
    void deleteByClusterName( String clusterName );
}
